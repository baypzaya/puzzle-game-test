#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

#define PTM_RATIO 32.0f
#define FLOOR_HEIGHT    -100.0f

CCScene* HelloWorld::scene() {
	// 'scene' is an autorelease object
	CCScene *scene = CCScene::create();

	// 'layer' is an autorelease object
	HelloWorld *layer = HelloWorld::create();

	// add layer as a child to scene
	scene->addChild(layer);

	// return the scene
	return scene;
}

HelloWorld::~HelloWorld() {
	CC_SAFE_RELEASE_NULL(nestArray);
	if (m_world) {
		delete m_world;
		m_world = NULL;
	}
}

// on "init" you need to initialize your instance
bool HelloWorld::init() {
	if (!CCLayer::init()) {
		return false;
	}

	//init data
	setTouchEnabled(true);
	isJumpEggDown = false;
	m_mouseJoint = NULL;
	jumpState = -1;
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();

	//init ui
	//add close menu item
	CCMenuItemImage *pCloseItem = CCMenuItemImage::create("CloseNormal.png", "CloseSelected.png", this,
			menu_selector(HelloWorld::menuCloseCallback));
	pCloseItem->setPosition(ccp(screenSize.width - 20, 20));
	CCMenu* pMenu = CCMenu::create(pCloseItem, NULL);
	pMenu->setPosition(CCPointZero);
	this->addChild(pMenu, 1);

	scoreLable = CCLabelTTF::create("1234", "fonts/Abduction.ttf", 35);
	scoreLable->setAnchorPoint(ccp(0,1));
	scoreLable->setPosition(ccp(20,screenSize.height - 20));
	addChild(scoreLable, 1);

	m_nestLayer = NestLayer::create();
	m_nestLayer->setPosition(CCPointZero);
	m_nestLayer->setAnchorPoint(ccp(0,0));
	addChild(m_nestLayer);

	//add jump egg
	jumpEgg = CCSprite::create("jump_egg.png");
	jumpEgg->setAnchorPoint(ccp(0.5,0));
	jumpEgg->setPosition(ccp(screenSize.width/2,20.0f));
	addChild(jumpEgg);

	followNest = m_nestLayer->catchEgg(jumpEgg);
	//	createNest();

	//init box2d world
	b2Vec2 gravity;
	gravity.Set(0.0f, -10.0f);
	bool doSleep = true;
	m_world = new b2World(gravity);
	m_world->SetAllowSleeping(doSleep);
	m_world->SetContinuousPhysics(true);

	b2BodyDef groundBodyDef;
	groundBodyDef.position.Set(0, FLOOR_HEIGHT / PTM_RATIO);
	m_groundBody = m_world->CreateBody(&groundBodyDef);

	// bottom
	b2EdgeShape groundBox;

	groundBox.Set(b2Vec2(0, FLOOR_HEIGHT / PTM_RATIO), b2Vec2(screenSize.width / PTM_RATIO, FLOOR_HEIGHT / PTM_RATIO));
	m_groundBody->CreateFixture(&groundBox, 0);
	// top
	groundBox.Set(b2Vec2(0, (screenSize.height - FLOOR_HEIGHT) / PTM_RATIO),
			b2Vec2(screenSize.width / PTM_RATIO, (screenSize.height - FLOOR_HEIGHT) / PTM_RATIO));
	m_groundBody->CreateFixture(&groundBox, 0);
	// left
	groundBox.Set(b2Vec2(0, (screenSize.height - FLOOR_HEIGHT) / PTM_RATIO), b2Vec2(0, FLOOR_HEIGHT));
	m_groundBody->CreateFixture(&groundBox, 0);
	// right
	groundBox.Set(b2Vec2(screenSize.width / PTM_RATIO, (screenSize.height - FLOOR_HEIGHT) / PTM_RATIO),
			b2Vec2(screenSize.width / PTM_RATIO, FLOOR_HEIGHT));
	m_groundBody->CreateFixture(&groundBox, 0);

	scheduleUpdate();

	return true;
}

void HelloWorld::createNest() {
	if (nestArray == NULL) {
		nestArray = CCArray::create();
		CC_SAFE_RETAIN(nestArray);
	}

	CCSize size = CCDirector::sharedDirector()->getWinSize();

	CCSprite* nest0 = CCSprite::create("nest.png");
	nest0->setPosition(ccp(size.width/2,20.0f));
	addChild(nest0);
	nestArray->addObject(nest0);
	followNest = nest0;

	CCSprite* nest = CCSprite::create("nest.png");
	nest->setPosition(ccp(size.width,size.height/3));
	addChild(nest);
	nestArray->addObject(nest);
	nest->runAction(createNestAction(nest));

	CCSprite* nest1 = CCSprite::create("nest.png");
	nest1->setPosition(ccp(0,size.height*2/3));
	addChild(nest1);
	nestArray->addObject(nest1);
	nest1->runAction(createNestAction(nest1));

}

CCActionInterval* HelloWorld::createNestAction(CCSprite* nest) {
	CCSize size = CCDirector::sharedDirector()->getWinSize();
	CCPoint position = nest->getPosition();
	float speed = nest_min_move_speed + CCRANDOM_0_1() * 100;
	bool isLeft = position.x != 0;
	CCPoint endPoint1;
	CCPoint endPoint2;

	float dt1;
	float dt2;
	dt2 = size.width / speed;
	if (isLeft) {
		endPoint1 = ccp(0,position.y);
		endPoint2 = ccp(size.width,position.y);
		dt1 = position.x / speed;
	} else {
		endPoint1 = ccp(size.width,position.y);
		endPoint2 = ccp(0,position.y);
		dt1 = (size.width - position.x) / speed;
	}
	//	CCActionInterval* move1 = CCMoveTo::create(dt1, endPoint1);
	CCActionInterval* move2 = CCMoveTo::create(dt2, endPoint2);
	CCActionInterval* move3 = CCMoveTo::create(dt2, endPoint1);
	CCActionInterval* moveRepeat = CCRepeatForever::create(CCSequence::create(move3, move2, NULL));
	return moveRepeat;//CCSequence::create(move1,moveRepeat,NULL);
}

CCPoint preLocation = CCPointZero;

void HelloWorld::update(float dt) {
	int velocityIterations = 8;
	int positionIterations = 1;
	m_world->Step(1.0 / 60, velocityIterations, positionIterations);

	for (b2Body* b = m_world->GetBodyList(); b; b = b->GetNext()) {
		if (b->GetUserData() != NULL) {
			//Synchronize the AtlasSprites position and rotation with the corresponding body
			CCSprite* myActor = (CCSprite*) b->GetUserData();

			myActor->setPosition(CCPointMake( b->GetPosition().x * PTM_RATIO, b->GetPosition().y * PTM_RATIO));
			myActor->setRotation(-1 * CC_RADIANS_TO_DEGREES(b->GetAngle()));
			isJumpEggDown = preLocation.y > myActor->getPosition().y;
		}
	}

	isJumpEggDown = preLocation.y > jumpEgg->getPosition().y;
	preLocation = jumpEgg->getPosition();

	if (isJumpEggDown && jumpState != -1) {
		CCSprite* nest = m_nestLayer->catchEgg(jumpEgg);
		if (nest!=NULL && nest->getPositionY() > followNest->getPositionY()) {
			if (m_armBody != NULL) {
				m_armBody->DestroyFixture(m_armFixture);
				m_world->DestroyBody(m_armBody);
				m_armFixture = NULL;
				m_armBody = NULL;
			}
			followNest = nest;
			score += 10;
			jumpState = -1;
			CCPoint worldPoint = m_nestLayer->convertToWorldSpace(followNest->getPosition());
			m_nestLayer->updateNestPositon(worldPoint);

		}
	} else if (jumpState == -1 && followNest != NULL) {
//		m_nestLayer->stopAllActions();
		CCPoint worldPoint = m_nestLayer->convertToWorldSpaceAR(followNest->getPosition());
		jumpEgg->setPosition(worldPoint);
		jumpEgg->setRotation(0.0f);
	}

	if (jumpEgg->getPositionY() <= FLOOR_HEIGHT) {
		if (m_armBody != NULL) {
			m_armBody->DestroyFixture(m_armFixture);
			m_world->DestroyBody(m_armBody);
			m_armFixture = NULL;
			m_armBody = NULL;
		}
		//		CCDirector::sharedDirector()->replaceScene(HelloWorld::scene());
		CCPoint worldPoint = m_nestLayer->convertToWorldSpaceAR(followNest->getPosition());
		jumpEgg->setPosition(worldPoint);
		jumpEgg->setRotation(0.0f);
		jumpState = -1;
	}

	char* scoreStr = new char[10];
	sprintf(scoreStr, "s:%d", score);
	scoreLable->setString(scoreStr);
}

void HelloWorld::draw() {

	CCLayer::draw();

	ccGLEnableVertexAttribs(kCCVertexAttribFlag_Position);

	kmGLPushMatrix();

	m_world->DrawDebugData();

	kmGLPopMatrix();

}

void HelloWorld::ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void HelloWorld::ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void HelloWorld::ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {

	if (jumpState == 0) {
		return;
	}

	if (m_armBody == NULL) {
		CCPoint worldPosition = jumpEgg->getParent()->convertToWorldSpace(jumpEgg->getPosition());
		b2FixtureDef armBoxDef;
		b2BodyDef armBodyDef;
		armBodyDef.type = b2_dynamicBody;
		armBodyDef.linearDamping = 0;
		armBodyDef.angularDamping = 0;

		armBodyDef.position.Set(worldPosition.x / PTM_RATIO, worldPosition.y / PTM_RATIO);
		armBodyDef.userData = jumpEgg;
		//	b2PolygonShape armBox;
		b2CircleShape armCircle;
		CCSize jumpEggContent = jumpEgg->getContentSize();
		armCircle.m_radius = jumpEggContent.width / 2 / PTM_RATIO;
		armBoxDef.shape = &armCircle;
		armBoxDef.density = 10.0F;
		m_armBody = m_world->CreateBody(&armBodyDef);
		m_armFixture = m_armBody->CreateFixture(&armBoxDef);
	}

	b2Vec2 vel = m_armBody->GetLinearVelocity();
	float m = m_armBody->GetMass();// the mass of the body
	b2Vec2 desiredVel = b2Vec2(0, b2Sqrt(2.0f*10*350/PTM_RATIO));// the vector speed you set
	b2Vec2 velChange = desiredVel - vel;
	b2Vec2 impluse = m * velChange; //impluse = mv
	m_armBody->ApplyLinearImpulse(impluse, m_armBody->GetWorldCenter());
	//	m_armBody->ApplyTorque(100);
	jumpState = 0;
}

void HelloWorld::menuCloseCallback(CCObject* pSender) {
	CCDirector::sharedDirector()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	exit(0);
#endif
}

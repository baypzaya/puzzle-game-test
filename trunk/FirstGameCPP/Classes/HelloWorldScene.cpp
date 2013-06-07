#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

#define PTM_RATIO 32.0f
#define FLOOR_HEIGHT    0.0f

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

// on "init" you need to initialize your instance
bool HelloWorld::init() {
	if (!CCLayer::init()) {
		return false;
	}
	setTouchEnabled(true);
	isJumpEggDown = false;
	m_mouseJoint = NULL;
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();

	//add close menu item
	CCMenuItemImage *pCloseItem = CCMenuItemImage::create("CloseNormal.png", "CloseSelected.png", this,
			menu_selector(HelloWorld::menuCloseCallback));
	pCloseItem->setPosition(ccp(CCDirector::sharedDirector()->getWinSize().width - 20, 20));
	CCMenu* pMenu = CCMenu::create(pCloseItem, NULL);
	pMenu->setPosition(CCPointZero);
	this->addChild(pMenu, 1);

	//add jump egg
	jumpEgg = CCSprite::create("jump_egg.png");
	//	jumpEgg->setAnchorPoint(CCPointZero);
	addChild(jumpEgg);



	createNest();
	
	b2Vec2 gravity;
	gravity.Set(0.0f, -10.0f);
	bool doSleep = true;
	m_world = new b2World(gravity);
	m_world->SetAllowSleeping(doSleep);
	m_world->SetContinuousPhysics(true);

	CCLog("win width:%.2f height:%.2f", screenSize.width, screenSize.height);
	b2BodyDef groundBodyDef;
	groundBodyDef.position.Set(0, 0);
	m_groundBody = m_world->CreateBody(&groundBodyDef);

	// bottom
	b2EdgeShape groundBox;

	groundBox.Set(b2Vec2(0, FLOOR_HEIGHT / PTM_RATIO), b2Vec2(screenSize.width / PTM_RATIO, FLOOR_HEIGHT / PTM_RATIO));
	m_groundBody->CreateFixture(&groundBox, 0);
	// top
	groundBox.Set(b2Vec2(0, screenSize.height / PTM_RATIO),
			b2Vec2(screenSize.width / PTM_RATIO, screenSize.height / PTM_RATIO));
	m_groundBody->CreateFixture(&groundBox, 0);
	// left
	groundBox.Set(b2Vec2(0, screenSize.height / PTM_RATIO), b2Vec2(0, 0));
	m_groundBody->CreateFixture(&groundBox, 0);
	// right
	groundBox.Set(b2Vec2(screenSize.width / PTM_RATIO, screenSize.height / PTM_RATIO),
			b2Vec2(screenSize.width / PTM_RATIO, 0));
	m_groundBody->CreateFixture(&groundBox, 0);

	scheduleUpdate();

	return true;
}

void HelloWorld::createNest() {
	CCSize size = CCDirector::sharedDirector()->getWinSize();
	m_currentNest = CCSprite::create("nest.png");
	m_currentNest->setPosition(ccp(size.width/2,size.height/2));
	addChild(m_currentNest);
	CCMoveTo::CCActionInterval* move1 = CCMoveTo::create(10.0f, ccp(size.width,size.height/2));
	CCActionInterval* move2 = CCMoveTo::create(10.0f, ccp(0,size.height/2));
	CCActionInterval* moveRepeat = CCRepeatForever::create(CCSequence::create(move1, move2, NULL));
	m_currentNest->runAction(moveRepeat);

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


	if (isJumpEggDown && m_currentNest != NULL && m_mouseJoint == NULL) {
		CCRect nestBound = m_currentNest->boundingBox();
		bool isContain = nestBound.containsPoint(jumpEgg->getPosition());
		if (isContain) {
			if (m_armBody != NULL) {
				m_armBody->DestroyFixture(m_armFixture);
				m_world->DestroyBody(m_armBody);
				m_armFixture = NULL;
				m_armBody = NULL;
			}
			removeChild(jumpEgg, true);
			jumpEgg = CCSprite::create("jump_egg.png");
			jumpEgg->setPosition(ccp(m_currentNest->getContentSize().width/2,m_currentNest->getContentSize().height/2));
			m_currentNest->addChild(jumpEgg);
			//			jumpEgg->setPosition(ccp(0,));
		}
	}

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

	if (m_mouseJoint != NULL) {
		m_world->DestroyJoint(m_mouseJoint);
		m_mouseJoint = NULL;
	}

	if (m_armBody == NULL) {
		CCPoint worldPosition = jumpEgg->getParent()->convertToWorldSpace(jumpEgg->getPosition());
		removeChild(jumpEgg, true);
		m_currentNest->removeChild(jumpEgg, true);
		jumpEgg = CCSprite::create("jump_egg.png");
		jumpEgg->setPosition(worldPosition);
		addChild(jumpEgg);

		b2FixtureDef armBoxDef;
		b2BodyDef armBodyDef;
		armBodyDef.type = b2_dynamicBody;
		armBodyDef.linearDamping = 1;
		armBodyDef.angularDamping = 1;

		armBodyDef.position.Set(worldPosition.x / PTM_RATIO, worldPosition.y / PTM_RATIO);
		armBodyDef.userData = jumpEgg;
		//	b2PolygonShape armBox;
		b2CircleShape armCircle;
		CCSize jumpEggContent = jumpEgg->getContentSize();
		armCircle.m_radius = jumpEggContent.width / 2 / PTM_RATIO;
		armBoxDef.shape = &armCircle;
		armBoxDef.density = 0.5F;
		m_armBody = m_world->CreateBody(&armBodyDef);
		m_armFixture = m_armBody->CreateFixture(&armBoxDef);

	}

	b2Vec2 vel = m_armBody->GetLinearVelocity();
	float m = m_armBody->GetMass();// the mass of the body
	b2Vec2 desiredVel = b2Vec2(0, 32);// the vector speed you set
	b2Vec2 velChange = desiredVel - vel;
	b2Vec2 impluse = m * velChange; //impluse = mv
	m_armBody->ApplyLinearImpulse(impluse, m_armBody->GetWorldCenter());
	//	m_armBody->ApplyTorque(100);
}

void HelloWorld::menuCloseCallback(CCObject* pSender) {
	CCDirector::sharedDirector()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	exit(0);
#endif
}

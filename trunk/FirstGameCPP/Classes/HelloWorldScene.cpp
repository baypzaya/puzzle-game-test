#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

using namespace cocos2d;
//using namespace CocosDenshion;

#define PTM_RATIO       32
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
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();

	//add close menu item
	CCMenuItemImage *pCloseItem = CCMenuItemImage::create("CloseNormal.png", "CloseSelected.png", this,
			menu_selector(HelloWorld::menuCloseCallback));
	pCloseItem->setPosition(ccp(CCDirector::sharedDirector()->getWinSize().width - 20, 20));
	CCMenu* pMenu = CCMenu::create(pCloseItem, NULL);
	pMenu->setPosition(CCPointZero);
	this->addChild(pMenu, 1);

	//add jump egg
	CCSprite *jumpEgg = CCSprite::create("jump_egg.png");
//	jumpEgg->setAnchorPoint(CCPointZero);
	addChild(jumpEgg);

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

	b2BodyDef armBodyDef;
	armBodyDef.type = b2_dynamicBody;
	armBodyDef.linearDamping = 1;
	armBodyDef.angularDamping = 1;
	armBodyDef.position.Set(230.0f / PTM_RATIO, (FLOOR_HEIGHT + 91.0f) / PTM_RATIO);
	armBodyDef.userData = jumpEgg;
	m_armBody = m_world->CreateBody(&armBodyDef);

	b2PolygonShape armBox;
	b2FixtureDef armBoxDef;
	armBoxDef.shape = &armBox;
	armBoxDef.density = 0.3F;
	armBox.SetAsBox(23.0f / PTM_RATIO, 24.0f / PTM_RATIO);
	m_armFixture = m_armBody->CreateFixture(&armBoxDef);

	scheduleUpdate();

	return true;
}

void HelloWorld::update(float dt) {
	int velocityIterations = 8;
	int positionIterations = 1;
	m_world->Step(dt, velocityIterations, positionIterations);

	for (b2Body* b = m_world->GetBodyList(); b; b = b->GetNext()) {
		if (b->GetUserData() != NULL) {
			//Synchronize the AtlasSprites position and rotation with the corresponding body
			CCSprite* myActor = (CCSprite*) b->GetUserData();
			myActor->setPosition(CCPointMake( b->GetPosition().x * PTM_RATIO, b->GetPosition().y * PTM_RATIO));
			myActor->setRotation(-1 * CC_RADIANS_TO_DEGREES(b->GetAngle()));
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
	b2Vec2 vel = m_armBody->GetLinearVelocity();
	float m = m_armBody->GetMass();// the mass of the body
	b2Vec2 desiredVel = b2Vec2(0,20);// the vector speed you set
	b2Vec2 velChange = desiredVel - vel;
	b2Vec2 impluse = m * velChange; //impluse = mv
	m_armBody->ApplyLinearImpulse( impluse, m_armBody->GetWorldCenter() );
	m_armBody->ApplyTorque(100);
}

void HelloWorld::menuCloseCallback(CCObject* pSender) {
	CCDirector::sharedDirector()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	exit(0);
#endif
}

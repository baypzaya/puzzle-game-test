/*
 * JumpActorLayer.cpp
 *
 *  Created on: 2013-6-27
 *      Author: yujsh
 */

#include <JumpActorLayer.h>

JumpActorLayer::~JumpActorLayer() {
}

bool JumpActorLayer::init() {
	setTouchEnabled(true);
	scheduleUpdate();

	//	m_jumpActor = JumpActor::create();
	//	addChild(m_jumpActor);

	m_jumpEgg = CCSprite::create("jump_egg.png");
	m_jumpEgg->setAnchorPoint(ccp(0.5,0));
	m_jumpEgg->setPosition(ccp(screenSize.width/2,20.0f));
	addChild(m_jumpEgg);

	return true;
}

void JumpActorLayer::update(float dt) {

}

void JumpActorLayer::ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void JumpActorLayer::ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void JumpActorLayer::ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
	if (jumpState == 0 || m_nestLayer->getMoving()) {
		return;
	}

	m_jumpEgg->stopAllActions();
	CCJumpBy* jumpBy = CCJumpBy::create(1.5f, ccp(0,-40), 390, 1);
	m_jumpEgg->runAction(jumpBy);
}

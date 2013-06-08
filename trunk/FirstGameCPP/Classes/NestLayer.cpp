/*
 * NestLayer.cpp
 *
 *  Created on: 2013-6-8
 *      Author: yujsh
 */

#include <NestLayer.h>

NestLayer::~NestLayer() {
	CC_SAFE_DELETE_ARRAY(m_nestArray);
}

bool NestLayer::init() {
	//init data
	createNest();

	//init ui
	return true;
}

CCSprite* NestLayer::catchEgg(CCSprite* jumpEgg) {
	CCObject* nestO;
	CCARRAY_FOREACH(m_nestArray,nestO) {
			CCSprite* nest = dynamic_cast<CCSprite*> (nestO);
			CCRect nestBound = nest->boundingBox();
			CCPoint nodePoint = convertToNodeSpace(jumpEgg->getPosition());
			bool isContain = nestBound.containsPoint(nodePoint);
			if (isContain) {
				return nest;
			}
		}
	return NULL;
}
void NestLayer::updateNestPositon(CCPoint position) {
	CCMoveTo* moveTo = CCMoveTo::create(0.5f, ccp(0,getPositionY()-position.y+20));
	CCCallFunc* callBack = CCCallFunc::create(this,callfunc_selector(NestLayer::moveEnd));
	runAction(CCSequence::create(moveTo,callBack,NULL));
}

void NestLayer::moveEnd() {
	CCObject* nestO;

	CCARRAY_FOREACH(m_nestArray,nestO) {
			CCSprite* nest = dynamic_cast<CCSprite*> (nestO);
			CCPoint worldPoint = convertToWorldSpace(nest->getPosition());
			CCLog("nest tag:%d y:%.1f", nest->getTag(), worldPoint.y);
			if (worldPoint.y <= 0) {
				nest->stopAllActions();
				int height = lastNestHeight + nest_step_height;
				nest->setPosition(ccp(0,lastNestHeight+nest_step_height));
				lastNestHeight = height;
				nest->runAction(createNestAction(nest));
			}
		}
}

void NestLayer::createNest() {
	if (m_nestArray == NULL) {
		m_nestArray = CCArray::create();
		CC_SAFE_RETAIN(m_nestArray);
	}

	CCSize size = CCDirector::sharedDirector()->getWinSize();

	CCSprite* nest0 = CCSprite::create("nest.png");
	nest0->setPosition(ccp(size.width/2,20.0f));
	addChild(nest0);
	m_nestArray->addObject(nest0);
	nest0->setTag(0);

	CCSprite* nest = CCSprite::create("nest.png");
	nest->setPosition(ccp(size.width,size.height/3));
	addChild(nest);
	m_nestArray->addObject(nest);
	nest->runAction(createNestAction(nest));
	nest->setTag(1);

	CCSprite* nest1 = CCSprite::create("nest.png");
	nest1->setPosition(ccp(0,size.height*2/3));
	addChild(nest1);
	m_nestArray->addObject(nest1);
	nest1->runAction(createNestAction(nest1));
	lastNestHeight = size.height * 2 / 3;
	nest1->setTag(2);

}

CCActionInterval* NestLayer::createNestAction(CCSprite* nest) {
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

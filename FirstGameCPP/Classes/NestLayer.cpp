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


	//init ui
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	scollerLayer1 = CCLayerColor::create(ccc4(255,255,0,160));
	scollerLayer1->setContentSize(screenSize);
	scollerLayer1->setPosition(0, 0);
	scollerLayer1->setAnchorPoint(CCPointZero);
	addChild(scollerLayer1);

	scollerLayer2 = CCLayerColor::create(ccc4(255,0,255,160));
	scollerLayer2->setContentSize(screenSize);
	scollerLayer2->setPosition(0, screenSize.height);
	scollerLayer2->setAnchorPoint(CCPointZero);
	addChild(scollerLayer2);

	createNest();

	return true;
}

CCSprite* NestLayer::catchEgg(CCSprite* jumpEgg) {
	CCObject* nestO;
	CCARRAY_FOREACH(m_nestArray,nestO) {
			CCSprite* nest = dynamic_cast<CCSprite*> (nestO);
			CCRect nestBound = nest->boundingBox();
			CCPoint nodePoint = nest->getParent()->convertToNodeSpace(jumpEgg->getPosition());
			bool isContain = nestBound.containsPoint(nodePoint);
			if (isContain) {
				return nest;
			}
		}
	return NULL;
}
void NestLayer::updateNestPositon(CCPoint position) {
	CCLog("nest layout y :%.1f", getPositionY());
	float subHeight = getPositionY()-position.y+20;
	lastNestHeight = lastNestHeight + subHeight;

	CCMoveBy* moveTo = CCMoveBy::create(0.5f, ccp(0,subHeight));
	CCCallFunc* callBack = CCCallFunc::create(this, callfunc_selector(NestLayer::moveEnd));
	scollerLayer1->runAction(CCSequence::create(moveTo, callBack, NULL));

	CCMoveBy* moveBy2 = CCMoveBy::create(0.5f, ccp(0,subHeight));
	scollerLayer2->runAction(moveBy2);
}

void NestLayer::moveEnd() {
	CCSize size = CCDirector::sharedDirector()->getWinSize();
	if (scollerLayer1->getPosition().y <= -size.height) {
		scollerLayer1->setPosition(ccp(0,size.height));
	}

	if (scollerLayer2->getPosition().y <= -size.height) {
		scollerLayer2->setPosition(ccp(0,size.height));
	}

	CCObject* nestO;

	CCARRAY_FOREACH(m_nestArray,nestO) {
			CCSprite* nest = dynamic_cast<CCSprite*> (nestO);
			CCPoint worldPoint = nest->getParent()->convertToWorldSpace(nest->getPosition());
			if (worldPoint.y <= 0) {
				nest->getParent()->removeChild(nest,false);
				int height = lastNestHeight + nest_step_height;
				CCPoint nestWPosition = ccp(0,height);
				CCLog("lastNestHeight:%d",lastNestHeight);
				CCRect sl1Bound = scollerLayer1->boundingBox();
				CCRect sl2Bound = scollerLayer2->boundingBox();
				CCLog("sl1Bound t:%.1f,b:%.1f",sl1Bound.getMaxY(),sl1Bound.getMinY());
				CCLog("sl2Bound t:%.1f,b:%.1f",sl2Bound.getMaxY(),sl2Bound.getMinY());
				CCLog("height:%d",height);
				if(sl1Bound.containsPoint(nestWPosition)){
					CCLog("sl1Bound contained");
					scollerLayer1->addChild(nest);
					nest->setPosition(scollerLayer1->convertToNodeSpace(nestWPosition));
				}else if(sl2Bound.containsPoint(nestWPosition)){
					CCLog("sl2Bound contained");
					scollerLayer2->addChild(nest);
					nest->setPosition(scollerLayer2->convertToNodeSpace(nestWPosition));
				}

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
	scollerLayer1->addChild(nest0);
	m_nestArray->addObject(nest0);
	nest0->setTag(0);

	CCSprite* nest = CCSprite::create("nest.png");
	nest->setPosition(ccp(size.width,size.height/3+20));
	scollerLayer1->addChild(nest);
	m_nestArray->addObject(nest);
	nest->runAction(createNestAction(nest));
	nest->setTag(1);

	CCSprite* nest1 = CCSprite::create("nest.png");
	nest1->setPosition(ccp(0,size.height*2/3+20));
	scollerLayer1->addChild(nest1);
	m_nestArray->addObject(nest1);
	nest1->runAction(createNestAction(nest1));
	lastNestHeight = size.height * 2 / 3+20;
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

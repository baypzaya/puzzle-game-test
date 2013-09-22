/*
 * NestLayer.cpp
 *
 *  Created on: 2013-6-8
 *      Author: yujsh
 */

#include <NestLayer.h>

NestLayer::~NestLayer() {
	CCLog("~NestLayer");
	CC_SAFE_DELETE(m_nestArray);
}

bool NestLayer::init() {
	//init data
	CCLog("NestLayer initData");
	//init ui
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	scollerLayer1 = CCLayerColor::create(ccc4(0, 0, 0, 0));
	scollerLayer1->setContentSize(screenSize);
	scollerLayer1->setPosition(0, 0);
	scollerLayer1->setAnchorPoint(CCPointZero);
	addChild(scollerLayer1);

	scollerLayer2 = CCLayerColor::create(ccc4(0, 0, 0, 0));
	scollerLayer2->setContentSize(screenSize);
	scollerLayer2->setPosition(0, screenSize.height);
	scollerLayer2->setAnchorPoint(CCPointZero);
	addChild(scollerLayer2);

	createNest();

	return true;
}

CCSprite* NestLayer::catchEgg(CCSprite* jumpEgg) {
	CCObject* nestO;
	CCPoint worldPoint = jumpEgg->getParent()->convertToWorldSpace(jumpEgg->getPosition());
	CCSize eggContentSize = jumpEgg->getContentSize();
	CCPoint nodePoint = convertToNodeSpace(worldPoint);
	nodePoint = ccp(nodePoint.x,nodePoint.y-eggContentSize.height/2);

	CCARRAY_FOREACH(m_nestArray,nestO) {
			CCSprite* nest = dynamic_cast<CCSprite*> (nestO);
			CCRect nestBound = nest->boundingBox();

			float catchWidth = nestBound.size.width * 0.6f;
			float catchHeight = 10;//nestBound.size.height * 0.6f;
			float catchRectX = nestBound.getMinX() + (nestBound.size.width - catchWidth) / 2;
			float catchRectY = nestBound.getMinY() + (nestBound.size.height - catchHeight) / 2;
			CCRect catchRect = CCRectMake(catchRectX,catchRectY,catchWidth,catchHeight);
			CCPoint nodePoint = nest->getParent()->convertToNodeSpace(worldPoint);
			nodePoint = ccp(nodePoint.x,nodePoint.y-eggContentSize.height/2);
			bool isContain = catchRect.containsPoint(nodePoint);
			if (isContain) {
				return nest;
			}
		}
	return NULL;
}
void NestLayer::updateNestPositon(CCPoint position) {
	GameData* gameData = GameData::getInstance();
	float subHeight = getPositionY() - position.y + GameData::nest_base_height;
	float lastHeight = gameData->getLastNestHeight() + subHeight;
	gameData->setLastNestHeight(lastHeight);

	CCMoveBy* moveTo = CCMoveBy::create(0.5, ccp(0,subHeight));
	CCCallFunc* callBack = CCCallFunc::create(this, callfunc_selector(NestLayer::moveEnd));
	scollerLayer1->runAction(CCSequence::create(moveTo, callBack, NULL));

	CCMoveBy* moveBy2 = CCMoveBy::create(0.5, ccp(0,subHeight));
	scollerLayer2->runAction(moveBy2);
	m_isMoving = true;
}

void NestLayer::moveEnd() {
	m_isMoving = false;
	CCObject* nestO;

	CCARRAY_FOREACH(m_nestArray,nestO) {
			CCSprite* nest = dynamic_cast<CCSprite*> (nestO);
			CCPoint worldPoint = nest->getParent()->convertToWorldSpace(nest->getPosition());
			if (worldPoint.y <= 0) {
				nest->stopAllActions();
				nest->getParent()->removeChild(nest, false);

				NormalNest* normalNest = dynamic_cast<NormalNest*> (nest->getUserObject());
				normalNest->runAction();
				addToLayer(nest);
			}
		}

	CCSize size = CCDirector::sharedDirector()->getWinSize();
	if (convertToWorldSpace(scollerLayer1->getPosition()).y <= -size.height + GameData::nest_base_height) {
		scollerLayer1->setPosition(ccp(0,size.height));
	}

	if (convertToWorldSpace(scollerLayer2->getPosition()).y <= -size.height + GameData::nest_base_height) {
		scollerLayer2->setPosition(ccp(0,size.height));
	}
}

void NestLayer::createNest() {
	if (m_nestArray == NULL) {
		m_nestArray = CCArray::create();
		CC_SAFE_RETAIN(m_nestArray);
	}

	GameData* gameData = GameData::getInstance();
	CCSize size = CCDirector::sharedDirector()->getWinSize();
	int nestCount = (size.height - GameData::nest_base_height) / GameData::nest_step_height + 1;

	for (int i = 0; i < nestCount; i++) {
		NormalNest* nest = NormalNest::create();
		nest->runAction();
		CCSprite* nestSprite = nest->getNestSprite();
		nestSprite->setUserObject(nest);
		m_nestArray->addObject(nestSprite);
		addToLayer(nestSprite);
	}

}

void NestLayer::addToLayer(CCSprite* nestSprite) {

	if (nestSprite) {

		if (nestSprite->getParent()) {
			nestSprite->getParent()->removeChild(nestSprite, false);
		}

		CCPoint nestWPosition = nestSprite->getPosition();
		CCRect sl1Bound = scollerLayer1->boundingBox();
		CCRect sl2Bound = scollerLayer2->boundingBox();
		if (sl1Bound.containsPoint(nestWPosition)) {
			scollerLayer1->addChild(nestSprite);
			nestSprite->setPosition(scollerLayer1->convertToNodeSpace(nestWPosition));
		} else if (sl2Bound.containsPoint(nestWPosition)) {
			scollerLayer2->addChild(nestSprite);
			nestSprite->setPosition(scollerLayer2->convertToNodeSpace(nestWPosition));
		}
	}
}

CCSprite* NestLayer::getBaseNest() {
	CCSprite* baseNest = dynamic_cast<CCSprite*> (getChildByTag(0));
	return baseNest;
}


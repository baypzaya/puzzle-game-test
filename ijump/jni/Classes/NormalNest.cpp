#include "NormalNest.h"

NormalNest::~NormalNest() {
	CC_SAFE_RELEASE_NULL(m_nestSprite);
}

bool NormalNest::init() {
	CCSprite* nestSprite = CCSprite::create("nest.png");
	setNestSprite(nestSprite);
}

void NormalNest::runAction() {
	m_nestSprite->stopAllActions();
	m_nestData = GameData::getInstance()->createNest();
	m_nestSprite->setPosition(m_nestData.location);
	m_nestSprite->setScaleX(m_nestData.width / m_nestSprite->getContentSize().width);
	m_nestSprite->setTag(m_nestData.type);
	if (m_nestData.speed > 0) {
		m_nestSprite->runAction(createCircleAction());
	}
}

bool NormalNest::catchEgg() {
	;
}

void NormalNest::firstActionEnd() {
	;
}

CCActionInterval* NormalNest::createFirstAction() {
	;
}

CCActionInterval* NormalNest::createCircleAction() {
	CCSize size = CCDirector::sharedDirector()->getWinSize();
	float speed = m_nestData.speed;
	CCPoint endPoint;

	float dt2 = size.width / speed;
	switch (m_nestData.direction) {
	case left:
		endPoint = ccp(-size.width,0);
		break;
	case right:
		endPoint = ccp(size.width,0);
		break;
	default:
		endPoint = ccp(0,0);
	}

	CCActionInterval* move1 = CCMoveBy::create(dt2, endPoint);
	CCActionInterval* move2 = move1->reverse();
	CCActionInterval* moveRepeat = CCRepeatForever::create(CCSequence::create(move1, move2, NULL));
	return moveRepeat;
}

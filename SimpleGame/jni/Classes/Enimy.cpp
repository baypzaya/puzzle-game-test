#include "GameData.h"
#include "Enimy.h"
#include "cocos2d.h"
using namespace cocos2d;

Enimy::Enimy() {
	init();
	enimySprite->retain();
}

Enimy::~Enimy() {
	enimySprite->release();
}

void Enimy::init() {

	mGameData = GameData::getInstance();
	life = 100;
	speed = 100;
	destroy = 10;
	cost = 10;

	cc_timeval psv;
	CCTime::gettimeofdayCocos2d(&psv, NULL);
	unsigned long int seed = psv.tv_sec * 1000 + psv.tv_usec / 1000;
	srand(seed);

	int randNumber = CCRANDOM_0_1() * 6 + 0;

	CCPoint initPoint = mGameData->getCellPointByIndex(GameData::cellRow - 1,
			randNumber);
	CCPoint endPoint = mGameData->getCellPointByIndex(0, randNumber);

	enimySprite = CCSprite::create("enemy1.png", CCRectMake(0,0,40,55));
	enimySprite->setPosition(initPoint);

	CCAnimate *pRunDouga =
			CCAnimate::create(
					CCAnimationCache::sharedAnimationCache()->animationByName(
							"downRun"));
	enimySprite->runAction(CCRepeatForever::create(pRunDouga));

	float length;
	if (enimySprite->getPositionX() == endPoint.x) {
		length = endPoint.y - enimySprite->getPositionY();
	} else {
		length = endPoint.x - enimySprite->getPositionX();
	}

	length = length > 0 ? length : -length;
	float dt = length / speed;

	CCFiniteTimeAction* actionMove = CCMoveTo::create((float) dt, endPoint);
	CCFiniteTimeAction* actionMoveDone = CCCallFuncN::create(this,
			callfuncN_selector(Enimy::spriteMoveFinished));
	CCFiniteTimeAction* targetMoveAction = CCSequence::create(actionMove,
			actionMoveDone, NULL);
	targetMoveAction->setTag(2);

	enimySprite->runAction(targetMoveAction);
}

void Enimy::spriteMoveFinished(CCNode* sender) {

//	int currentPointIndex = sender->getTag();
//	sender->stopActionByTag(2);
//	currentPointIndex++;
//	int length = mGameData->getEnimyPath()->count();
//	if (currentPointIndex < length) {
//		moveToNextPoint(currentPointIndex);
//	} else {
		mGameData->power = mGameData->power - destroy;
		sender->getParent()->removeChild(sender, true);
		mGameData->removeEnimy(this);
//	}
}

void Enimy::moveToNextPoint(int nextIndex) {
	CCPoint endPoint = mGameData->getEnimyPath()->getControlPointAtIndex(
			nextIndex);
	float length;
	if (enimySprite->getPositionX() == endPoint.x) {
		length = endPoint.y - enimySprite->getPositionY();
	} else {
		length = endPoint.x - enimySprite->getPositionX();
	}

	length = length > 0 ? length : -length;
	float dt = length / speed;

	CCFiniteTimeAction* actionMove = CCMoveTo::create((float) dt, endPoint);
	CCFiniteTimeAction* actionMoveDone = CCCallFuncN::create(this,
			callfuncN_selector(Enimy::spriteMoveFinished));
	CCFiniteTimeAction* targetMoveAction = CCSequence::create(actionMove,
			actionMoveDone, NULL);
	targetMoveAction->setTag(2);

	enimySprite->runAction(targetMoveAction);
	enimySprite->setTag(nextIndex);
}

void Enimy::addToScenne(CCLayer* layer) {
	layer->addChild(enimySprite);
}

CCPoint Enimy::getCurrentLocation() {
	CCPoint position = enimySprite->getPosition();
	return position;
}

void Enimy::subLife(int power) {
	if (life > 0) {
		life = life - power;
	}
	//	if (life <= 0 && enimySprite->getTag()!=5) {
	//		enimySprite->stopAllActions();
	//		enimySprite->getParent()->removeChild(enimySprite, false);
	//		mGameData->removeEnimy(this);
	//		enimySprite->setTag(5);
	//	}
}


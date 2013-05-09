#include "GameData.h"
#include "Enimy.h"
#include "cocos2d.h"
using namespace cocos2d;

Enimy::Enimy() {
	init();
}

Enimy::~Enimy() {
}

void Enimy::init() {

	mGameData = GameData::getInstance();
	life = 100;
	speed = 100;
	destroy = 10;

	CCPoint initPoint = mGameData->getEnimyPath()->getControlPointAtIndex(0);
	CCPoint nextPoint = mGameData->getEnimyPath()->getControlPointAtIndex(1);

	enimySprite = CCSprite::create("enemy1.png", CCRectMake(0,0,40,55));//¾«ÁéµÚÒ»Ö¡
	enimySprite->setPosition(initPoint);

	CCAnimate *pRunDouga =
			CCAnimate::actionWithAnimation(
					CCAnimationCache::sharedAnimationCache()->animationByName(
							"downRun"));

	CCFiniteTimeAction* frameAction = CCSequence::create(
			CCRepeatForever::create(pRunDouga), NULL);

	enimySprite->runAction(frameAction);

	moveToNextPoint(1);

	//	mGameData->_enimies->addObject(enimySprite);

}

void Enimy::spriteMoveFinished(CCNode* sender) {

	int currentPointIndex = sender->getTag();
	sender->stopActionByTag(2);
	currentPointIndex++;
	int length = mGameData->getEnimyPath()->count();
	if (currentPointIndex < length) {
		moveToNextPoint(currentPointIndex);
	} else {
		sender->getParent()->removeChild(sender, true);
		mGameData->removeEnimy(this);
	}
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


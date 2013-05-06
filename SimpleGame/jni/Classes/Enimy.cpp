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

	CCLog("init()");
	mGameData = GameData::getInstance();
	life = 100;
	speed = 20;

	CCPoint initPoint = mGameData->_enimiesPath[0];
	CCPoint nextPoint = mGameData->_enimiesPath[1];
	CCLog("init()_1");
	enimySprite = CCSprite::create("enemy1.png", CCRectMake(0,0,40,55));//¾«ÁéµÚÒ»Ö¡
	enimySprite->setPosition(initPoint);

	CCAnimate *pRunDouga =
			CCAnimate::actionWithAnimation(
					CCAnimationCache::sharedAnimationCache()->animationByName(
							"downRun"));

	CCFiniteTimeAction* frameAction = CCSequence::create(
			CCRepeatForever::create(pRunDouga), NULL);

	CCFiniteTimeAction* actionMove = CCMoveTo::create((float) 5, nextPoint);
	CCFiniteTimeAction* actionMoveDone = CCCallFuncN::create(enimySprite,
			callfuncN_selector(Enimy::spriteMoveFinished));

	CCFiniteTimeAction* targetMoveAction = CCSequence::create(actionMove,
			actionMoveDone, NULL);
//	targetMoveAction->setTag(2);
	enimySprite->runAction(frameAction);
	enimySprite->runAction(targetMoveAction);
	CCLog("init()_2");
	enimySprite->setTag(1);

//	mGameData->_enimies->addObject(enimySprite);

}

void Enimy::spriteMoveFinished(CCNode* sender) {
	CCLog("spriteMoveFinished()");

	int currentPointIndex = sender->getTag();
	CCLog("spriteMoveFinished()_1 %d",currentPointIndex);
//	sender->stopActionByTag(2);
	sender->stopAllActions();
	currentPointIndex++;

		mGameData = GameData::getInstance();
		CCPoint endPoint = mGameData->_enimiesPath[currentPointIndex];



	CCFiniteTimeAction* actionMove = CCMoveTo::create((float) 5,endPoint);
	CCLog("spriteMoveFinished()_3");
	CCFiniteTimeAction* actionMoveDone = CCCallFuncN::create(enimySprite,
			callfuncN_selector(Enimy::spriteMoveFinished));
	CCLog("spriteMoveFinished()_4");
	CCFiniteTimeAction* targetMoveAction = CCSequence::create(actionMove,
			actionMoveDone, NULL);
//	targetMoveAction->setTag(2);

	sender->runAction(targetMoveAction);
	sender->setTag(currentPointIndex);
}

void Enimy::addToScenne(CCLayer* layer){
	layer->addChild(enimySprite);
}


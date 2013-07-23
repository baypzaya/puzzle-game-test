/*
 * MainMenuScene.cpp
 *
 *  Created on: 2013-7-19
 *      Author: yujsh
 */

#include <MainMenuScene.h>
#include "MainGameScene.h"

CCScene* MainMenuScene::creatMainMenuScene() {
	MainMenuScene* mainMenuScene = MainMenuScene::create();
	CCScene* scene = CCScene::create();
	scene->addChild(mainMenuScene);
	return scene;
}

MainMenuScene::~MainMenuScene() {

}

bool MainMenuScene::init() {
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();

	CCSprite* background = CCSprite::create("jump_background.png");
	background->setAnchorPoint(CCPointZero);
	background->setPosition(CCPointZero);
	addChild(background);

	CCMenuItemFont* startItem = CCMenuItemFont::create("START", this, menu_selector(MainMenuScene::startGame));
	CCMenuItemFont* quitItem = CCMenuItemFont::create("QUIT", this, menu_selector(MainMenuScene::quitGame));
	startItem->setColor(ccc3(255, 241, 0));
	quitItem->setColor(ccc3(255, 241, 0));
	CCMenu* stateMenu = CCMenu::create(startItem, quitItem, NULL);
	stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/3));
	stateMenu->alignItemsVertically();
	addChild(stateMenu);

	CCLabelTTF* titleLabel = CCLabelTTF::create("Jump Star", "fonts/Abduction.ttf", 70);
	titleLabel->setPosition(ccp(screenSize.width/2,screenSize.height*2/3));
	titleLabel->setColor(ccc3(255, 241, 0));
	addChild(titleLabel, 1);

	CCSprite* star = CCSprite::create("jump_egg.png");
	star->setPosition(
			ccp(titleLabel->getPositionX()+titleLabel->getContentSize().width/2,titleLabel->getPositionY()+titleLabel->getContentSize().height/2));
	addChild(star, 0);
	//
	//	CCSprite* cloud1 = CCSprite::create("nest.png");
	//	cloud1->setPosition(ccp(screenSize.width/3,screenSize.height*2/3-20));
	//	addChild(cloud1);
	//
	//	CCSprite* cloud2 = CCSprite::create("nest.png");
	//	cloud2->setPosition(ccp(screenSize.width*2/3,screenSize.height*2/3-20));
	//	addChild(cloud2);
	//
	//	CCJumpBy* jumpBy = CCJumpBy::create(1.0f, CCPointZero, 100, 1);
	CCRotateBy* rotateBy = CCRotateBy::create(2.0f, 360);
	//
	//	CCSpawn* spawn = CCSpawn::create(jumpBy, rotateBy, NULL);
	//	CCSequence* sequence = CCSequence::create(spawn, CCDelayTime::create(0.2f), NULL);
	//
	star->runAction(CCRepeatForever::create(rotateBy));
}

void MainMenuScene::startGame() {
	MainGameScene* mainGameScene = MainGameScene::create();
	CCDirector::sharedDirector()->replaceScene(mainGameScene);
}

void MainMenuScene::quitGame() {
	CCDirector::sharedDirector()->end();
	#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
		exit(0);
	#endif
}


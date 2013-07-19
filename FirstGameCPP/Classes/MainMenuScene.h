/*
 * MainMenuScene.h
 *
 *  Created on: 2013-7-19
 *      Author: yujsh
 */

#ifndef MAINMENUSCENE_H_
#define MAINMENUSCENE_H_

#include "cocos2d.h"

USING_NS_CC;

class MainMenuScene : public CCLayer{
public:
	CREATE_FUNC(MainMenuScene);
	virtual ~MainMenuScene();
	virtual bool init();
	static CCScene* creatMainMenuScene();

	void startGame();
	void quitGame();

};

#endif /* MAINMENUSCENE_H_ */

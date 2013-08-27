/*
 * MainGameScene.h
 *
 *  Created on: 2013-6-27
 *      Author: yujsh
 */

#ifndef MAINGAMESCENE_H_
#define MAINGAMESCENE_H_

#include "cocos2d.h"
#include "GameLayer.h"
#include "GameStateLayer.h"

USING_NS_CC;

class MainGameScene : public CCScene {
public:
	CREATE_FUNC(MainGameScene);
	virtual bool init();
	virtual void update(float dt);
	virtual ~MainGameScene();
private:
	GameLayer* m_gameLayer;
	GameStateLayer* m_stateLayer;
	GameState m_gameCurrentSate;

	void processGameState();
	void showStateLayer();
	void hideSateLayer();

};

#endif /* MAINGAMESCENE_H_ */

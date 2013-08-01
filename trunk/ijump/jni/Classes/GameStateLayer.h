/*
 * GameStateLayer.h
 *
 *  Created on: 2013-6-13
 *      Author: yujsh
 */

#ifndef GAMESTATELAYER_H_
#define GAMESTATELAYER_H_

#include "cocos2d.h"
#include "GameData.h"

USING_NS_CC;

class GameStateLayer: public cocos2d::CCLayer {
public:

	CREATE_FUNC(GameStateLayer)CC_SYNTHESIZE(int,m_currentGameState,GameState)
	virtual ~GameStateLayer();
	virtual bool init();

	void playGame();
	void restartGame();
	void exitGame();

	virtual void registerWithTouchDispatcher();

	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);

};

#endif /* GAMESTATELAYER_H_ */

/*
 * GameStateLayer.h
 *
 *  Created on: 2013-6-13
 *      Author: yujsh
 */

#ifndef GAMESTATELAYER_H_
#define GAMESTATELAYER_H_

#include "cocos2d.h"

USING_NS_CC;

class GameStateLayer: public cocos2d::CCLayer {
public:

	static const int STATE_PLAY = 1;
	static const int STATE_OVER = 2;
	static const int STATE_PAUSE = 3;

	CREATE_FUNC(GameStateLayer)CC_SYNTHESIZE(int,m_currentGameState,GameState)
	virtual ~GameStateLayer();
	virtual bool init();
	void showGameState();
	void hideGameState();

	void playGame();

	 virtual void registerWithTouchDispatcher();

	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);

};

#endif /* GAMESTATELAYER_H_ */

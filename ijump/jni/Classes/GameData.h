/*
 * GameData.h
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#ifndef GAMEDATA_H_
#define GAMEDATA_H_

#include "cocos2d.h"
#include <string>
USING_NS_CC;

enum GameState {
	STATE_START = 1, STATE_PAUSE, STATE_OVER, STATE_SUCCESS, STATE_RESTART, STATE_END
};

enum NestType {
	lNestType, mNestType, sNestType
};

enum NestRunDirection {
	none, left, right, up, down, r_rotate, l_rotate
};

struct Nest {
	int width;
	int height;
	int speed;
	CCPoint location;
	NestRunDirection direction;
};

class GameData {

public:

	static const int nest_step_height = 320;
	static const int nest_move_speed_scope = 100;
	static const int nest_base_height = 20;

	static const int nest_l1_width = 100;
	static const int nest_l2_width = 80;
	static const int nest_l3_width = 60;
	static const int nest_l4_widht = 40;

	static const int nest_l1_speed = 70;
	static const int nest_l2_speed = 90;
	static const int nest_l3_speed = 120;
	static const int nest_l4_speed = 150;

	CC_SYNTHESIZE(GameState,m_currentGameState,CurrentGameState)
	CC_SYNTHESIZE_READONLY(int,m_score,Score)
	CC_SYNTHESIZE_READONLY(int,m_eggCount,EggCount)
	CC_SYNTHESIZE(float,m_lastNestHeight,LastNestHeight)

	static GameData* getInstance();

	virtual ~GameData();
	virtual void addScore(int score);
	virtual void subEggCount();
	void resetData();
	void saveHighestScore();
	int getHighestScore();

	Nest createNest();
	CCPoint getInitPosition();

private:
	GameData();
	virtual void initData();
	Nest nestPage1[10];
	Nest nestPage2[10];

	int currentPageIndex;
	Nest* currentPage;
};

#endif /* GAMEDATA_H_ */

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

#define nest_l1_width 100
#define nest_l2_width 80
#define nest_l3_width 60
#define nest_l4_widht 40

#define nest_l1_speed 70
#define nest_l2_speed 90
#define nest_l3_speed 120
#define nest_l4_speed 150


USING_NS_CC;

enum GameState {
	STATE_START = 1, STATE_PAUSE, STATE_OVER, STATE_SUCCESS, STATE_RESTART, STATE_END
};

enum NestType {
	normal, slow, strong
};

enum NestRunDirection {
	none, left, right, up, down, r_rotate, l_rotate
};

struct Nest {
	int width;
	int height;
	int speed;
	NestType type;
	CCPoint location;
	NestRunDirection direction;
};

class GameData {

public:

	static const int nest_step_height = 240;
	static const int nest_move_speed_scope = 100;
	static const int nest_base_height = 20;
	static const int egg_jump_max_height = 350;

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

/*
 * GameData.h
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#ifndef GAMEDATA_H_
#define GAMEDATA_H_

#include "cocos2d.h"

USING_NS_CC;

class GameData {

public:
	static const int STATE_START = 0;
	static const int STATE_PAUSE = 1;
	static const int STATE_OVER = 2;
	static const int STATE_SUCCESS = 3;

	CCArray *_enimies;
	CCArray *_bullets;
	CCArray *_towers;
	CCPointArray* enimyPathArray;
	int currentPage;
	int power;
	int money;
	int currentGameState;



	bool hasNextEnimy();
	int getNextEnimyType();
	void removeEnimy(CCObject* enimy);
	void refreshData();
	CCPointArray* getEnimyPath();
	static GameData* getInstance();
	virtual ~GameData();

private:
	GameData();

	virtual void initData();
	float cellWidth;
	float cellHeight;
	bool startNextWave;
	int currentEnimyIndex;
	int countEnimy;
	int countPW;
	int* enimies;
};

#endif /* GAMEDATA_H_ */

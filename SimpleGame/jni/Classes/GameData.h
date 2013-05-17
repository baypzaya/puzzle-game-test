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

	struct EnimyData {
		int lift;
		int speed;
		int cost;
		int destroy;
		char* imagePath;
	};

	struct BulletData {
		int power;
		int speed;
		char* imagepath;
	};

	struct TowerData {
		int scope;
		int speed;
		int cost;
		char* imagepath;
		struct BulletData bullet;
	};

	CCArray *_enimies;
	CCArray *_bullets;
	CCArray *_towers;
	CCPointArray* enimyPathArray;
	int currentPage;
	int power;
	int money;
	int currentGameState;

	CCPoint converToCellPoint(float x, float y);
	bool canLocationTower(float x, float y);
	bool hasNextEnimy();
	int getNextEnimyType();
	void addTower(CCObject* tower, float x, float y);
	void removeEnimy(CCObject* enimy);
	void refreshData();
	CCPointArray* getEnimyPath();
	static GameData* getInstance();
	virtual ~GameData();

	float computeDistance(CCPoint p1, CCPoint p2);

private:
	GameData();

	static const int cellRow = 10;
	static const int cellCol = 6;
	virtual void initData();
	float cellWidth;
	float cellHeight;
	bool startNextWave;
	int currentEnimyIndex;
	int countEnimy;
	int countPW;
	int* enimies;
	int* towerLocation;
};

#endif /* GAMEDATA_H_ */

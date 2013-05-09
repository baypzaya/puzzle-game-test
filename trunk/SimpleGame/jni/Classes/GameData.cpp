/*
 * GameData.cpp
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#include "GameData.h"
#include "cocos2d.h"
USING_NS_CC;

static GameData* sGameData = NULL;

GameData::GameData() {
	_enimies = CCArray::create();
	_enimies->retain();

	_bullets = CCArray::create();
	_bullets->retain();

	enimyPathArray = CCPointArray::create(60);
	enimyPathArray->retain();

	_towers = CCArray::create();
	_towers->retain();

	initData();
}

CCPoint GameData::converToCellPoint(float x, float y) {
	int cellx = (int) x / cellWidth;
	int celly = (int) y / cellHeight;

	float xL = cellx * cellWidth + cellWidth / 2;
	float yL = celly * cellHeight + cellHeight / 2;
	CCPoint ccPoint = ccp(xL,yL);
	return ccPoint;
}

bool GameData::canLocationTower(float x, float y) {
	int cellx = (int) x / cellWidth;
	int celly = (int) y / cellHeight;
	int cellIndex = cellx + celly * cellCol;
	CCLog("towerLocation[cellIndex] %d,%d", cellIndex, towerLocation[cellIndex]);
	return towerLocation[cellIndex] == 1;
}

void GameData::addTower(CCObject* tower, float x, float y) {
	int cellx = (int) x / cellWidth;
	int celly = (int) y / cellHeight;
	int cellIndex = cellx + celly * cellCol;
	towerLocation[cellIndex] = 0;
	_towers->addObject(tower);
}

GameData::~GameData() {
	sGameData = NULL;

	_enimies->release();
	_bullets->release();
	_towers->release();
	enimyPathArray->release();
}

void GameData::refreshData() {
}

bool GameData::hasNextEnimy() {
	if (currentEnimyIndex >= countEnimy) {
		return false;
	}
	bool result = ((currentEnimyIndex % countPW) != 0) || (startNextWave);
	if (startNextWave) {
		startNextWave = false;
	}
	return result;
}

int GameData::getNextEnimyType() {
	if (currentEnimyIndex >= countEnimy) {
		return -1;
	}

	int type = enimies[currentEnimyIndex];
	currentEnimyIndex++;
	return type;
}

GameData* GameData::getInstance() {
	if (sGameData == NULL) {
		sGameData = new GameData();
	}

	return sGameData;
}

void GameData::initData() {

	int enimyRunPath[] = { 3, 9, 3, 5, 2, 5, 2, 0 };
	int enimyWave[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	int towerL[] = { 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1,
			1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1,
			1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1 };
	enimies = enimyWave;

	int towerLL = cellRow * cellCol;
	towerLocation = new int[towerLL];

	//	int towerLL = (sizeof towerL) / (sizeof towerL[0]);
	for (int i = 0; i < towerLL; i++) {
		towerLocation[i] = towerL[i];
	}

	countEnimy = (sizeof enimyWave) / (sizeof enimyWave[0]);
	countPW = 10;
	currentPage = 1;
	power = 100;
	currentEnimyIndex = 0;
	currentGameState = STATE_START;
	startNextWave = true;

	CCSize winSize = CCDirector::sharedDirector()->getVisibleSize();
	cellWidth = winSize.width / cellCol;
	cellHeight = winSize.height / cellRow;
	int length = (sizeof enimyRunPath) / (sizeof enimyRunPath[0]);
	for (int i = 0; i < length; i++) {
		float pathX = cellWidth * enimyRunPath[i] + cellWidth / 2;
		float pathY = cellHeight * enimyRunPath[++i] + cellHeight / 2;
		CCPoint ccPoint = ccp(pathX,pathY);
		enimyPathArray->addControlPoint(ccPoint);
	}
}
CCPointArray* GameData::getEnimyPath() {
	return enimyPathArray;
}

void GameData::removeEnimy(CCObject* enimy) {
	_enimies->removeObject(enimy, true);
	if (_enimies->count() <= 0) {
		if (currentEnimyIndex >= countEnimy) {
			currentGameState = GameData::STATE_SUCCESS;
		} else {
			startNextWave = true;
		}
	}
}

float GameData::computeDistance(CCPoint p1, CCPoint p2) {

	float xL = p1.x - p2.x;
	float yL = p1.y - p2.y;

	float l = sqrtf((xL * xL) + (yL * yL));
	return l;
}

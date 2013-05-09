/*
 * GameData.cpp
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#include "GameData.h"
#include "cocos2d.h"

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
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	enimies = enimyWave;
	countEnimy = (sizeof enimyWave) / (sizeof enimyWave[0]);
	countPW = 10;
	currentPage = 1;
	power = 100;
	currentEnimyIndex = 0;
	startNextWave = true;

	CCSize winSize = CCDirector::sharedDirector()->getVisibleSize();
	float cellWidth = winSize.width / 6;
	float cellHeight = winSize.height / 10;
	int length = (sizeof enimyRunPath) / (sizeof enimyRunPath[0]);
	for (int i = 0; i < length; i++) {
		float pathX = cellWidth * enimyRunPath[i];
		float pathY = cellHeight * enimyRunPath[++i];
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

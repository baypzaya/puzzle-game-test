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
	initData();
}

GameData::~GameData() {
	sGameData = NULL;
}

void refreshData() {
}


GameData* GameData::getInstance() {
	if(sGameData == NULL){
		sGameData = new GameData();
	}

	return sGameData;
}

void GameData::initData() {
	CCSize winSize = CCDirector::sharedDirector()->getVisibleSize();
		float cellWidth = winSize.width / 6;
		float cellHeight = winSize.height / 10;
		int path[] = { 3, 9, 3,5, 2,5, 2,0 };
		int length = sizeof path;

		//	_enimyPath = CCPointArray::arrayWithCapacity(length / 2);
		_enimiesPath = new CCPoint[length / 2];
		for (int i = 0; i < length; i++) {
			float pathX = cellWidth * path[i];
			float pathY = cellHeight * path[++i];
			//		_enimyPath->addControlPoint(ccp(pathX,pathY));
			_enimiesPath[i / 2] = ccp(pathX,pathY);
		}
}

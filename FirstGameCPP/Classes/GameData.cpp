/*
 * GameData.cpp
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#include "GameData.h"
#include "ccMacros.h"

static GameData* sGameData = NULL;

GameData::GameData() {
	initData();
}

GameData::~GameData() {
	sGameData = NULL;
}

GameData* GameData::getInstance() {

	if (sGameData == NULL) {
		sGameData = new GameData();
	}

	return sGameData;
}

void GameData::initData() {
	resetData();
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	//init nestpage1
	for (int i = 0; i < 5; i++) {
		int lastHeight = nest_base_height + nest_step_height * (i + 1);
		nestPage1[i].width = 64;
		nestPage1[i].height = 16;
		nestPage1[i].direction = i % 2 == 0 ? right : left;
		nestPage1[i].speed = nest_min_move_speed;
		if (nestPage1[i].direction == right) {
			nestPage1[i].location = ccp(0,lastHeight);
		} else {
			nestPage1[i].location = ccp(screenSize.width,lastHeight);
		}
	}

	nestPage1[5].width = 64;
	nestPage1[5].height = 16;
	nestPage1[5].direction = none;
	int lastHeight = nest_base_height + nest_step_height * 6;
	nestPage1[5].location = ccp(screenSize.width/2,lastHeight);

	for (int i = 6; i < 10; i++) {
		int lastHeight = nest_base_height + nest_step_height * (i + 1);
		nestPage1[i].width = 64;
		nestPage1[i].height = 16;
		nestPage1[i].direction = i % 2 == 0 ? right : left;
		nestPage1[i].speed = nest_min_move_speed;
		if (nestPage1[i].direction == right) {
			nestPage1[i].location = ccp(0,lastHeight);
		} else {
			nestPage1[i].location = ccp(screenSize.width,lastHeight);
		}
	}

	//init nestpage2
	for (int i = 0; i < 10; i++) {
		int lastHeight = nest_base_height + nest_step_height * (i + 1);
		nestPage2[i].width = 64;
		nestPage2[i].height = 16;
		nestPage2[i].direction = i % 2 == 0 ? right : left;
		nestPage2[i].speed = nest_min_move_speed;
		if (nestPage2[i].direction == right) {
			nestPage2[i].location = ccp(0,lastHeight);
		} else {
			nestPage2[i].location = ccp(screenSize.width,lastHeight);
		}
	}

	currentPage = nestPage1;

}

void GameData::resetData() {
	m_score = 0;
	m_eggCount = 5;
	m_currentGameState = STATE_PAUSE;
	m_lastNestHeight = nest_base_height;
}

void GameData::addScore(int score) {
	m_score += score;
}
void GameData::subEggCount() {
	m_eggCount--;

	if (m_eggCount < 0) {
		m_eggCount = 0;
	}
}

Nest GameData::createNest() {
	if (currentPageIndex >= 10) {
		int pageNum = (int) CCRANDOM_0_1() * 2 + 1;
		CCLog("pageNum:%d", pageNum);
		switch (pageNum) {
		case 1:
			currentPage = nestPage1;
		case 2:
			currentPage = nestPage2;
		}
		currentPageIndex = 0;
	}

	m_lastNestHeight = m_lastNestHeight + nest_step_height;
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	Nest nest = *(currentPage+currentPageIndex);


	if (nest.direction == none) {
		nest.location = ccp(screenSize.width/2,m_lastNestHeight);
	} else if (nest.direction == left) {
		nest.location = ccp(screenSize.width,m_lastNestHeight);
	} else {
		nest.location = ccp(0,m_lastNestHeight);
	}

	currentPageIndex++;
	return nest;
}

CCPoint GameData::getInitPosition() {
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	return ccp(screenSize.width/2,nest_base_height);
}


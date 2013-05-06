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
	CCArray *_enimies;
	CCArray *_bullets;
	CCPoint *_enimiesPath;

	void refreshData();
	static GameData* getInstance();

private:
	GameData();
	virtual ~GameData();
	virtual void initData();

};

#endif /* GAMEDATA_H_ */

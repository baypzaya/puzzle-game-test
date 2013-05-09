/*
 * Enimy.h
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#ifndef ENIMY_H_
#define ENIMY_H_

#include "cocos2d.h"
#include "GameData.h"
USING_NS_CC;

class Enimy :public CCObject{
public:
	Enimy();
	static const int TYPE_ENIMY1 = 1;
	static const int TYPE_ENIMY2 = 2;
	void spriteMoveFinished(cocos2d::CCNode* sender);
	void addToScenne(CCLayer* layer);


private:
	~Enimy();
	int life;
	int speed;
	int destroy;
	CCSprite *enimySprite;
	GameData* mGameData;
	void init();
	void moveToNextPoint(int);
};

#endif /* ENIMY_H_ */

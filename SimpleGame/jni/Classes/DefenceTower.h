/*
 * DefenceTower.h
 *
 *  Created on: 2013-5-8
 *      Author: yujsh
 */

#ifndef DEFENCETOWER_H_
#define DEFENCETOWER_H_

#include "cocos2d.h"

USING_NS_CC;

class DefenceTower: public CCObject {
public:
	DefenceTower(float x,float y);
	virtual ~DefenceTower();

	CCSprite* spriteTower;

	bool canFire(CCObject* target);
	void fire(CCObject* target);
	bool hasFireTarget();

private:
	float fireInterval;
	CCRect fireScope;

};

#endif /* DEFENCETOWER_H_ */

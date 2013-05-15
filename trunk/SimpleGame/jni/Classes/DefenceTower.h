/*
 * DefenceTower.h
 *
 *  Created on: 2013-5-8
 *      Author: yujsh
 */

#ifndef DEFENCETOWER_H_
#define DEFENCETOWER_H_

#include "cocos2d.h"
#include "Bullet.h"

USING_NS_CC;

class DefenceTower: public CCObject {
public:
	DefenceTower(float x,float y);
	virtual ~DefenceTower();

	CCSprite* spriteTower;

	bool canFire(CCObject* target);
	Bullet* fire(CCObject *target);
	bool hasFireTarget();
	Bullet* fireCurrentTarget();

	CC_SYNTHESIZE(CCObject*,currentTarget,CurrentTarget);

private:
	long fireTime;// unit second
	long fireInterval;// unit second
	int fireScope;
};

#endif /* DEFENCETOWER_H_ */

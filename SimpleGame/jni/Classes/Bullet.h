/*
 * Bullet.h
 *
 *  Created on: 2013-5-9
 *      Author: yujsh
 */

#ifndef BULLET_H_
#define BULLET_H_

#include "cocos2d.h"

USING_NS_CC;

class Bullet: public CCObject {
public:
	Bullet(float x, float y);
	virtual ~Bullet();

	CCSprite* bulletSprite;
	virtual void flyToTarget(CCObject* target);
	void spriteMoveFinished(cocos2d::CCNode* sender,CCObject* target);

private:
	int speed;
	int power;
};

#endif /* BULLET_H_ */

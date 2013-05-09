/*
 * DefenceTower.cpp
 *
 *  Created on: 2013-5-8
 *      Author: yujsh
 */

#include "DefenceTower.h"
USING_NS_CC;

DefenceTower::DefenceTower(float x, float y) {
	spriteTower = CCSprite::create("tower1.png");
	spriteTower->setPosition(ccp(x,y));


	fireScope = CCRectMake(x - 200/2,y - 200/2,200,200);

}

DefenceTower::~DefenceTower() {
}

bool DefenceTower::canFire(CCObject *target) {
}

void DefenceTower::fire(CCObject *target) {
}

bool DefenceTower::hasFireTarget() {
}


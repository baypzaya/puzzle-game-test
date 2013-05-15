/*
 * DefenceTower.cpp
 *
 *  Created on: 2013-5-8
 *      Author: yujsh
 */

#include "DefenceTower.h"
#include "Enimy.h"
#include "GameData.h"
USING_NS_CC;

DefenceTower::DefenceTower(float x, float y) {
	spriteTower = CCSprite::create("tower1.png");
	spriteTower->setPosition(ccp(x,y));
	fireScope = 300;
	fireInterval = (1);
	fireTime = 0;
	currentTarget = NULL;

}

DefenceTower::~DefenceTower() {
}

bool DefenceTower::canFire(CCObject *target) {
	Enimy *enimy = dynamic_cast<Enimy*> (target);

	if(enimy->life <= 0){
		return false;
	}

	CCPoint enimyLP = enimy->getCurrentLocation();

	CCPoint towerLP = spriteTower->getPosition();

	float xL = towerLP.x - enimyLP.x;
	float yL = towerLP.y - enimyLP.y;

	float l = sqrtf((xL * xL) + (yL * yL));

	bool result = (l <= fireScope);
	return result;
}

Bullet* DefenceTower::fire(CCObject *target) {

	CCPoint towerLP = spriteTower->getPosition();
	Bullet* bullet = new Bullet(towerLP.x, towerLP.y);
	return bullet;
}

Bullet* DefenceTower::fireCurrentTarget() {
	if (currentTarget != NULL) {
		CCLog("fireCurrentTarget");
		return fire(currentTarget);
	}
	return NULL;
}

bool DefenceTower::hasFireTarget() {
	bool result = false;
	if (currentTarget != NULL) {
		result =  canFire(currentTarget);
	}
	if(!result){
		currentTarget = NULL;
	}
	return result;
}


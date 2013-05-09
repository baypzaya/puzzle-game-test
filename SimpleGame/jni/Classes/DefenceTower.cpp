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
	fireInterval = 0.5;
	fireTime = fireInterval;

}

DefenceTower::~DefenceTower() {
}

bool DefenceTower::canFire(CCObject *target) {
	Enimy *enimy = dynamic_cast<Enimy*> (target);
	CCPoint enimyLP = enimy->getCurrentLocation();

	CCPoint towerLP = spriteTower->getPosition();

	float xL = towerLP.x - enimyLP.x;
	float yL = towerLP.y - enimyLP.y;

	float l = sqrtf((xL * xL) + (yL * yL));

	bool result = (l <= fireScope);
	return result;
}

Bullet* DefenceTower::fire(CCObject *target,float dt) {

	fireTime = fireTime + dt;
	if(fireTime >= fireInterval){
		CCLog("fire1 %.2f, %.2f ",fireTime,fireInterval);
		fireTime = fireTime - fireInterval;
		CCLog("fire2 %.2f, %.2f ",fireTime,fireInterval);

		GameData* mGameData = GameData::getInstance();

		CCPoint towerLP = spriteTower->getPosition();
		Bullet* bullet = new Bullet(towerLP.x,towerLP.y);
		return bullet;
	}
	return NULL;
}

bool DefenceTower::hasFireTarget() {
}


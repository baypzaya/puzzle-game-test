/*
 * Bullet.cpp
 *
 *  Created on: 2013-5-9
 *      Author: yujsh
 */

#include "Bullet.h"
#include "Enimy.h"
#include "GameData.h"

Bullet::Bullet(float x, float y) {
	speed = 500;
	power = 30;

	bulletSprite = CCSprite::create("bullet1.png");
	bulletSprite->setPosition(ccp(x,y));
}

Bullet::~Bullet() {
	// TODO Auto-generated destructor stub
}

void Bullet::flyToTarget(CCObject* target) {
	Enimy *enimy = dynamic_cast<Enimy*> (target);
	CCPoint p1 = enimy->getCurrentLocation();
	CCPoint p2 = bulletSprite->getPosition();
	float distance = GameData::getInstance()->computeDistance(p1, p2);
	float dt = distance / speed;

	CCFiniteTimeAction* actionMove = CCMoveTo::create(dt,
			enimy->getCurrentLocation());
	CCFiniteTimeAction* actionMoveDone =
			CCCallFuncND::create(this,
					callfuncND_selector(Bullet::spriteMoveFinished),
					(CCObject*) target);
	CCFiniteTimeAction* targetMoveAction = CCSequence::create(actionMove,
			actionMoveDone, NULL);
	bulletSprite->runAction(targetMoveAction);
}

void Bullet::spriteMoveFinished(cocos2d::CCNode* sender, CCObject* target) {
	sender->stopAllActions();
	sender->getParent()->removeChild(sender, true);

	GameData::getInstance()->_bullets->removeObject(this, true);
	CCLog("spriteMoveFinished");
	if (target) {
		Enimy *enimy = dynamic_cast<Enimy*> (target);
		enimy->subLife(power);
	}
	CCLog("spriteMoveFinished2");
}


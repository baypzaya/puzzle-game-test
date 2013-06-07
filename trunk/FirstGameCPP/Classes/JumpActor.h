/*
 * JumpActor.h
 *
 *  Created on: 2013-6-7
 *      Author: yujsh
 */

#ifndef JUMPACTOR_H_
#define JUMPACTOR_H_

#include "cocos2d.h"

USING_NS_CC;

class JumpActor : public CCNode{
public:
	virtual ~JumpActor();
	CREATE_FUNC(JumpActor);
	CC_SYNTHESIZE_RETAIN(CCSprite*,m_sprite,Sprite);
	virtual bool init();
	void jump();
	bool isUp();
	bool isDown();
	bool isStatic();
	void updateJumpState(float dt);

};

#endif /* JUMPACTOR_H_ */

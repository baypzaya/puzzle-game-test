/*
 * JumpActor.cpp
 *
 *  Created on: 2013-6-7
 *      Author: yujsh
 */

#include <JumpActor.h>

JumpActor::~JumpActor() {
	CC_SAFE_RELEASE_NULL(m_sprite);
}

bool JumpActor::init() {
	CCSprite* jumpSprite = CCSprite::create("jump_egg");
	setSprite(jumpSprite);
	addChild(jumpSprite);
	return true;
}

void JumpActor::jump() {

}

void JumpActor::updateJumpState(float dt) {
}

bool JumpActor::isDown() {
}

bool JumpActor::isUp() {
}

bool JumpActor::isStatic() {
}

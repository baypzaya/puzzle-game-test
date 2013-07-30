/*
 * JumpActorLayer.h
 *
 *  Created on: 2013-6-27
 *      Author: yujsh
 */

#ifndef JUMPACTORLAYER_H_
#define JUMPACTORLAYER_H_

#include "cocos2d.h"
//#include "JumpActor.h"

USING_NS_CC;

class JumpActorLayer: public CCLayer {
public:
	CREATE_FUNC(JumpActorLayer)
	;
	virtual bool init();
	virtual ~JumpActorLayer();

	virtual void update(float dt);
	virtual void ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
private:
//	JumpActor* m_jumpActor;
	CCSprite* m_jumpEgg;

};

#endif /* JUMPACTORLAYER_H_ */

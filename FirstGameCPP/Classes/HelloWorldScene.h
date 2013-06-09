#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "Box2D/Box2D.h"
#include "NestLayer.h"
#include "GLES-Render.h"

USING_NS_CC;

class HelloWorld: public cocos2d::CCLayer {
public:
	// Here's a difference. Method 'init' in cocos2d-x returns bool, instead of returning 'id' in cocos2d-iphone
	virtual bool init();
	virtual ~HelloWorld();

	// there's no 'id' in cpp, so we recommand to return the exactly class pointer
	static cocos2d::CCScene* scene();

	// a selector callback
	void menuCloseCallback(CCObject* pSender);

	GLESDebugDraw* m_debugDraw;

	// implement the "static node()" method manually
	CREATE_FUNC(HelloWorld)
	;
	virtual void draw();
	virtual void update(float);
	virtual void ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event);

private:
	static const int nest_max_move_speed = 150;
	static const int nest_min_move_speed = 50;
	b2World* m_world;
	b2Body* m_groundBody;
	b2Body* m_armBody;
	b2Fixture* m_armFixture;
	b2MouseJoint* m_mouseJoint;

	NestLayer* m_nestLayer;
	CCArray* nestArray;
	CCSprite *jumpEgg;
	CCSprite* followNest;
	CCLabelTTF* scoreLable;
	bool isJumpEggDown;
	int jumpState;
	int score;
//
//	void createNest();
//	CCActionInterval* createNestAction(CCSprite* nest);
};

#endif // __HELLOWORLD_SCENE_H__

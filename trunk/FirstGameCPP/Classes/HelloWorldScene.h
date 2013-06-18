#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "Box2D/Box2D.h"
#include "NestLayer.h"
#include "GameStateLayer.h"

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
	// implement the "static node()" method manually
	CREATE_FUNC(HelloWorld)

	virtual void update(float);
	virtual void ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event);
	virtual void ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event);

private:
	static const int nest_max_move_speed = 150;
	static const int nest_min_move_speed = 50;

	NestLayer* m_nestLayer;
	CCArray* nestArray;
	GameStateLayer* m_stateLayer;
	CCSprite *jumpEgg;
	CCSprite* followNest;
	CCLabelTTF* scoreLable;
	bool isJumpEggDown;
	int jumpState;
	int score;
};

#endif // __HELLOWORLD_SCENE_H__

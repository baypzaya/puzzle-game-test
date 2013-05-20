#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "cocos2d.h"
#include "GameData.h"
#include "SimpleAudioEngine.h"
USING_NS_CC;

class HelloWorld: public cocos2d::CCLayerColor {
public:
	HelloWorld();
	~HelloWorld();

	// Here's a difference. Method 'init' in cocos2d-x returns bool, 
	// instead of returning 'id' in cocos2d-iphone
	virtual bool init();
	// there's no 'id' in cpp, so we recommand to return the exactly class pointer
	static cocos2d::CCScene* scene();

	// a selector callback
	virtual void menuCloseCallback(cocos2d::CCObject* pSender);

	// implement the "static node()" method manually
	CREATE_FUNC(HelloWorld);

	void shoot(float dt);
	void gameLogic(float dt);
	void updateGame(float dt);

	//	void registerWithTouchDispatcher();
	bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	void ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event);

	void keyBackClicked();
	void keyMenuClicked();
	void buildDenfenceTower(CCObject* pSender);

	static CCPoint *enimiesPath;

protected:
	GameData* mGameData ;
	CCMenu *menu;
	void addTarget();
	void createEnimyFrame(int rowNumber, CCSpriteFrame *pRun[]);
	void initResource();
	void showSelectTowerMenu(CCPoint location);
	void hideSelectTowerMenu();
};

#endif  // __HELLOWORLD_SCENE_H__

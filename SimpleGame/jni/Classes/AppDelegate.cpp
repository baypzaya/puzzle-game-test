#include "AppDelegate.h"
#include "HelloWorldScene.h"

USING_NS_CC;

AppDelegate::AppDelegate() {

}

AppDelegate::~AppDelegate() {
}

bool AppDelegate::applicationDidFinishLaunching() {
	// initialize director
	CCDirector *pDirector = CCDirector::sharedDirector();

	pDirector->setOpenGLView(CCEGLView::sharedOpenGLView());

	CCSize screenSize = CCEGLView::sharedOpenGLView()->getFrameSize();
	//    CCSize screenSize = pDirector->getVisibleSize();
	CCSize designSize = CCSizeMake(320, 480);
	CCLog("screenSize4   w:%f h: %f", screenSize.width, screenSize.height);
	if (screenSize.height > 480) {
		CCFileUtils::sharedFileUtils()->setResourceDirectory("hd");
		pDirector->setContentScaleFactor(960.0f / screenSize.height);
		CCEGLView::sharedOpenGLView()->setDesignResolutionSize(640, 960,
				kResolutionExactFit);
	} else {
		CCFileUtils::sharedFileUtils()->setResourceDirectory("sd");
		pDirector->setContentScaleFactor(480.0f / screenSize.height);
		CCEGLView::sharedOpenGLView()->setDesignResolutionSize(320, 480,
				kResolutionExactFit);
	}

	// turn on display FPS
	pDirector->setDisplayStats(true);

	// set FPS. the default value is 1.0/60 if you don't call this
	pDirector->setAnimationInterval(1.0 / 60);

	// create a scene. it's an autorelease object
	CCScene *pScene = HelloWorld::scene();

	// run
	pDirector->runWithScene(pScene);

	return true;
}

// This function will be called when the app is inactive. When comes a phone call,it's be invoked too
void AppDelegate::applicationDidEnterBackground() {
	CCDirector::sharedDirector()->stopAnimation();

	// if you use SimpleAudioEngine, it must be pause
	CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
}

// this function will be called when the app is active again
void AppDelegate::applicationWillEnterForeground() {
	CCDirector::sharedDirector()->startAnimation();

	// if you use SimpleAudioEngine, it must resume here
	CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
}

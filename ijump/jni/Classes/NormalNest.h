/*
 * BaseNest.h
 *
 *  Created on: 2013-9-22
 *      Author: yujsh
 */

#ifndef BASENEST_H_
#define BASENEST_H_

#include "cocos2d.h"
#include "GameData.h"

USING_NS_CC;

class NormalNest: public CCObject {
public:
	~NormalNest();

	CREATE_FUNC(NormalNest)
	CC_SYNTHESIZE_RETAIN(CCSprite*,m_nestSprite,NestSprite)

	void runAction();
	virtual bool init();
	virtual bool catchEgg();
private:
	Nest m_nestData;

	virtual void firstActionEnd();
	virtual CCActionInterval* createFirstAction();
	virtual CCActionInterval* createCircleAction();
};

#endif /* BASENEST_H_ */

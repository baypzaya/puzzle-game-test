/*
 * NestLayer.h
 *
 *  Created on: 2013-6-8
 *      Author: yujsh
 */

#ifndef NESTLAYER_H_
#define NESTLAYER_H_

#include "cocos2d.h"
#include "GameData.h"
#include "NormalNest.h"

USING_NS_CC;

class NestLayer: public CCLayer {
public:
	virtual ~NestLayer();
	virtual bool init();
	CREATE_FUNC(NestLayer);

	CCSprite* catchEgg(CCSprite* jumpEgg);
	void updateNestPositon(CCPoint position);
	CC_SYNTHESIZE_READONLY(bool,m_isMoving,Moving);
	CCSprite* getBaseNest();
private:
	CCLayer* scollerLayer1;
	CCLayer* scollerLayer2;
	CCArray* m_nestArray;
	void createNest();
	void moveEnd();
	void addToLayer(CCSprite* nestSprite);
};

#endif /* NESTLAYER_H_ */

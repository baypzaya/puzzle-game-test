/*
 * NestLayer.h
 *
 *  Created on: 2013-6-8
 *      Author: yujsh
 */

#ifndef NESTLAYER_H_
#define NESTLAYER_H_

#include "cocos2d.h"

USING_NS_CC;

class NestLayer: public CCLayer {
public:
	virtual ~NestLayer();
	virtual bool init();
	CREATE_FUNC(NestLayer);

	CCSprite* catchEgg(CCSprite* jumpEgg);
	void updateNestPositon(CCPoint position);
private:
	static const int nest_step_height = 320;
	static const int nest_max_move_speed = 150;
	static const int nest_min_move_speed = 50;

	CCLayer* scollerLayer1;
	CCLayer* scollerLayer2;
	CCArray* m_nestArray;
	void createNest();
	CCActionInterval* createNestAction(CCSprite* nest);
	int lastNestHeight;
	void moveEnd();
};

#endif /* NESTLAYER_H_ */

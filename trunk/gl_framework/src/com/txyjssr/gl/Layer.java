/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.txyjssr.gl;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.view.MotionEvent;

public abstract class Layer {
	float mX = 0f;
	float mY = 0f;
	float mWidth = 0;
	float mHeight = 0;
	boolean mHidden = false;

	public final float getX() {
		return mX;
	}

	public final float getY() {
		return mY;
	}

	public final void setPosition(float x, float y) {
		mX = x;
		mY = y;
	}

	public final float getWidth() {
		return mWidth;
	}

	public final float getHeight() {
		return mHeight;
	}

	public final void setSize(float width, float height) {
		if (mWidth != width || mHeight != height) {
			mWidth = width;
			mHeight = height;
			onSizeChanged();
		}
	}

	public boolean isHidden() {
		return mHidden;
	}

	public void setHidden(boolean hidden) {
		if (mHidden != hidden) {
			mHidden = hidden;
			onHiddenChanged();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	public boolean containsPoint(float x, float y) {
		float minX = mX;
        float minY = mY;
        float maxX = minX + mWidth;
        float maxY = minY + mHeight;
        boolean result = x >= minX && y >= minY && x < maxX && y < maxY;
		return result;
	}

	public void onSizeChanged() {
	}

	public void onHiddenChanged() {
	}

	public abstract void render(GL10 gl);

}

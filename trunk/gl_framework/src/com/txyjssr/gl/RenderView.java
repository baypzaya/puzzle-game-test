package com.txyjssr.gl;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.opengl.GLSurfaceView;

public class RenderView implements GLSurfaceView.Renderer, SensorEventListener {

	public static final Map<Integer, BaseScene> SCENE_MAP = new HashMap<Integer, BaseScene>();

	private BaseScene mCurrentScene;

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {

	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// 重置当前的模型观察矩阵
//		gl.glLoadIdentity();
		if (mCurrentScene != null) {
			synchronized (mCurrentScene) {
				mCurrentScene.render(gl);
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		float ratio = (float) width / height;
		// 设置OpenGL场景的大小
		gl.glViewport(0, 0, width, height);
		// 设置投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// 重置投影矩阵
		gl.glLoadIdentity();
		// 设置视口的大小
		gl.glFrustumf(-ratio, ratio, -1, 1, 0, 10);
		// 选择模型观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 重置模型观察矩阵
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// 黑色背景
		gl.glClearColor(0, 0, 0, 0);
		gl.glEnable(GL10.GL_CULL_FACE);
		// 启用阴影平滑
		gl.glShadeModel(GL10.GL_SMOOTH);
		// 启用深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// 启用纹理映射
		gl.glClearDepthf(1.0f);
		// 深度测试的类型
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// 精细的透视修正
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		// 允许2D贴图,纹理
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glColor4f(1, 1, 1, 0.5f);

		gl.glEnable(GL10.GL_BLEND);

		// IntBuffer intBuffer = IntBuffer.allocate(1);
		// // 创建纹理
		// gl.glGenTextures(1, intBuffer);
		// texture = intBuffer.get();
		//
		// // 设置要使用的纹理
		// gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		//
		// //生成纹理
		// GLUtils.texImage2D(GL10.GL_TEXTURE_2D,
		// 0,BitmapFactory.decodeResource(mContext.getResources(),
		// R.drawable.mini_button_press), 0);

		// 线形滤波
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
	}

	public BaseScene getCurrentScene() {
		return mCurrentScene;
	}

	public void setState(int state) {
		mCurrentScene = SCENE_MAP.get(state);
	}

}

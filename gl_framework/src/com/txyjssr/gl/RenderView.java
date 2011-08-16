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
		// �����Ļ����Ȼ���
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// ���õ�ǰ��ģ�͹۲����
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
		// ����OpenGL�����Ĵ�С
		gl.glViewport(0, 0, width, height);
		// ����ͶӰ����
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// ����ͶӰ����
		gl.glLoadIdentity();
		// �����ӿڵĴ�С
		gl.glFrustumf(-ratio, ratio, -1, 1, 0, 10);
		// ѡ��ģ�͹۲����
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// ����ģ�͹۲����
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// ��ɫ����
		gl.glClearColor(0, 0, 0, 0);
		gl.glEnable(GL10.GL_CULL_FACE);
		// ������Ӱƽ��
		gl.glShadeModel(GL10.GL_SMOOTH);
		// ������Ȳ���
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// ��������ӳ��
		gl.glClearDepthf(1.0f);
		// ��Ȳ��Ե�����
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// ��ϸ��͸������
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		// ����2D��ͼ,����
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glColor4f(1, 1, 1, 0.5f);

		gl.glEnable(GL10.GL_BLEND);

		// IntBuffer intBuffer = IntBuffer.allocate(1);
		// // ��������
		// gl.glGenTextures(1, intBuffer);
		// texture = intBuffer.get();
		//
		// // ����Ҫʹ�õ�����
		// gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		//
		// //��������
		// GLUtils.texImage2D(GL10.GL_TEXTURE_2D,
		// 0,BitmapFactory.decodeResource(mContext.getResources(),
		// R.drawable.mini_button_press), 0);

		// �����˲�
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

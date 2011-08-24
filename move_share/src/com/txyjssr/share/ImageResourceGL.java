package com.txyjssr.share;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;

public class ImageResourceGL implements Renderer {

	int one = 0x10000;

	int texture = -1;

	// 三角形三个顶点
	private int[] trigger = { 0, one, 0, // 上顶点
			-one, -one, 0, // 左下点
			one, -one, 0, }; // 右下点

	// 正方形的4个顶点
	private int[] quater = { -one,-one,one,
			one,-one,one,
			one,one,one,
			-one,one,one,};

	int[] coords = { one,0,0,0,0,one,one,one,	 // Front

	};

	int[] pointer = { one, 0, 0, one, 0, one, 0, one, 0, 0, one, one, };
	
	ByteBuffer indices = ByteBuffer.wrap(new byte[]{
			0,1,3,2,
	});

	private IntBuffer triggerBuffer = null;
	private IntBuffer quaterBuffer = null;
	private IntBuffer pointerBuffer = null;
	private Bitmap mBitmap = null;

	private Context mContext = null;
	IntBuffer texCoords;
	Texture2D texture2D;
	float rote = 0.5f;

	public ImageResourceGL(Context context) {
		mContext = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// 重置当前的模型观察矩阵
		gl.glLoadIdentity();

//		// 左移 1.5 单位，并移入屏幕 6.0
//		gl.glTranslatef(-1.5f, 0.0f, -6.0f);
//		gl.glRotatef(30, 0, 0, 1);
//
//		// 允许设置顶点
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//
//		gl.glColorPointer(4, GL10.GL_FIXED, 0, pointerBuffer);
//		
//		// 设置三角形
//		gl.glVertexPointer(3, GL10.GL_FIXED, 0, triggerBuffer);
//		// 绘制三角形
//		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
//		
//		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
//		
//		//绘制三角形结束
////		gl.glFinish();		
//
//		// 重置当前的模型观察矩阵
//		gl.glLoadIdentity();
//		// 左移 1.5 单位，并移入屏幕 6.0
//		gl.glTranslatef(1.5f, 0.0f, -6.0f);
//		
//		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
//		// 设置和绘制正方形
//		gl.glVertexPointer(3, GL10.GL_FIXED, 0, quaterBuffer);
//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//		
//		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glTranslatef(0.0f, 0.0f, -10.0f);
		
		gl.glRotatef(rote, 0, 1, 0);
		// 绑定纹理
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		//纹理和四边形对应的顶点
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, quaterBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texCoords);

		//绘制
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices);

	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    
	    rote+=0.5;
	    rote = rote % 360;
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
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		// 选择模型观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 重置模型观察矩阵
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		triggerBuffer = creatIntBuffer(trigger);
		quaterBuffer = creatIntBuffer(quater);
		pointerBuffer = creatIntBuffer(pointer);
		texCoords = creatIntBuffer(coords);
		mBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.mini_button_press);

		// 黑色背景
		gl.glClearColor(0, 0, 0, 0);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		// 启用阴影平滑
		gl.glShadeModel(GL10.GL_SMOOTH);
		// 启用深度测试
//		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		//启用纹理映射
		gl.glClearDepthf(1.0f);
		//深度测试的类型
		gl.glDepthFunc(GL10.GL_LEQUAL);
		//精细的透视修正
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		//允许2D贴图,纹理
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glColor4f(1, 1, 1, 0.5f);
		
		gl.glEnable(GL10.GL_BLEND);
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		// 创建纹理
		gl.glGenTextures(1, intBuffer);
		texture = intBuffer.get();
		// 设置要使用的纹理
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		//生成纹理
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0,BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.mini_button_press), 0);
		
		// 线形滤波
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}

	public IntBuffer creatIntBuffer(int[] a) {
		IntBuffer mBuffer = null;
		// 先初始化buffer,数组的长度*4,因为一个int占4个字节
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// 数组排列用nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		mBuffer = mbb.asIntBuffer();
		mBuffer.put(a);
		mBuffer.position(0);
		return mBuffer;
	}

}

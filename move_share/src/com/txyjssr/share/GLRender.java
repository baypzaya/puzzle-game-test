package com.txyjssr.share;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.view.KeyEvent;

public class GLRender implements Renderer
{
	boolean key = true;
	float xrot, yrot;
	float xspeed, yspeed;
	float z = -5.0f;
	int one = 0x10000;
	
	//光线参数
	FloatBuffer lightAmbient = creatFloatBuffer(new float[]{0.5f,0.5f,0.5f,1.0f}); 
	FloatBuffer lightDiffuse = creatFloatBuffer(new float[]{1.0f,1.0f,1.0f,1.0f}); 
	FloatBuffer lightPosition = creatFloatBuffer(new float[]{0.0f,0.0f,2.0f,1.0f}); 
	
	int filter = 1;
	
	int [] texture;
	
	IntBuffer vertices = creatIntBuffer(new int[]{
			-one,-one,one,
			one,-one,one,
			one,one,one,
			-one,one,one,
			
			-one,-one,-one,
			-one,one,-one,
			one,one,-one,
			one,-one,-one,
			
			-one,one,-one,
			-one,one,one,
			one,one,one,
			one,one,-one,
			
			-one,-one,-one,
			one,-one,-one,
			one,-one,one,
			-one,-one,one,
			
			one,-one,-one,
			one,one,-one,
			one,one,one,
			one,-one,one,
			
			-one,-one,-one,
			-one,-one,one,
			-one,one,one,
			-one,one,-one,
			
	});
	
	IntBuffer normals = creatIntBuffer(new int[]{
			0,0,one,
			0,0,one,
			0,0,one,
			0,0,one,
			
			0,0,one,
			0,0,one,
			0,0,one,
			0,0,one,
			
			0,one,0,
			0,one,0,
			0,one,0,
			0,one,0,
			
			0,-one,0,
			0,-one,0,
			0,-one,0,
			0,-one,0,
			
			one,0,0,
			one,0,0,
			one,0,0,
			one,0,0,
			
			-one,0,0,
			-one,0,0,
			-one,0,0,
			-one,0,0,
	});
	
	IntBuffer texCoords = creatIntBuffer(new int[]{
		one,0,0,0,0,one,one,one,	
		0,0,0,one,one,one,one,0,
		one,one,one,0,0,0,0,one,
		0,one,one,one,one,0,0,0,
		0,0,0,one,one,one,one,0,
		one,0,0,0,0,one,one,one,
	});
	
	ByteBuffer indices = ByteBuffer.wrap(new byte[]{
			0,1,3,2,
			4,5,7,6,
			8,9,11,10,
			12,13,15,14,
			16,17,19,18,
			20,21,23,22,
	});
	
	Context mContext ;
	
	GLRender(Context context){
		mContext = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 重置当前的模型观察矩阵
		gl.glLoadIdentity();
		
		gl.glEnable(GL10.GL_LIGHTING);
		
		////////////////
		gl.glTranslatef(0.0f, 0.0f, z);
		
		//设置旋转
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);

		//设置纹理
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[filter]);
		
		gl.glNormalPointer(GL10.GL_FIXED, 0, normals);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertices);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texCoords);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		//绘制四边形
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 24,  GL10.GL_UNSIGNED_BYTE, indices);
		
	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	    //修改旋转角度
	    xrot+=0.3f; 
	    yrot+=0.2f;
	    
	    //混合开关
	    if (key)
		{
	    	gl.glEnable(GL10.GL_BLEND);		// 打开混合
	    	gl.glDisable(GL10.GL_DEPTH_TEST);	// 关闭深度测试
		}
	    else 
	    {
	    	gl.glDisable(GL10.GL_BLEND);		// 关闭混合
	    	gl.glEnable(GL10.GL_DEPTH_TEST);	// 打开深度测试
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		float ratio = (float) width / height;
		//设置OpenGL场景的大小
		gl.glViewport(0, 0, width, height);
		//设置投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		//重置投影矩阵
		gl.glLoadIdentity();
		// 设置视口的大小
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		// 选择模型观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);	
		// 重置模型观察矩阵
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.icon);
		
		gl.glDisable(GL10.GL_DITHER);

		// 告诉系统对透视进行修正
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		// 黑色背景
		gl.glClearColor(0, 0, 0, 0);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		// 启用阴影平滑
		gl.glShadeModel(GL10.GL_SMOOTH);
		// 启用深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		//设置光线,,1.0f为全光线，a=50%
		gl.glColor4f(1.0f,1.0f,1.0f,0.5f);	
		// 基于源象素alpha通道值的半透明混合函数
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE);		

		//纹理相关
		IntBuffer textureBuffer = IntBuffer.allocate(3);
		gl.glGenTextures(3, textureBuffer);
		texture = textureBuffer.array();
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR); // ( NEW )
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR); // ( NEW )
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
		
		//深度测试相关
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//设置环境光
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient);

	    //设置漫射光
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse);

	    //设置光源位置
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition);
	    
	    //开启一号光源
	    gl.glEnable(GL10.GL_LIGHT1);
	    
	    //开启混合
	    gl.glEnable(GL10.GL_BLEND);
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		key = !key;
		return false;
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
	
	public FloatBuffer creatFloatBuffer(float[] a) {
		FloatBuffer mBuffer = null;
		// 先初始化buffer,数组的长度*4,因为一个int占4个字节
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// 数组排列用nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		mBuffer = mbb.asFloatBuffer();
		mBuffer.put(a);
		mBuffer.position(0);
		return mBuffer;
	}
}


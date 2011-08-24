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

	// ��������������
	private int[] trigger = { 0, one, 0, // �϶���
			-one, -one, 0, // ���µ�
			one, -one, 0, }; // ���µ�

	// �����ε�4������
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
		// �����Ļ����Ȼ���
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// ���õ�ǰ��ģ�͹۲����
		gl.glLoadIdentity();

//		// ���� 1.5 ��λ����������Ļ 6.0
//		gl.glTranslatef(-1.5f, 0.0f, -6.0f);
//		gl.glRotatef(30, 0, 0, 1);
//
//		// �������ö���
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//
//		gl.glColorPointer(4, GL10.GL_FIXED, 0, pointerBuffer);
//		
//		// ����������
//		gl.glVertexPointer(3, GL10.GL_FIXED, 0, triggerBuffer);
//		// ����������
//		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
//		
//		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
//		
//		//���������ν���
////		gl.glFinish();		
//
//		// ���õ�ǰ��ģ�͹۲����
//		gl.glLoadIdentity();
//		// ���� 1.5 ��λ����������Ļ 6.0
//		gl.glTranslatef(1.5f, 0.0f, -6.0f);
//		
//		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
//		// ���úͻ���������
//		gl.glVertexPointer(3, GL10.GL_FIXED, 0, quaterBuffer);
//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//		
//		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glTranslatef(0.0f, 0.0f, -10.0f);
		
		gl.glRotatef(rote, 0, 1, 0);
		// ������
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		//������ı��ζ�Ӧ�Ķ���
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, quaterBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texCoords);

		//����
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices);

	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    
	    rote+=0.5;
	    rote = rote % 360;
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
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		// ѡ��ģ�͹۲����
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// ����ģ�͹۲����
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

		// ��ɫ����
		gl.glClearColor(0, 0, 0, 0);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		// ������Ӱƽ��
		gl.glShadeModel(GL10.GL_SMOOTH);
		// ������Ȳ���
//		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		//��������ӳ��
		gl.glClearDepthf(1.0f);
		//��Ȳ��Ե�����
		gl.glDepthFunc(GL10.GL_LEQUAL);
		//��ϸ��͸������
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		//����2D��ͼ,����
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glColor4f(1, 1, 1, 0.5f);
		
		gl.glEnable(GL10.GL_BLEND);
		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		// ��������
		gl.glGenTextures(1, intBuffer);
		texture = intBuffer.get();
		// ����Ҫʹ�õ�����
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		//��������
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0,BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.mini_button_press), 0);
		
		// �����˲�
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}

	public IntBuffer creatIntBuffer(int[] a) {
		IntBuffer mBuffer = null;
		// �ȳ�ʼ��buffer,����ĳ���*4,��Ϊһ��intռ4���ֽ�
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// ����������nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		mBuffer = mbb.asIntBuffer();
		mBuffer.put(a);
		mBuffer.position(0);
		return mBuffer;
	}

}

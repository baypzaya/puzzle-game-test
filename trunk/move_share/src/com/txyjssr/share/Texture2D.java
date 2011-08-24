package com.txyjssr.share;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLUtils;

public class Texture2D {
    private int mWidth;
    private int mHeight;
    private int mPow2Width;
    private int mPow2Height;
    private float maxU = 1.0f;
    private float maxV = 1.0f;
    
    private Bitmap mBitmap = null;
    
    private int textureId = 0;
    
    
    // ɾ����������
    public void delete(GL10 gl)
    {
        if (textureId != 0){
            gl.glDeleteTextures(1, new int[]{textureId}, 0);
            textureId = 0;
        }
        
        // bitmap
        if (mBitmap != null)
        {
            if (mBitmap.isRecycled())
                mBitmap.recycle();
            mBitmap = null;
        }
        
    }
    
    public static int pow2(int size)
    {
        int small = (int)(Math.log((double)size)/Math.log(2.0f)) ;
        if ( (1 << small) >= size)
            return 1 << small;
        else 
            return 1 << (small + 1);
    }
    
    // �������Ƴٵ���һ�ΰ�ʱ
    public Texture2D(Bitmap bmp)
    {
        // mBitmap = bmp;
        mWidth = bmp.getWidth();
        mHeight = bmp.getHeight();
        
        mPow2Height = pow2(mHeight);
        mPow2Width =pow2(mWidth);
        
        maxU = mWidth/(float)mPow2Width;
        maxV = mHeight/(float)mPow2Height;
        
        Bitmap bitmap = Bitmap.createBitmap(mPow2Width, mPow2Height,
                bmp.hasAlpha() ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, 0, 0, null);
        mBitmap = bitmap;
    }
    
    // ��һ�λ������������
    public void bind(GL10 gl)
    {
        if (textureId ==0)
        {
            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);
            textureId = textures[0];
            
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
            
            mBitmap.recycle();
            mBitmap = null;
        }
        
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }
    
    // ���Ƶ���Ļ��
    public void draw(GL10 gl, float x, float y)
    {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
 
        //  ��
        this.bind(gl);
        
        // ӳ��
        FloatBuffer verticleBuffer = creatFloatBuffer(new float[]{
            x,y,
            x+mWidth, 0,
            x, y+mHeight,
            x+mWidth, y+mHeight,
        });
        FloatBuffer coordBuffer = creatFloatBuffer(new float[]{
            0,0,
            maxU,0,
            0,maxV,
            maxU,maxV,
        });
        
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,4);
        
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
    
    public void draw(GL10 gl, float x, float y, float width, float height)
    {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        
        //  ��
        bind(gl);
        
        // ӳ��
        // ӳ��
        FloatBuffer verticleBuffer = creatFloatBuffer(new float[]{
            x,y,
            x+width, 0,
            x, y+height,
            x+width, y+height,
        });
        FloatBuffer coordBuffer = creatFloatBuffer(new float[]{
            0,0,
            maxU,0,
            0,maxV,
            maxU,maxV,
        });
        
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,4);
        
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        
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
    
    public FloatBuffer creatFloatBuffer(float[] a) {
    	FloatBuffer mBuffer = null;
		// �ȳ�ʼ��buffer,����ĳ���*4,��Ϊһ��intռ4���ֽ�
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		// ����������nativeOrder
		mbb.order(ByteOrder.nativeOrder());
		mBuffer = mbb.asFloatBuffer();
		mBuffer.put(a);
		mBuffer.position(0);
		return mBuffer;
	}
    
}

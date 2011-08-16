package com.txyjssr.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLUtils {
	
	public static IntBuffer creatIntBuffer(int[] a) {
		IntBuffer mBuffer = null;
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		mbb.order(ByteOrder.nativeOrder());
		mBuffer = mbb.asIntBuffer();
		mBuffer.put(a);
		mBuffer.position(0);
		return mBuffer;
	}
	
	public static FloatBuffer creatFloatBuffer(float[] a) {
		FloatBuffer mBuffer = null;
		ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
		mbb.order(ByteOrder.nativeOrder());
		mBuffer = mbb.asFloatBuffer();
		mBuffer.put(a);
		mBuffer.position(0);
		return mBuffer;
	}
	
	
}

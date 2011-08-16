package com.txyjssr.gl.test;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;
import android.widget.Toast;

import com.txyjssr.gl.AppUtils;
import com.txyjssr.gl.GLUtils;
import com.txyjssr.gl.Layer;

public class TestTragleLayer extends Layer{
	int one = 10000;
	
	// ��������������
	private int[] trigger = { 0, one, 0, // �϶���
			-one, -one, 0, // ���µ�
			one, -one, 0, }; // ���µ�
	
	private IntBuffer triggerBuffer = null;
	
	public TestTragleLayer(){
		triggerBuffer= GLUtils.creatIntBuffer(trigger);
	}

	@Override
	public void render(GL10 gl) {
		gl.glTranslatef(-0.0005f, 0.0f, 0f);
		// �������ö���
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);		
		// ����������
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, triggerBuffer);
		// ����������
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			Toast.makeText(AppUtils.sContext, "event down", Toast.LENGTH_SHORT);
		}
		
		if(action == MotionEvent.ACTION_UP){
			Toast.makeText(AppUtils.sContext, "event up", Toast.LENGTH_SHORT);
		}
		
		return super.onTouchEvent(event);
	}
	
	

}

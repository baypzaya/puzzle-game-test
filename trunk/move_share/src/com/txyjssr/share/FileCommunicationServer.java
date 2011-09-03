package com.txyjssr.share;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class FileCommunicationServer implements Communication {

	private int mState = 0;
	private File tranFile;

	@Override
	public void execute(OutputStream out, InputStream in) {
		File sdcard = Environment.getExternalStorageDirectory();
		String sdPath = sdcard.getPath();
		String savePath = sdPath + "/move_share";
		File saveFile = new File(savePath);
		if(!saveFile.exists()){
			saveFile.mkdir();
		}
		
		byte[] buffer = new byte[1024];
		int num = 0;
		OutputStream outputStream = null;
		try {
			while ((num = in.read(buffer)) > -1) {
				if (mState == 0) {
					String message = new String(buffer, 0, num);
					JSONObject jso;
					try {
						jso = new JSONObject(message);
						String fileName = jso.getString("file_name");
						Log.i("yujsh log", "fileName :" + fileName);
						tranFile = new File(savePath+"/"+fileName);
						if(!tranFile.exists()){
							tranFile.createNewFile();							
						}
						outputStream = new FileOutputStream(tranFile);
						mState=1;
						continue;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				if(mState == 1){					
					outputStream.write(buffer, 0, num);
					outputStream.flush();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("yujsh log", "server read end");
	}

}

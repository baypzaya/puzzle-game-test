package com.txyjssr.share;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.Log;

public class FileCommunicationClient implements Communication {

	private Uri mFileUri;
	private OutputStream mOut;
	private InputStream mIn;

	public FileCommunicationClient(Uri fileUri) {
		mFileUri = fileUri;
	}

	@Override
	public void execute(OutputStream out, InputStream in) {
		
		if (mFileUri == null){
			return;
		}
		
		mOut = out;
		mIn = in;
		ContentResolver cr = AppUtils.sActivity.getContentResolver();
		String[] projection = { Images.Media.DATA };
		Cursor c = null;
		InputStream fileInputStream = null;
		try {
			c = cr.query(mFileUri, projection, null, null, null);
			if (c != null && c.moveToNext()) {

				String filePath = c.getString(c.getColumnIndex(Images.Media.DATA));
				File file = new File(filePath);
				String fileName = file.getName();

//				StringBuffer sb = new StringBuffer();
//				// sb.append("communication_type").append(":").append(1);
//				sb.append("file_name").append(":").append(fileName);
				
				JSONObject jso = new JSONObject();
				try {
					jso.put("file_name", fileName);
				} catch (JSONException e) {					
					e.printStackTrace();
				}
				Log.i("yujsh log" , "client writing file name");
				this.sendCommond(jso.toString());

//				boolean isReady = false;
				Log.i("yujsh log" , "client writing file");
				byte[] buffer = new byte[1024];
//				int num =0;
//				if ((num = in.read(buffer)) > -1) {
//					String message = new String(buffer,0,num);
//					if ("ready".equals(message)) {
//						isReady = true;
//					}
//
//				}

//				if (isReady) {
				fileInputStream = new FileInputStream(filePath);
				int num = 0;
				while ((num = fileInputStream.read(buffer)) > 0) {
					out.write(buffer, 0, num);
					out.flush();
				}

					// test code
//					out.write("mytest".getBytes());
//					out.flush();
//
//				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
			if (fileInputStream != null)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		Log.i("yujsh log" , "client write end");
	}

	private void sendCommond(String commond) throws IOException {
		byte[] commondBytes = commond.getBytes();
		mOut.write(commondBytes);
		mOut.flush();
	}
}

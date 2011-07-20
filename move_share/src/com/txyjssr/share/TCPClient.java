package com.txyjssr.share;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class TCPClient {
	public static final int ERROR_NO_FIND_HOST = 1;
	public static final int ERROR_STRANSLATE_FAILD = 2;

	private String mHost;
	private int mPort;
	private Socket mSocket;
	private Communication mCommunication;
	private OnErrorListener mOnErrorListener;
	private OnCompleteListener mOnCompleteListener;

	public interface OnErrorListener {
		public void onError(int errorType);
	}

	public interface OnCompleteListener {
		public void onComplete();
	}

	public TCPClient(String host, int port, Communication comunication) {
		mHost = host;
		mPort = port;
		mCommunication = comunication;
	}

	public void start() {
		try {
			connect();
			communicate();
			if (mOnCompleteListener != null) {
				mOnCompleteListener.onComplete();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			if (mOnErrorListener != null) {
				mOnErrorListener.onError(ERROR_NO_FIND_HOST);
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (mOnErrorListener != null) {
				mOnErrorListener.onError(ERROR_STRANSLATE_FAILD);
			}
		} finally {
			close();
		}
	}

	private void connect() throws UnknownHostException, IOException {
		mSocket = new Socket(mHost, mPort);
	}

	private void close() {
		try {
			if (mSocket != null) {
				mSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void communicate() throws IOException {

		if (mSocket == null)
			return;

		OutputStream out = mSocket.getOutputStream();
		InputStream in = mSocket.getInputStream();
		if (mCommunication != null) {
			mCommunication.execute(out, in);
		} else {
			// testCode
			PrintWriter output = new PrintWriter(out, true);
			output.println("Hello IdeasAndroid!");
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			// read line(s)
			String message = input.readLine();
			Log.d("yujsh log", "message From Server:" + message);
		}
	}

}

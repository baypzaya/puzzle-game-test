package com.txyjssr.share;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class TCPServer implements Runnable {

	public static final int SERVER_PORT = 5211;
	private boolean isCloseServer = false;
	private Communication mCommunication;

	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(SERVER_PORT);
			while (!isCloseServer) {
				// 等待客户端连接
				Socket s = ss.accept();
				OutputStream out = s.getOutputStream();
				InputStream in = s.getInputStream();
				if (mCommunication != null) {
					mCommunication.execute(out, in);
				} else {
					BufferedReader input = new BufferedReader(
							new InputStreamReader(in));
					PrintWriter output = new PrintWriter(out, true);
					String message = input.readLine();
					Log.i("yujsh log","server receive:"+message);
					output.println("message received!");
					if ("shutDown".equals(message)) {
						isCloseServer = true;
					}
					
				}
				s.close();
			}
			ss.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeServer() {
		isCloseServer = true;
	}

}

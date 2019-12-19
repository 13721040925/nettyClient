package com.wdt.datacenter.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class SocketNettyClientTest {

	public static final String IP = "127.0.0.1";// 服务器地址
	public static final int PORT = 10110;// 服务器端口号

	public static void main(String[] args) {
		handler();
	}

	private static void handler() {
		try {
			Socket client = new Socket(IP, PORT);
			new Thread(new ClientReadHandlerThread(client)).start();
			new Thread(new ClientWriteHandlerThread(client)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ClientReadHandlerThread implements Runnable {
	private Socket client;

	public ClientReadHandlerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		BufferedReader in = null;
		System.out.println("Client reader started.");
		String str = null;
		char [] readBuf = new char[1024];
		int charLen = 0;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while ((charLen = in.read(readBuf)) > 0) {
				str = new String(readBuf).substring(0, charLen);
				System.out.println("<-: " + str);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (client != null) {
					client = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ClientWriteHandlerThread implements Runnable {
	private Socket client;

	public ClientWriteHandlerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		Writer writer = null;
		
		try {
			int i = 0;
			writer = new OutputStreamWriter(client.getOutputStream());
			while (true) {
				//writer.write("I am Client: num is " + i++ + "\n");
				writer.write("I am Client: num is " + i++ );
				writer.flush();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (client != null) {
					client = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
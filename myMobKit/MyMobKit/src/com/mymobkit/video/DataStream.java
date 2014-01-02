package com.mymobkit.video;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

import com.mymobkit.config.AppConfig;

public final class DataStream
{
	private static final String TAG = AppConfig.LOG_TAG_APP + ":DataStream";

	private LocalSocket receiver, sender;
	private LocalServerSocket lss;
	private String localAddress;
	private boolean isConnected = false;

	public DataStream(final String addr)
	{
		this.localAddress = addr;
		try {
			lss = new LocalServerSocket(localAddress);
		} catch (IOException e) {
			Log.e(TAG, "[DataStream] Error creating server socket", e);
		}
	}
	
	public String getLocalAddress(){
		return localAddress;
	}

	public InputStream getInputStream() throws IOException {
		return receiver.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		if (sender == null) return null;
		return sender.getOutputStream();
	}

	public FileDescriptor getOutputStreamDescriptor() {
		return sender.getFileDescriptor();
	}

	public LocalSocket getSender(){
		return sender;
	}
	
	public LocalSocket getReceiver(){
		return receiver;
	}
	
	public void release()
	{
		try {
			if (receiver != null) {
				receiver.close();
			}
			if (sender != null) {
				sender.close();
			}
		} catch (IOException ioEx) {
			Log.w(TAG, "[release] Error releasing receiver", ioEx);
		}

		sender = null;
		receiver = null;
		isConnected = false;
	}

	public boolean prepare(int recvBufferSize, int sendBufferSize)
	{
		receiver = new LocalSocket();
		try {
			receiver.connect(new LocalSocketAddress(localAddress));
			receiver.setReceiveBufferSize(recvBufferSize);
			sender = lss.accept();
			sender.setSoTimeout(1000);	// To detected disconnecte socket - Important
			sender.setSendBufferSize(sendBufferSize);
		} catch (IOException e) {
			Log.e(TAG, "[prepare] Error initialising receiver and sender", e);
			return false;
		}
		isConnected = true;
		return true;
	}

	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public String toString() {
		return "DataStream [receiver=" + receiver + ", sender=" + sender + ", lss=" + lss + ", localAddress=" + localAddress + ", isConnected=" + isConnected + "]";
	}

}

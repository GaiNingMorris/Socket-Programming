package socket.concurrency;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import socket.utils.ErrorInfo;
import socket.utils.InputUserInfo;
import socket.utils.SocketHelper;

public class SocketThread implements Runnable {
	
	private Socket connection;
	private InputUserInfo inputUserInfo;
	private boolean isOrdered;
	@SuppressWarnings("rawtypes")
	private Map tagsMap;
	
	public SocketThread(Socket conSocket, 
			@SuppressWarnings("rawtypes") Map tagsMap, boolean isOrdered) {
		this.connection = conSocket;
		this.isOrdered = isOrdered;
		this.tagsMap = tagsMap;
	}
	
	public void run() {
		try {
			inputUserInfo = SocketHelper.readMsgAndUpdateTags( // read message from client and update tags
					connection.getInputStream(), tagsMap, isOrdered); 
			SocketHelper.writeMsgToClient( // send message to client
					connection.getOutputStream(), tagsMap, inputUserInfo, null, isOrdered); 
		} catch (Exception e) {
			try {
				ErrorInfo errorInfo = new ErrorInfo();
				errorInfo.setError(e.getMessage());
				SocketHelper.writeMsgToClient(connection.getOutputStream(), null, 
						inputUserInfo, errorInfo, isOrdered); // send error information to client
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
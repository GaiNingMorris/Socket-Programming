package socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import socket.utils.SocketHelper;

/**
 * Socket Client
 * 
 * @author GaiNingMorris
 */
public class SocketClient {
	
	private static final int time_out = 60000;
	private static final int server_port = 30;
	private static final String server_address = "localhost";
	private String jsonStr;
	
	public SocketClient(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public static void main(String[] args) {
		SocketClient client = 
				new SocketClient(SocketHelper.convertToJsonString());
		for (int i = 0; i < 10; i++)
			client.service(server_address, server_port);
	}

	/**
	 * send message to server and read response from server
	 * 
	 * @param serverHost 
	 * 			  server address
	 * @param port 
	 * 			  server port
	 */
	public void service(String serverHost, int server_port) {
		Socket socket = null;
		
		try {
			// STEP 1: build connection with server
			socket = new Socket(server_address, server_port);
			socket.setSoTimeout(time_out);

			// STEP 2: get socket inputStream and outputStream
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			// STEP 3: write message to socket outputStream
			pw.println(jsonStr);
			socket.shutdownOutput();
			
			// get response from server
			String line = null;
			while (((line = br.readLine()) != null)) {
				System.out.println("Reponse message from server ：" + line);
			}
			
			// STEP 4: close resources
			pw.close();  
			os.close();  
            br.close();  
            is.close();  
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
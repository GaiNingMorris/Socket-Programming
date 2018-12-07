package socket.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import socket.utils.TagsInfo;

public class SocketThreadPoolService {

	/**
	 * read message from client and send response to client
	 * 
	 * @param threadPool
	 * 				thread pool
	 * @param port
	 * 				port
	 * @param isOrdered
	 * 				if it is ordered
	 */
	public void service(ExecutorService threadPool, int port, boolean isOrdered) {
		// a map to save all the tags and user IDs without order
		Map<String, List<String>> tagsMapWithOutOrder = 
				new ConcurrentHashMap<String, List<String>>();
		// a map to save all the tags and user IDs with order
		Map<String, TagsInfo> tagsMapWithOrder = 
				new ConcurrentHashMap<String, TagsInfo>();
		ServerSocket serverSocket = null;

		try {
			System.out.println("server is waitting for client......");
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket connection = null;
				connection = serverSocket.accept(); // get socket connection between server and client
				threadPool.execute(new SocketThread(connection, 
						isOrdered == true ? tagsMapWithOrder : tagsMapWithOutOrder, isOrdered));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * shut down thread pool when Ctrl+C
	 * 
	 * @param thredPool
	 *            thread pool
	 */
	public void shutDown(ExecutorService threadPool) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				threadPool.shutdownNow(); // shut down thread pool
			}
		});
	}
}
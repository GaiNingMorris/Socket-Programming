package socket.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import socket.concurrency.SocketThreadPoolService;

/**
 * Socket Server Thread Pool
 * 
 * @author GaiNingMorris
 *
 */
public class Part2SocketServer {

	private static final int server_port = 30;
	private static final int thread_pool_size = 50; // a thread pool size is 50

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(thread_pool_size);
		SocketThreadPoolService threadPoolService = new SocketThreadPoolService();
		threadPoolService.service(threadPool, server_port, true);
		threadPoolService.shutDown(threadPool); // shut down thread pool when Ctrl+C
	}
}
package socket.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import socket.client.SocketClient;
import socket.utils.InputUserInfo;

public class Part2TestCase {

	/**
	 * Single user, time stamp is as same as latest time stamp.
	 */
	@Test
	public void test1() { 
		// remove first
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");
		
		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army");
		userInfo.setRemove(removeList);
		
		userInfo.setTimestamp(LocalDateTime.now());
		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 5; i++)
			client.service("localhost", 30);
		
		// add later
		List<String> addList = new ArrayList<String>();
		addList.add("timbers_army");
		userInfo.setAdd(addList);
		userInfo.setRemove(null);
		
		client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 5; i++)
			client.service("localhost", 30);
	}
	
	/**
	 * Single user, time stamp is newer than latest time stamp.
	 * then manipulate it.
	 */
	//@Test
	public void test2() throws InterruptedException {
		// remove first
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");

		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army");
		userInfo.setRemove(removeList);

		userInfo.setTimestamp(LocalDateTime.now());
		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 5; i++)
			client.service("localhost", 30);

		// add later
		List<String> addList = new ArrayList<String>();
		addList.add("timbers_army");
		userInfo.setAdd(addList);
		userInfo.setRemove(null);
		
		userInfo.setTimestamp(LocalDateTime.now().plusNanos(10));
		client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 5; i++)
			client.service("localhost", 30);
	}
	
	/**
	 * Single user, time stamp is older than latest time stamp.
	 * then skip it.
	 */
	//@Test
	public void test3() {
		// remove first
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");

		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army");
		userInfo.setRemove(removeList);

		userInfo.setTimestamp(LocalDateTime.now());
		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 5; i++)
			client.service("localhost", 30);

		// add later
		List<String> addList = new ArrayList<String>();
		addList.add("timbers_army");
		addList.add("good_dog");
		userInfo.setAdd(addList);
		userInfo.setRemove(null);

		userInfo.setTimestamp(LocalDateTime.now().minusNanos(10));
		client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 5; i++)
			client.service("localhost", 30);
	}
}
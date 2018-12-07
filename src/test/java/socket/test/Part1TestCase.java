package socket.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import socket.client.SocketClient;
import socket.utils.InputUserInfo;

public class Part1TestCase {

	/**
	 * It is not an error to attempt to remove from 
	 * a user a tag that it does not currently have
	 */
	@Test
	public void test1() { 
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");
		
		List<String> addList = new ArrayList<String>();
		List<String> removeList = new ArrayList<String>();
		removeList.add("beyhive_member");
		
		userInfo.setAdd(addList);
		userInfo.setRemove(removeList);
		userInfo.setTimestamp(LocalDateTime.now());
		
		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 10; i++)
			client.service("localhost", 30);
	}
	
	/**
	 * If a tag appears multiple times in either add or remove,
	 * it is equivalent to appearing once. 
	 */
	//@Test
	public void test2() {
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");

		List<String> addList = new ArrayList<String>();
		addList.add("beyhive_member");
		addList.add("beyhive_member");
		addList.add("timbers_army");
		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army");
		removeList.add("silly_dog");
		removeList.add("sill_dog");

		userInfo.setAdd(addList);
		userInfo.setRemove(removeList);
		userInfo.setTimestamp(LocalDateTime.now());
		System.out.println(userInfo.getTimestamp());

		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 10; i++)
			client.service("localhost", 30);
	}
	
	/**
	 * If a tag appears in both add and remove,
	 * it should be treated as if it only appeared in remove.
	 */
	//@Test
	public void test3() {
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");

		List<String> addList = new ArrayList<String>();
		addList.add("timbers_army");
		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army");

		userInfo.setAdd(addList);
		userInfo.setRemove(removeList);
		userInfo.setTimestamp(LocalDateTime.now());
		System.out.println(userInfo.getTimestamp());

		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 10; i++)
			client.service("localhost", 30);
	}

	/**
	 * If user information format is null, 
	 * then server will response "{error: "request data format is wrong!"}"
	 */
	//@Test
	public void test4() { 
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");
		
		List<String> addList = new ArrayList<String>();
		List<String> removeList = new ArrayList<String>();

		userInfo.setAdd(addList);
		userInfo.setRemove(removeList);
		userInfo.setTimestamp(LocalDateTime.now());
		
		SocketClient client = new SocketClient(new Gson().toJson(null));
		for (int i = 0; i < 10; i++)
			client.service("localhost", 30);
	}
	
	/**
	 * only same user can affect their tags
	 */
	//@Test
	public void test5() {
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel 2");

		List<String> addList = new ArrayList<String>();
		addList.add("beyhive_member_2");
		addList.add("timbers_army_2");
		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army_2");

		userInfo.setAdd(addList);
		userInfo.setRemove(removeList);
		userInfo.setTimestamp(LocalDateTime.now());
		System.out.println(userInfo.getTimestamp());

		SocketClient client = new SocketClient(new Gson().toJson(userInfo));
		for (int i = 0; i < 10; i++)
			client.service("localhost", 30);
	}
}
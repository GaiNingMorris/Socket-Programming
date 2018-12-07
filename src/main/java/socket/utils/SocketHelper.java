package socket.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class SocketHelper {

	private static final String error_data_format = "request data format is wrong!";

	/**
	 * get request from client and update tags
	 * 
	 * @param inputStream
	 *            input stream
	 * @param tagsMap
	 *            tags map
	 * @return user information
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static InputUserInfo readMsgAndUpdateTags(InputStream inputStream,
			@SuppressWarnings("rawtypes") Map tagsMap, boolean isOrdered) 
					throws IOException {
		InputUserInfo inputUserInfo = null;
		Reader reader = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(reader);
		String line = null;

		while ((line = br.readLine()) != null) {
			System.out.println(line);
			inputUserInfo = new Gson().fromJson(line, InputUserInfo.class);

			if (inputUserInfo == null || inputUserInfo.getUser() == null)
				throw new ClassCastException(error_data_format);

			// update tags
			String userID = inputUserInfo.getUser().toLowerCase();
			System.out.println(inputUserInfo.getTimestamp());
			synchronized (userID) {
				if(!isOrdered) 
					tagsMap.put(userID, SocketHelper.getTagsListByUserIDWithOutOrder(
							inputUserInfo, (ConcurrentMap<String, List<String>>) tagsMap));
				else 
					getTagsListByUserIDWithOrder(inputUserInfo, 
							(ConcurrentMap<String, TagsInfo>) tagsMap);
			}
		}

		return inputUserInfo;
	}

	/**
	 * send response to client
	 * 
	 * @param outputStream
	 *            outputStream
	 * @param tagsMap
	 *            tags map
	 * @param inputUserInfo
	 *            user information
	 * @param errorInfo
	 *            error information
	 * @param isOrdered 
	 * 			  isOrdered          
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	public static void writeMsgToClient(OutputStream outputStream,
			@SuppressWarnings("rawtypes") Map tagsMap, InputUserInfo inputUserInfo, 
			ErrorInfo errorInfo, boolean isOrdered)
			throws IOException {
		PrintWriter pw = new PrintWriter(outputStream, true);

		if (errorInfo == null) {
			OutputUserInfo outputUserInfo = new OutputUserInfo();
			outputUserInfo.setUser(inputUserInfo.getUser());
			if(!isOrdered) {
				outputUserInfo.setTags((List<String>) tagsMap.get(inputUserInfo
						.getUser().toLowerCase()));
			}else {
				TagsInfo tagsInfo = (TagsInfo) tagsMap.get(inputUserInfo
						.getUser().toLowerCase());
				// filter the tags which operation is add
				List<String> tagsList = new ArrayList<String>();
				tagsInfo.getTagOpMap().forEach((k, v) -> {
					if (v) tagsList.add(k);
				});
				outputUserInfo.setTags(tagsList);
			}
			pw.println(new Gson().toJson(outputUserInfo));
		} else
			pw.println(new Gson().toJson(errorInfo));
		
		pw.close();
	}

	/**
	 * get the tags list by userID without order
	 * 
	 * @param inputUserInfo
	 *            user information
	 * @param tagsMap
	 *            tags map
	 * @return tags list
	 */
	private static List<String> getTagsListByUserIDWithOutOrder(InputUserInfo inputUserInfo,
			ConcurrentMap<String, List<String>> tagsMap) {
		List<String> addList = reduceDuplicateTags(inputUserInfo, tagsMap, true);
		List<String> removeList = reduceDuplicateTags(inputUserInfo, null, false);
		
		// remove the elements in addList
		removeList.forEach(v -> {
			if (addList.contains(v)) addList.remove(v);
		});

		return addList;
	}
	
	/**
	 * get the tags list by userID with order
	 * 
	 * @param inputUserInfo
	 * 			user information
	 * @param tagsMap
	 * 			tags map
	 */
	private static void getTagsListByUserIDWithOrder(InputUserInfo inputUserInfo,
			ConcurrentMap<String, TagsInfo> tagsMap) {
		List<String> addList = reduceDuplicateTags(inputUserInfo, null, true);
		List<String> removeList = reduceDuplicateTags(inputUserInfo, null, false);
		String userID = inputUserInfo.getUser().toLowerCase();
		TagsInfo tagsInfo = tagsMap.get(userID);
		
		if (!tagsMap.containsKey(userID)) { // when user is new 
			tagsInfo = new TagsInfo();
			tagsInfo.setTagOpMap(new HashMap<String, Boolean>());
			updateTagsToMap(tagsInfo, addList, true); 
			updateTagsToMap(tagsInfo, removeList, false); 
		} else if (inputUserInfo.getTimestamp() // when user is not new 
				.isBefore(tagsMap.get(userID).getLatestTimestamp())) { // and time is older than latest time
			return;
		} else if (inputUserInfo.getTimestamp() // when user is not new
				.isAfter(tagsMap.get(userID).getLatestTimestamp())) { // and time is newer than latest time
			updateTagsToMap(tagsInfo, addList, true); 
			updateTagsToMap(tagsInfo, removeList, false); 
		} else { // when user is not new and time is as same as latest time.
			for(String str : addList) {
				if(!tagsInfo.getTagOpMap().containsKey(str))
					tagsInfo.getTagOpMap().put(str, true);
			}
			updateTagsToMap(tagsInfo, removeList, false); 
		}
		
		tagsInfo.setLatestTimestamp(inputUserInfo.getTimestamp());
		tagsMap.put(userID, tagsInfo);
	}
	
	/**
	 * reduce duplicate tags in list
	 * @param inputUserInfo
	 * 			user information
	 * @param tagsMap
	 * 			tags map
	 * @param isAdd
	 * 			true is add list, false is remove list
	 * @return
	 */
	private static List<String> reduceDuplicateTags(InputUserInfo inputUserInfo, 
			ConcurrentMap<String, List<String>> tagsMap, boolean isAdd) {
		List<String> list = null;

		if(isAdd) { 
			list = inputUserInfo.getAdd() == null ? new ArrayList<String>() 
					: inputUserInfo.getAdd(); 
			if(tagsMap != null && tagsMap.containsKey( 
					inputUserInfo.getUser().toLowerCase()))
				list.addAll(tagsMap.get(inputUserInfo.getUser().toLowerCase()));
		} else 
			list = inputUserInfo.getRemove() == null ? new ArrayList<String>() 
					: inputUserInfo.getRemove();
		list = list.stream().map(String::toLowerCase).distinct()
				.collect(Collectors.toList()); // unique tags in list
		
		return list;
	}

	private static void updateTagsToMap(TagsInfo tagsInfo, List<String> list, 
			boolean isAdd) {
		list.forEach(v -> tagsInfo.getTagOpMap().put(v, isAdd));
	}
	
	/**
	 * convert intputUserInfo object into json string
	 */
	public static String convertToJsonString() {
		InputUserInfo userInfo = new InputUserInfo();
		userInfo.setUser("Secret Squirrel");

		// the tags needed to be added
		List<String> addList = new ArrayList<String>();
		addList.add("beyhive_member");
		addList.add("beyhive_member");
		addList.add("BEYHIVE_MEMBER");
		addList.add("timbers_army");
		addList.add("good_dog");
		addList.add("cute_rabbit");

		// the tags needed to be removed
		List<String> removeList = new ArrayList<String>();
		removeList.add("timbers_army");
		removeList.add("silly_cat");

		userInfo.setAdd(addList);
		userInfo.setRemove(removeList);
		userInfo.setTimestamp(LocalDateTime.now());
		System.out.println(userInfo.getTimestamp());
		
		return new Gson().toJson(userInfo);
	}
}
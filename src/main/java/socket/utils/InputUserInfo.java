package socket.utils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Input User Information
 * @author GaiNingMorris
 *
 */
public class InputUserInfo {
	
	// user ID
	private String user;

	// the tags needed to be added
	private List<String> add;
	
	// the tags needed to be removed
	private List<String> remove;
	
	// time stamp
	private LocalDateTime timestamp;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<String> getAdd() {
		return add;
	}

	public void setAdd(List<String> add) {
		this.add = add;
	}

	public List<String> getRemove() {
		return remove;
	}

	public void setRemove(List<String> remove) {
		this.remove = remove;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "InputUserInfo [user=" + user + ", add=" + add + ", remove=" + remove + ", timestamp=" + timestamp + "]";
	}
}
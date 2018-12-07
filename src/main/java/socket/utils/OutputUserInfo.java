package socket.utils;

import java.util.List;

/**
 * Output User Information
 * @author GaiNingMorris
 *
 */
public class OutputUserInfo {
	
	// user ID
	private String user;
	
	// all the tags needed to be returned to client
	private List<String> tags;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "OutputUserInfo [user=" + user + ", tags=" + tags + "]";
	}
}
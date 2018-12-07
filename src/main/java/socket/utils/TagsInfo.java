package socket.utils;

import java.time.LocalDateTime;
import java.util.Map;

public class TagsInfo {

	// latest time stamp
	private LocalDateTime latestTimestamp;

	// <tagName, operation> (operation: true is "add", false is "remove")
	private Map<String, Boolean> tagOpMap;

	public LocalDateTime getLatestTimestamp() {
		return latestTimestamp;
	}

	public void setLatestTimestamp(LocalDateTime latestTimestamp) {
		this.latestTimestamp = latestTimestamp;
	}

	public Map<String, Boolean> getTagOpMap() {
		return tagOpMap;
	}

	public void setTagOpMap(Map<String, Boolean> tagOpMap) {
		this.tagOpMap = tagOpMap;
	}

	@Override
	public String toString() {
		return "TagsInfo [latestTimestamp=" + latestTimestamp + ", tagOpMap=" + tagOpMap + "]";
	}
}
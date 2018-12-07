package socket.utils;

/**
 * Error Information
 * @author GaiNingMorris
 *
 */
public class ErrorInfo {
	
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ErrorInfo [error=" + error + "]";
	}
}

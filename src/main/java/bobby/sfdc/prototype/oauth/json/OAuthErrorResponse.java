package bobby.sfdc.prototype.oauth.json;

/**
 * Error response from OAuth call that fails.
 * This clean is a bean which is suitable for deserialization with GSon
 * 
 * Example return
 * 
{
"error_description": "authentication failure - Invalid Password",
"error": "invalid_grant"
}
 * @author bobby.white
 *
 */
public class OAuthErrorResponse {

	String error;
	String error_description;
	
	@Override
	public String toString() {
		return "ErrorResponse [error_description=" + error_description
				+ ", error=" + error + "]";
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}

/**
 * 
 */
package bobby.sfdc.prototype.oauth;

import bobby.sfdc.prototype.oauth.json.OAuthErrorResponse;

/**
 * Authentication Exception
 * 
 * @author bobby.white
 *
 */
public class AuthenticationException extends Exception {
	private static final long serialVersionUID = 7169775404962776767L;
	
	/**
	 * Default constructor
	 */
	public AuthenticationException() {
		super();
	}
	
	/**
	 * Constructor intended for use when wrapping and rethrowing an Exception
	 * @param msg
	 * @param wrappedException
	 */
	public AuthenticationException(String msg, Throwable wrappedException) {
		super(msg,wrappedException);
	}
	
	/**
	 * Constructor intended when handling non-exception error scenarios
	 * @param msg
	 */
	public AuthenticationException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructor for convenience when dealing with HTTP=400 error responses
	 * @param response
	 */
	public AuthenticationException(OAuthErrorResponse response) {
		super(response.getError_description());
	}

}

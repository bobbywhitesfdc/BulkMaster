package bobby.sfdc.prototype.oauth.json;

import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;

import com.google.gson.Gson;

/**
 * Json response body for OAuth 2.0 response from Token method
 * 
 * POST /services/oauth2/token HTTP/1.1
 * 
 * Status=200
 * 
 * This class is a Bean suitable for deserialization with Gson
 * 
 * Example response here:
{
"id":"https://login.salesforce.com/id/00DT0000000DpvcMAC/005B0000000guD2IAI"
,"issued_at":"1419271088764"
,"scope":"api refresh_token chatter_api"
,"instance_url":"https://gus.my.salesforce.com"
,"token_type":"Bearer","refresh_token":"5Aep861_OKMvio5gy8xCNsXxybPdupY9fVEZyeVpd60v0G33HWB_h2SfhoGItFoA0HPlzSA2TXiBB3Q1SqXfMxO"
,"signature":"TK2/cXU+xRVOTBeTziRpLW1zkPM0QSWSRhnXVDiQDXc="
,"access_token":"00DT0000000Dpvc!AQYAQM7vSH1lzf7viVjXb8qiJ5gvSGqhnR4hpvoulHeGQAXjcndOF4v7.tIOmzY9LvFi.6WZpVEXkYUv3h.UzK22kSvCCEMv"
}


 * @author bobby.white
 *
 */
public class OAuthTokenSuccessResponse {
	String id;
	String issued_at;
	String scope;
	String instance_url;
	String token_type;
	String signature;
	String access_token;
	private static TextEncryptor _encryptor;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the issued_at
	 */
	public String getIssued_at() {
		return issued_at;
	}
	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @return the instance_url
	 */
	public String getInstance_url() {
		return instance_url;
	}
	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OAuthTokenSuccessResponse [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (issued_at != null) {
			builder.append("issued_at=");
			builder.append(issued_at);
			builder.append(", ");
		}
		if (scope != null) {
			builder.append("scope=");
			builder.append(scope);
			builder.append(", ");
		}
		if (instance_url != null) {
			builder.append("instance_url=");
			builder.append(instance_url);
			builder.append(", ");
		}
		if (token_type != null) {
			builder.append("token_type=");
			builder.append(token_type);
			builder.append(", ");
		}
		if (signature != null) {
			builder.append("signature=");
			builder.append(signature);
			builder.append(", ");
		}
		if (access_token != null) {
			builder.append("access_token=");
			builder.append(access_token);
		}
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * Helper Method to Encrypt this AuthBundle
	 * @return
	 */
	public String encryptAuthToken() {
		String jsonString = new Gson().toJson(this);
		
		return getEncryptor().encrypt(jsonString);
	}
	/**
	 * @return
	 */
	protected TextEncryptor getEncryptor() {
		if (_encryptor==null) {
			BasicTextEncryptor temp = new BasicTextEncryptor();
			temp.setPassword("Digestible");
			_encryptor = temp;
		}
		return _encryptor;
	}
	
	/**
	 * Helper Method to reinstantiate an AuthToken from the encrypted string form.
	 * @param encrypted
	 * @return
	 */
	public static OAuthTokenSuccessResponse decryptAuthToken(String encrypted) {
		String decrypted = new OAuthTokenSuccessResponse().getEncryptor().decrypt(encrypted);
		return new Gson().fromJson(decrypted, OAuthTokenSuccessResponse.class);
	}
}

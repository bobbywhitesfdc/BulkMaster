package bobby.sfdc.prototype.rest;

import com.google.gson.Gson;

public class AbstractJSONBody {

	public AbstractJSONBody() {
		super();
	}

	public String toJson() {
		String retValue = new Gson().toJson(this);
		return retValue;
	}
	@Override
	public String toString() {
		return toJson();
	}

}
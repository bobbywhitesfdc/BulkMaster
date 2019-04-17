package bobby.sfdc.prototype.rest;

import com.google.gson.GsonBuilder;

public class AbstractJSONBody {

	public AbstractJSONBody() {
		super();
	}

	public String toJson() {
		String retValue = new GsonBuilder().setPrettyPrinting().create().toJson(this);
		return retValue;
	}
	@Override
	public String toString() {
		return toJson();
	}

}
package bobby.sfdc.soql;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class Info {
	private final String id;
	private final List<BasicNameValuePair> fields;
	public Info(String id) {
		this.id=id;
		fields = new ArrayList<BasicNameValuePair>();
	}
	public String getId() {
		return id;
	}
	public void add(String field, String value) {
		fields.add(new BasicNameValuePair(field,value));
	}
	public List<BasicNameValuePair> getFields() {
		return fields;
	}
	@Override
	public String toString() {
		final int maxLen = 50;
		StringBuilder builder = new StringBuilder();
		builder.append("Info [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (fields != null) {
			builder.append("fields=");
			builder.append(fields.subList(0, Math.min(fields.size(), maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}

}

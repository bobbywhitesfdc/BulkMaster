package bobby.sfdc.prototype.bulkv1.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
 * JSON Request Structure used to Create a Bulk API V1 Job
 * @author bobby.white
 *
 *	{
		  "operation" : "insert",
		  "object" : "Account",
		  "contentType" : "CSV"
	}
 */
public class CreateV1JobRequest extends AbstractJSONBody {
	public static final String XML_TAG="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String XML_TYPE_TAG="<jobInfo xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n";
	public String operation;
	public String object;
	public String concurrencyMode="Parallel";
	public String contentType="CSV";

	/**
	 * Bulk API V1 expects to receive its arguments in XML format.
	 * @return
	 */
	public String toXML() {
		StringBuffer buffer = new StringBuffer(XML_TAG);
		buffer.append(XML_TYPE_TAG);
		buffer.append("<operation>").append(operation).append("</operation>\n");
		buffer.append("<object>").append(object).append("</object>\n");
		buffer.append("<concurrencyMode>").append(concurrencyMode).append("</concurrencyMode>\n");
		buffer.append("<contentType>").append(contentType).append("</contentType>\n");
		buffer.append("</jobInfo>\n");
		return buffer.toString();
	}
}

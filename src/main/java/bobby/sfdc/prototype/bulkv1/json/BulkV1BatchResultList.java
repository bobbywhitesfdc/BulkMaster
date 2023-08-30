package bobby.sfdc.prototype.bulkv1.json;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

@XmlRootElement(name="result-list")
public class BulkV1BatchResultList extends AbstractJSONBody {
	@XmlElement(name="result")
	public List<String> results = new ArrayList<String>();
}

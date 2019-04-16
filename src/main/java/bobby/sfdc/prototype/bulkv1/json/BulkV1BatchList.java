package bobby.sfdc.prototype.bulkv1.json;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

@XmlRootElement(name="batchInfoList")
public class BulkV1BatchList extends AbstractJSONBody {
    @XmlElement(name="batchInfo")
	public List<BulkV1BatchInfo> batches=new ArrayList<BulkV1BatchInfo>();
}

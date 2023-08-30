/**
 * Bulk API V1 Package responses need to be XML parsable. 
 * Defining the XmlSchema namespace here to allow JaxB parsing
 */
@XmlSchema(namespace="http://www.force.com/2009/06/asyncapi/dataload",
elementFormDefault = XmlNsForm.QUALIFIED)  

package bobby.sfdc.prototype.bulkv1.json;
import jakarta.xml.bind.annotation.*;

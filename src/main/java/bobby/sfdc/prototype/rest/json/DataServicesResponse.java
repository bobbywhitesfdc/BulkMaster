package bobby.sfdc.prototype.rest.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
 * RESOURCE 
 * /services/data/vXX.0
 * Generic Data Services Endpoint Response
 * { 
    "sobjects" : "/services/data/v26.0/sobjects", 
    "licensing" : "/services/data/v26.0/licensing", 
    "connect" : "/services/data/v26.0/connect", 
    "search" : "/services/data/v26.0/search", 
    "query" : "/services/data/v26.0/query", 
    "tooling" : "/services/data/v26.0/tooling", 
    "chatter" : "/services/data/v26.0/chatter", 
    "recent" : "/services/data/v26.0/recent" 
   }
 * @author bobby.white
 *
 */
public class DataServicesResponse extends AbstractJSONBody {
    public String sobjects;
    public String licensing;
    public String connect;
    public String search;  
    public String query;
    public String tooling;
    public String chatter;
    public String recent;	
}

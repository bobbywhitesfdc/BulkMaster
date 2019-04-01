/**
 * 
 */
package bobby.sfdc.util;

/**
 * Take raw Json that is well formed and print it neatly
 * 
 * @author bobby.white
 *
 */
public class JsonPrinter {
    public String makePretty(String rawJson) {
    	StringBuilder pretty = new StringBuilder();
    	// when we hit an open brace, we've started a new element, newline, increase indent
    	// when we hit an open bracket, we've begun an array, newline, increase indent
    	// when we hit a close bracket, we've ended an array, newline, decrement indent
    	// when we hit a close brace, we've ended an element, newline, decrement indent 
    	return pretty.toString();
    }
}

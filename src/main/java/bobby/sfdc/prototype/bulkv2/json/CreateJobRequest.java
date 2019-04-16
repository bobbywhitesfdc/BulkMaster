package bobby.sfdc.prototype.bulkv2.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;
public class CreateJobRequest extends AbstractJSONBody {
    public String columnDelimiter;
    public String contentType;
    public String externalIdFieldName;
    public String lineEnding;
    public String object;
	public String operation;
}

/**
columnDelimiter	ColumnDelimiterEnum	The column delimiter used for CSV job data. The default value is COMMA. Valid values are:
BACKQUOTE—backquote character (`)
CARET—caret character (^)
COMMA—comma character (,) which is the default delimiter
PIPE—pipe character (|)
SEMICOLON—semicolon character (;)
TAB—tab character
Optional

contentType	ContentType	The content type for the job. The only valid value (and the default) is CSV.	Optional

externalIdFieldName	string	The external ID field in the object being updated. Only needed for upsert operations. Field values must also exist in CSV job data.	Required for upsert operations

lineEnding	LineEndingEnum	The line ending used for CSV job data, marking the end of a data row. The default is LF. Valid values are:
LF—linefeed character
CRLF—carriage return character followed by a linefeed character
Optional

object	string	The object type for the data being processed. Use only a single object type per job.	Required

operation	OperationEnum	The processing operation for the job. Valid values are:
insert
delete
update
upsert
**/
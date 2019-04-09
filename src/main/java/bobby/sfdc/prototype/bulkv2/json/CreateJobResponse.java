package bobby.sfdc.prototype.bulkv2.json;

public class CreateJobResponse {
	public String apiVersion;
	public String columnDelimiter;
	public String concurrencyMode;
	public String contentType;
	public String contentUrl;
	public String createdById;
	public String createdDate;
	public String externalIdFieldName;
	public String id;
	public String jobType;
	public String lineEnding;
	public String state;
	public String systemModstamp;	
}

/**
Property	Type	Description
apiVersion	string	The API version that the job was created in.

columnDelimiter	ColumnDelimiterEnum	The column delimiter used for CSV job data. Values include:
BACKQUOTE—backquote character (`)
CARET—caret character (^)
COMMA—comma character (,) which is the default delimiter
PIPE—pipe character (|)
SEMICOLON—semicolon character (;)
TAB—tab character

concurrencyMode	ConcurrencyModeEnum	For future use. How the request was processed. Currently only parallel mode is supported. (When other modes are added, the mode will be chosen automatically by the API and will not be user configurable.)

contentType	ContentType	The format of the data being processed. Only CSV is supported.
contentUrl	URL	The URL to use for Upload Job Data requests for this job. Only valid if the job is in Open state.

createdById	string	The ID of the user who created the job.
createdDate	dateTime	The date and time in the UTC time zone when the job was created.

externalIdFieldName	string	The name of the external ID field for an upsert.
id	string	Unique ID for this job.
jobType	JobTypeEnum	The job’s type. Values include:
BigObjectIngest—BigObjects job
Classic—Bulk API 1.0 job
V2Ingest—Bulk API 2.0 job

lineEnding	LineEndingEnum	The line ending used for CSV job data. Values include:
LF—linefeed character
CRLF—carriage return character followed by a linefeed character
object	string	The object type for the data being processed.
operation		The processing operation for the job. Values include:
insert
delete
update
upsert

state	JobStateEnum	The current state of processing for the job. Values include:
Open—The job has been created, and data can be added to the job.
UploadComplete—No new data can be added to this job. You can’t edit or save a closed job.
Aborted—The job has been aborted. You can abort a job if you created it or if you have the “Manage Data Integrations” permission.
JobComplete—The job was processed by Salesforce.
Failed—Some records in the job failed. Job data that was successfully processed isn’t rolled back.
systemModstamp	dateTime	Date and time in the UTC time zone when the job finished.
**/
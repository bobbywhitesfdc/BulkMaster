# BulkMaster
Bulk Loading Utility for Salesforce
<p>This is a Mavenized Java 8 project, so building it is simple:</p>
<p><code>mvn clean package</code></p>

# Run time
It's packaged as an executable jar, run it from the commandline as follows
<p><code>java -jar target/BulkMaster-1.0.jar</code></p>

<p>It will emit a Command Syntax statement listing the parameters and flags</p>
<code>
<p>userid and password are required parameters!:</p>
<p>  myuser@example.com mypassword</p>
<p>Syntax:  BulkMaster <username> <password> [loginURL] [FLAGS]</p>
<br/>
<p>If omitted, default Login URL=https://login.salesforce.com</p>
<br/>
<p>Flags:</p>
<p>-l List Jobs</p>
<p>-i Insert Records</p>
<p>-u Upsert Records</p>
<p>-d Delete Records</p>
<p>-purge One step command that queries and purges</p>
<p>-s Get Job Status</p>
<p>-c Close Job</p>
<p>-a Abort Job</p>
<p>-r Get Job results</p>
<p>-j Hex ID of the Job</p>
<p>-f Input filename</p>
<p>-D Output Directory</p>
<p>-x External ID fieldname for Upsert</p>
<p>-o Object name for Insert, Update, or Delete</p>
<p>-p Poll for results - interval in seconds</p>
<p>-q SOQL Query string</p>
<p>-pk Enable PK Chunking for Large Queries</p>
</code>
# Example One-step Purge in a Sandbox
<p><code>java -jar target/BulkMaster-1.0.jar myuser@myorg.com MyPassword https://test.salesforce.com -o Lead -purge "Select * From Lead Where status='Converted'" - D ./output -p 60</code></p>


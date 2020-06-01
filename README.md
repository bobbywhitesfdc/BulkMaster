# BulkMaster
Bulk Loading Utility for Salesforce
<p>This is a Mavenized Java 8 project, so building it is simple:</p>
<p><code>mvn clean package</code></p>

# Run time
It's packaged as an executable jar, run it from the commandline as follows
<p><code>java -jar target/BulkMaster-1.0-jar-with-dependencies.jar</code></p>

<p>It will emit a Command Syntax statement listing the parameters and flags</p>
<code>
<p>userid and password are required parameters!:</p>
<p>  myuser@example.com mypassword</p>
<p>Syntax:  BulkMaster [{username} {password}] [loginURL] [FLAGS]</p>
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
<p>-mx Max records per job</p>
<p>-authtoken Auth Token</p>
<p>-instanceurl Instance URL used for API calls</p>
<p>-loginurl Login URL if using Username Password flow</p>
<p>-un User name</p>
<p>-pwd Password<p>
<p>-apiversion API Version xx.0</p>

</code>
# Example One-step Purge in a Sandbox
<p><code>java -jar target/BulkMaster-1.0-jar-with-dependencies.jar myuser@myorg.com MyPassword https://test.salesforce.com -o Lead -purge "Select * From Lead Where status='Converted'" - D ./output -p 60</code></p>
# Example List Jobs
<p><code>java -jar target/BulkMaster-1.0-jar-with-dependencies.jar myuser@myorg.com MyPassword https://test.salesforce.com -l</code></p>
# Example Execute a Query -- Poll for results every 10 seconds, download the results to ./output directory
<p><code>java -jar target/BulkMaster-1.0-jar-with-dependencies.jar myuser@myorg.com MyPassword https://test.salesforce.com -o Lead -q "Select * From Lead Where status='New'" - D ./output -p 10</code></p>
# Example Poll for Results on a running job -- Poll for results every 10 seconds, download results to ./output directory
<p><code>java -jar target/BulkMaster-1.0-jar-with-dependencies.jar myuser@myorg.com MyPassword https://test.salesforce.com  -r -j 7500U000002UO1V  - D ./output -p 10</code></p>
# Example One-step "Uber Upsert" a collection of files for a single object
<p><code>java -jar target/BulkMaster-1.0-jar-with-dependencies.jar myuser@myorg.com MyPassword https://test.salesforce.com -o Account -x MyExternalId__c -uu ./input/AccountData - D ./output/AccountResults -p 60</code></p>


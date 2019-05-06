#!/bin/bash
if [ $# -lt 2 ]
then
	echo Username and Password parameters are required
	exit 1
fi

if [ -z $3 ]
then
	echo "Default to Standard Sandbox URL"
	URL=https://test.salesforce.com
else
	URL=$3
	echo URL overriden as $URL
fi

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o FinServ__FinancialAccount__c -p 60 -purge "Select Id From FinServ__FinancialAccount__c Where LastModifiedById IN (Select Id From User Where Name IN ('CDI API','Lem Revilla','QA Automation User'))" -D ../output/sit/purge-fa
RESULT=$?
if [ $RESULT == 0 ]
then
	echo Purge Financial Accounts succeeded
else 
	echo Purge Financial Accounts failed exit code: $RESULT
	exit $RESULT
fi

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o Account -p 60 -purge "Select Id From Account Where LastModifiedById IN (Select Id From User Where Name IN ('CDI API','Lem Revilla','QA Automation User'))" -D ../output/sit/purge-account

RESULT=$?
if [ $RESULT == 0 ] 
then
	echo Purge Accounts succeeded
else
	echo Purge  Accounts failed exit code: $RESULT
	exit $RESULT
fi
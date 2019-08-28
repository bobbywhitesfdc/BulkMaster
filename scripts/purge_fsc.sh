#!/bin/bash
if [ $# -lt 2 ]
then
	echo Username and Password parameters are required
	exit 1
fi

if [ -z $3 ]
then
	echo "Default to output base location"
	OUTPUTDIR=../output/
else
	OUTPUTDIR=$3
	echo OUTPUT dir overridden as $OUTPUTDIR
fi

if [ -d $OUTPUTDIR ]
then
	echo Output directory must not exist, please specify a new name for each iteration
	exit 2
fi

if [ -z $4 ]
then
	echo "Default to Standard Sandbox URL"
	URL=https://test.salesforce.com
else
	URL=$4
	echo URL overriden as $URL
fi

STEPDIR=$OUTPUTDIR/purge-aar
mkdir -p $STEPDIR

if [ "$(ls -A $STEPDIR)" ]; then
     echo "Output directory must be empty" $STEPDIR
     exit 1
fi


WHERE="Where CreatedById IN (Select Id From User Where Name IN ('CDI API','Lem Revilla','QA Automation User'))"
echo "Applying filter to all queries:" $WHERE

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o FinServ__AccountAccountRelation__c -p 60 -purge "Select Id From FinServ__AccountAccountRelation__c  $WHERE order by FinServ__Account__c, FinServ__RelatedAccount__c" -D $STEPDIR
RESULT=$?
if [ $RESULT == 0 ]
then
	echo Purge AAR succeeded
else 
	echo Purge AAR failed exit code: $RESULT
	exit $RESULT
fi


STEPDIR=$OUTPUTDIR/purge-leads
mkdir -p $STEPDIR
if [ "$(ls -A $STEPDIR)" ]; then
     echo "Output directory must be empty" $STEPDIR
     exit 1
fi

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o Lead -p 60 -purge "Select Id From Lead $WHERE" -D $STEPDIR
RESULT=$?
if [ $RESULT == 0 ]
then
	echo Purge Lead succeeded
else 
	echo Purge Lead failed exit code: $RESULT
	exit $RESULT
fi

STEPDIR=$OUTPUTDIR/purge-opps
mkdir -p $STEPDIR
if [ "$(ls -A $STEPDIR)" ]; then
     echo "Output directory must be empty" $STEPDIR
     exit 1
fi

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o Opportunity -p 60 -purge "Select Id From Opportunity $WHERE" -D $STEPDIR
RESULT=$?
if [ $RESULT == 0 ]
then
	echo Purge Opportunities succeeded
else 
	echo Purge Opportunities failed exit code: $RESULT
	exit $RESULT
fi

STEPDIR=$OUTPUTDIR/purge-fa
mkdir -p $STEPDIR
if [ "$(ls -A $STEPDIR)" ]; then
     echo "Output directory must be empty" $STEPDIR
     exit 1
fi

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o FinServ__FinancialAccount__c -p 60 -purge "Select Id From FinServ__FinancialAccount__c $WHERE" -D $STEPDIR
RESULT=$?
if [ $RESULT == 0 ]
then
	echo Purge Financial Accounts succeeded
else 
	echo Purge Financial Accounts failed exit code: $RESULT
	exit $RESULT
fi

STEPDIR=$OUTPUTDIR/purge-account
mkdir -p $STEPDIR
if [ "$(ls -A $STEPDIR)" ]; then
     echo "Output directory must be empty" $STEPDIR
     exit 1
fi

java -jar ../target/BulkMaster-1.0-jar-with-dependencies.jar $1 $2 $URL -o Account -p 60 -purge "Select Id From Account $WHERE" -D $STEPDIR

RESULT=$?
if [ $RESULT == 0 ] 
then
	echo Purge Accounts succeeded
else
	echo Purge  Accounts failed exit code: $RESULT
	exit $RESULT
fi
Support Tools App for Service Graph Connectors

We receive cases from customer with below scenarios:

-	Data is loaded into staging table, but the records are not inserted/updated in target CMDB tables
-	Data for few important fields like “assigned_to”, “manufacturer_id” etc are loaded into Staging table, but it is not populated in target tables.
-	The data loaded from source application is causing flipping issues in target CMDB tables.

Sample cases we received :

1.	CS6843936 - Handheld Device is not getting populated "Assigned To" field from cmdb table to alm_hardware table
2.	CS6891155 - Asset record is not creating for mobile device (intune)
3.	CS6938837 - CIs are not creating in servicenow from SCCM integration- Missing CI/Incorrect data

Procedure we have been following so far:

1.	Identify the Import set record to which the row in question is loaded.
2.	Create single row import set, an Import set record that contains a given single row.
3.	Reprocess the Import set created in step 2.
4.	Simulate the payload for the import set through IRE simulation to identify the reason for the issue

Challenges with Steps 2 and 3 above :

Create single row import set, an Import set record that contains a given single row.

Many Import Sets contain thousands or tens of thousands of records, which generate too much output with IRE debugging enabled. The best approach is to take an existing Import Set that triggers the IRE issue, pick out a small number (max 5) records that demonstrate the issue, and have them reprocessed in a separate Import Set (these steps should be done by Integrations)
o	Step #0a: Open the Import Set that triggers the IRE issue. In the context (grey bar) menu click "Insert and Stay". We will be using this Import Set for reprocessing to obtain the specific IRE payload.
o	Step #0b: Now go to the list view of the relevant Import Set Staging table, (for example if the issue is with Computer record imports in SG SCCM it would be https://<instance-name>.service-now.com/sn_sccm_integrate_sccm_2019_computer_id.do) and open the staging record which experiences the issue.
o	Step #0c: Populate the "Set" field with the newly created Import Set number (from Step #0-1), then in the context (grey bar) menu click "Insert and Stay".

Note: You may need to modify the form layout to expose the "Set" field.
Reprocess the Import set created in step 2, capture the input payload for IRE which is generated by RTE mapping.

KB : https://support.servicenow.com/kb?id=kb_article_view&sysparm_article=KB0750382

The above procedure is time taking and need basic understanding of how IRE payload is generated, specifically if one is new to CMDB and Service Graph Connectors.

Solution :

We have come up with automation that can help perform above steps easily, with minimum or no prior knowledge of Identification Engine.

You can access the source code for the java application and the shell script in the github repos below :

https://github.com/phanivallur/Snow-Automate.git

https://github.com/phanivallur/snow_tool.git


How to use it?

1.	Extract the files from the zip to your local computer.
2.	Copy the file bssh.sh and sgc.jar to any path of your choice.
3.	Open terminal, on the folder used in step 2. 
4.	Run the script as below :

                             ./bssh.sh -i empphanivc -s <sys_id of required import set row>

The script takes two arguments as input, which is below :

-	Instance name in which we want to troubleshoot the import set record.
-	Sys_id of import set row, that is in question.

Here is what it does :

1.	The script creates a single row import set for which we want to investigate the issue.
2.	Runs the Transformation of the row. 
3.	With prior enablement of debug properties in the instance, the transformation will populate the detailed stats of the transformation in cmdb_ire_output_target_item table.
4.	The script creates a URL to the list view of the above table that gives detailed how the transformation happens and the data is being populated by IRE.

I have tested the script in one of my lab instances where SG-SCCM integration is configured.

Instance URL: htps://empphanivc.service-now.com

Sample execution :

Command : ./bssh.sh -i empphanivc -s 75c4741387eb351094a298a73cbb35e9

Where empphanivc is the instance, and ‘75c4741387eb351094a298a73cbb35e9’ is the input sys_id of import set row

Result :


<img width="452" alt="image" src="https://github.com/phanivallur/snow_tool/assets/33691241/3b6648ba-2ece-4214-b470-c18ef6bc5363">

 
Output:
Line 1 contains URL for Scripted REST resource which does the job of creating single row import set

Line 2 contains the URL for the list view of cmdb_ire_output_target_item table that stores the result of processing single row.

Last section shows the listing of :

1.	Sys_id of import set row that is passed as input to the tool.
2.	Expected target table : The table in which the target record is supposed to be populated
3.	Actual target table : The table in which the target record is actually populated
4.	Operation : indicates the operation performed while populating the specific target table
5.	Target Id : indicates the sys_id of the target cmdb_ci record populated in the target table

Note : Before executing the script, below system properties should be enabled in the instance.


Name : glide.identification_engine.ire_message_listener_output_stats_enabled
Type : true | false
Value : true

Name : glide.cmdb.logger.source.identification_engine
Type : string
Value : info,warn,error,debug,debugVerbose

Enable “verbose” option on Robust Import set transformer records.
![image](https://github.com/phanivallur/snow_tool/assets/33691241/b90169b3-45ba-4930-9cc2-bedef2b0cc51)

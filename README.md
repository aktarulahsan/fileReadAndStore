# fileReadAndStore
This is a Spring boot project, for file read, store and creating separate file 
java 17
spring-boot 2.7.10


this is a restful api project,   1M-customers.txt already store in the resources-> file folder which needs to read. 
first, create a schema in MySQL with a test or set schema name, username and password on application-dev.properties.

 all tables are created when the project will be run.
 when calling this API 
 http://localhost:9014/lead-api/category/fileCheck  
 1M-customers.txt file read and check first duplication agent phone number and then check phone and mail validation. 
 after checking the validation of valid Customers and Invalid Customers store different tables. 
 after storing get all valid data and create a file. those files in a batch of files
including 10k customers with each file.
all file store on project label upload folder. 


here are another 3 api have . 
 http://localhost:9014/lead-api/category/validCustomerSlab    // for including 10k customers with each file.
 http://localhost:9014/lead-api/category/inValidCustomerSlab  // store InvalidCustomer 
 http://localhost:9014/lead-api/category/validCustomer        // for download all validCustomer in one file 

Data process time was  Duration is: 89437ms, Total file line count: 1231081, Line count time: 89439ms

# DiscountCalculator

## A Spring Boot Application for calculating the Price Discounts of Products from excels.

The files are read from the resource folder("src/main/resources/excels") using the WatchService API.
 
Whenever a new file (file name should start with Product**.xlsx or Discount**.xlsx) is placed in the folder, the application reads the content from the file and saves the Data in the table Product or Discount respectively.

Once the data got saved, discount price of the Products will be calculated. This functionality will also be triggered from the cron job scheduled at 9AM everyday on the time zone 'CET'.

The discount price result will be saved in the Product table. 

An XML file with the Discount Price will be created in the resource folder("src/main/resources/xml").

## Validation

The file's MIME content is getting checked for any invalid file type's extension change.

On application startUp, the app checks for any pending files which are not yet processed.


## DataBase
 MySql
 
 DB Name: calculator
 
 Active profile: dev(default)
 
 ## Application SetUp
 
 Clone/download the project and build Maven to download the dependencies.
 
 Create a MqSql Database with name : calculator
 
 The excel files Product.xlsx and Discount.xlsx are already available in the resource folder "src/main/resources/excels".
 Create a copy of the files to do debugging/ remove the files and create new one.

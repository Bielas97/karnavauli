# Karnavauli

This project was built for company's purposes and should be used only for this. Inappropriate usages shoud be immediately reported to the owner.
## Description

The project purpose is to create a stable system on which users can manage ticket selling for prom on University of Warsaw called "Karnavauli" - http://karnavauli.pl/new/


## Detailed Description

Administrator creates Tickets that can be sold for several universities, VIPs or other customers, users for several Universities which then can sell tickets to the customers. There is a map of tables and seats depicted which can be assigned by 
administrator to users. The user can sell only the seats that are assigned to them. 
Administrator can change privilages of the users, delete them and see all of them. Users cannot do such things Each user has a particular number of tickets that can sell.

## Structure and Deployment

The application was deployed on Amazon Web Services in 12/2018 during the dates when the tickets on University of Warsaw have been selling.
App creates 4 tables on MySql Database - Customer, KvTable, Ticket, User.

## Running

In order to run it has to be configured spring boot and in application.properites there have to be provided data such as: "spring.datasource.url", "spring.datasource.username", "spring.datasource.password".
Data provided currently is the data used for Amazon Web Services to the account which has been already deleted so it is urgent to change it.

## Technology Stack

Java 1.8, springboot, AWS, MySql, Maven

## Copyright

It is strictly forbidden to use this application for markets purposes. Any of this action should be reported immediately

## Author

Jakub Bielawski for Board of European Students of Technology (BEST)
jakub.bielawski@wawasoft.com
# Show Booking Java Application

## Overview
Java application for the use case to book a show. <br> 
The program will run in command line and take input from command line.<br> 
The program would setup available seats per show, allow buyers to select 1 or more available seats and buy/cancel tickets.<br> 
The application shall cater to the below 2 types of users & their requirements – (1) Admin and (2) Buyer<br>

**Admin** – The users should be able to Setup and view the list of shows and seat allocations.<br>
Commands to be implemented for Admin :<br>
1.  Setup  <Show Number> <Number of Rows> <Number of seats per row>  <Cancellation window in minutes>  <br>
(To setup the number of seats per show)
2. View <Show Number>    <br>
(To display Show Number, Ticket#, Buyer Phone#, Seat Numbers allocated to the buyer)<br><br>

**Buyer** – The users should be able retrieve list of available seats for a show, select 1 or more seats , buy and cancel tickets. <br>
Commands to be implemented for Buyer :<br><br>

1. Availability  <Show Number>   <br>
(To list all available seat numbers for a show. E,g A1, F4 etc)<br>
2. Book <Show Number> <Phone#> <Comma separated list of seats> <br>
(To book a ticket. This must generate a unique ticket # and display)<br>
3. Cancel  <Ticket#>  <Phone#><br>
(To cancel a ticket. See constraints in the section below)<br><br>

**Additional Option added** <br>
Assuming that single command line is created, user will have options to switch from Admin to Buyer user type.
User will have the following option:
1. Select usertype 1 for 'Admin', 2 for Buyer (type 'exit' to quit): <br>
2. Enter a command (type 'exit' to quit, type 0 to select user type):  once user is in Buyer or admin mode<br>

## Tools

Before you can run this service, you must set up the following tools:
- Eclipse (Preferred)
- Java 17
- Git

## Running the application
run this class ShowRervationApplication

Note: 
Unit test are not complete

To Run the application

1)Need to have mongodb running on :

Host: 127.0.0.1 Port: 27017

2)I have IntelliJ IDEA and Gradle installed on the system

In IntelliJ Open the project

In terminal, use below commands to run the application :
 1)gradle build (build the project)
 2)java -jar build/libs/myRetail-0.0.1-SNAPSHOT.jar (run the project)
 or gradle bootRun (run the project)

//GET endpoint Type the below URl in the Web Browser: http://localhost:8080/products/{id}

example: http://localhost:8080/products/13860428

To test Update: In postman: Send a PUT request to http://localhost:8080/products/{id}

Example:- http://localhost:8080/products/13860428

with json body:
{
   "value": 70,
    "currency_code": "USD"
}


3) Integration test in folder int  do not require MongoDB running as they use the Embedded Mongo framework
Run command: gradle integrationTest

4)Unit TestCases in test folder
Run command: gradle test

To run both in one command:
gradle test integrationTest
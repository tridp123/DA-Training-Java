# DA-Training-Java
https://docs.spring.io/spring-boot/docs/2.0.0.M4/reference/htmlsingle/

Create a web application to read data from Cassandra Database and insert into Postgres DB.
Workflow:

STAGE 1: Build web app structure (This ticket)
TECHNOLOGIES
- SpringBoot: create standalone app
- Maven: Build project
- Spring Data:
+ to store data in a relational database (Postgres)
+ to retrieve data from NoSQL Cassandra
- Using liquibase to create database Postgres structure and initial needed data

ESTIMATION
- Investigation:
+ SpringBoot: 2 days
+ Spring JPA: 2 days
+ Maven: 1 day
+ Cassandra and install: 1 days
- Implementation: 4 days
TOTAL: 10 working days.

IMPLEMENT
- Organize code to reuseable for unit test, such as API request mapping url, enums values for type object..
- Three layer (controller, service, repository/dao).
- Using LAZY strategy for object mapping.
- Using Spring Repository to implement DAO class.
- For controller, convert a specific model to a DTO object before return data to client.
- Declare a interface for each services class, using the interface in controller instead of a specific service class.
- Declare a base service to manage properties which exists in all sub services.
- Declare a base controller to catch all exception-(@ExceptionHandler), then throw a specific exception( NOT FOUND DATA EXCEPTION-given param not exist in the database, INTERNAL EXCEPTION-exception that occurred somewhere in the code, BAD REQUEST-given param is not matched to controller, FORBIDDEN-not authentication) and corresponding http status code to the client.
- Declare LogUtil for debug, error methods, create a logger for each specific controller, service then using LogUtil by pass the specific logger instance and/or message log/message error log(ex.getErrorMessage) into the util.
- For datetime value, using Joda time to get the value with UTC timezone, declare a DateTimeUtil to reuseable and minimum changing when needed.
- For each method, log debug at the begin and the end of the method.
- For debug API at runtime, find out a way to log debug message and/or error message to client by store the message in response header(ex log-detail-trace header).

NOTE
- At the beginning of implementing, quickly define skeleton project, classes, interfaces, utils, the way organize packages...This will be reviewed after first two days.

STAGE 2: Apply Spring boot security to web app (next ticket)
- Requirements:
+ Secure at APIs level.
- Estimation: 3 days

STAGE 3: Apply logging to web app
- Apply ELK (Elasticsearch, logstash, Kibana) to web app. This also includes install all things need to do this. (next ticket)
Estimation
- Investigation:
+ Elasticsearch: 1 days
+ logstash: 1 days
+ Kibana: 1 days
+ Set up them: 3 days
- Implementation: 4 days
TOTAL: 10 working days

STAGE 4: Write unit test for web app (next ticket)
- Need to write unit test and integration test
+ Write unit test for APIs, such as end to end from API to store data into DB...
Estimation: 4 days

STAGE 5: Apply QueryDsl to web app. (final ticket)
Estimation: 3 days

If you survive the above steps on time (30 working days = 6 weeks). So...Congratulation!!! you awesome!!!

![Java CI with Maven](https://github.com/lehansun/avazdusevich-speaking-practice/actions/workflows/maven.yml/badge.svg)

# avazdusevich-speaking-practice-app

Demo application for finding native speakers and planning calls with them
to practice the learning language.
+ [Environment setting](#Environment-setting)
+ [Installing](#Installing)
+ [Build project](#Build-project)
+ [Deploy application on Tomcat server](#Deploy-application-on-Tomcat-server)
+ [Available REST endpoints](#Available-REST-endpoints)
  + [customers](#Customers)
  + [requests](#Requests)
  + [personal page](#Personal-page)
## Environment setting
```
install openjdk11
install maven3+
install tomcat
install git
```

## Installing
Select the directory for the project and clone the project from github:
```
$ git clone https://github.com/lehansun/avazdusevich-speaking-practice.git
```

## Build project
Run command in project directory:
```
$ mvn clean install
```

## Deploy application on Tomcat server
Copy:
```
../avazdusevich-speaking-practice/controller/target/speaking-practice.war
```
to tomcat directory:
```
../tomcat/webapps/
```
For shutdown and removing this apps from server remove this files.

Or, you can use GUI at:
```
http://localhost:8080/manager/html
```
and select .war files to deploy.
For shutdown select "stop", for removing "undeploy".

Web-app should be available at:
```
http://localhost:8080/speaking-practice/
```

## Available REST endpoints
When applications run on Tomcat server, the following endpoints are available:

### Customers

##### Get list of customers:
```
GET /customers
```
##### Get a specific customer:
```
GET /customers/id
```

##### Create a new customer:
```
POST /customers
```

##### Update a customer:
```
PATCH /customers/id
```

##### Delete a customer:
```
DELETE /customers/id
```
### Requests

##### Get list of requests:
```
GET /requests
```
##### Get sorted list of requests using type of sorting in the url:
```
GET /requests?sortType=BY_INITIATOR
```
    BY_INITIATOR - sort by username of the customer who initiated the request 
    BY_ACCEPTOR - sort by username of the customer who accept the request 
    BY_START_TIME - sort by scheduled call start time
    BY_END_TIME - sort by scheduled call end time

##### Get a specific request
```
GET /requests/id
```
##### Create a new request
```
POST /requests
```
##### Update a request
```
PATCH /requests/id
```
##### Delete a request
```
DELETE /requests/id
```

### Personal page

##### Get authenticated customer info:
```
GET /me 
```

##### Get list of requests initiated by authenticated customer:
```
GET /me/requests 
```
###### Parameters
dateFrom (optional)
```dtd
Specify the start of period for searching the list of requests.
    Pattern: "yyyy-MM-dd"
    Example: "2021-03-21"
    Example requests: "http://localhost:8080/speaking-practice/me/requests?dateFrom=2021-08-01"
```
dateTo (optional)
```dtd
Specify the end of period for searching the list of requests.
        
    Pattern: "yyyy-MM-dd"
    Example: "2021-08-28"
    Example requests: "http://localhost:8080/speaking-practice/me/requests?dateFrom=2021-08-01&dateTo=2021-08-13"
```
accepted (optional)
```dtd
Specify which requests should be included in the resulting list: accepted, not accepted or both.

    - the accepted requests will be included when set to true.
    - the not accepted requests will be included when set to false.
    - both (accepted and not accepted) requests will be included when parameter not specified.

    Example: "true"
    Example requests: "http://localhost:8080/speaking-practice/me/requests?accepted=false"
```

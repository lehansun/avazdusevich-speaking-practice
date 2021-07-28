![Java CI with Maven](https://github.com/lehansun/avazdusevich-speaking-practice/actions/workflows/maven.yml/badge.svg)

# avazdusevich-speaking-practice-app

Demo application for finding native speakers and planning calls with them
to practice the learning language.
+ [Environment setting](#Environment-setting)
+ [Installing](#Installing)
+ [Build project](#Build-project)
+ [Deploy application on Tomcat server](#Deploy-application-on-Tomcat-server)

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
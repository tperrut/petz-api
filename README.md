# Petz-API

## [!Build Status(https://travis-ci.com/tperrut/petz-api.svg?branch=master&status=passed)]

Desafio PETZ


This project aims to create a **API Rest** to control and generation the to Entitys (Cliente and Pet), using technology [Java 8](http://java.com). It was developed using [Spring Boot](http://projects.spring.io/spring-boot/), [Travis.ci](https://travis-ci.org) for build automation, [Swagger](http://swagger.com) for documentation, and finally Automatic Deploy on [Heroku](https://dashboard.heroku.com).

>The system has the following endpoint
### CLiente e Pet	
 |VERB  | END_POINT      |
 |:---  |  :---          |
 | POST | createCliente  |  
 | POST | createPet      |   
 | GET  | listClientes   |   
 | GET  | listPets       |   
 | GET  | clienteByID    |
 | GET  | petByID        |   
 | GET  | clienteByNome  |
 | GET  | petByNome      |
 | PUT  | alterarCliente |
 | PUT  | alterarPet     |
 |DELETE| excluirCliente |
 |DELETE| excluirPet     |
--------


    

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org) - 
- IDE or editor of your choice

## Getting started

To start this web application just follow these steps:
	
   Build the project no Maven:

	```shell
	 $ ./mvnw clean package spring-boot:run 			
	```  		
	
   Build the project via Maven:

    $ mvn clean install

   Start the application:
        In your IDE invoke the class method Application#main to start the server , or
        From command line execute:

    $ java -jar target/petz-0.0.1-SNAPSHOT.jar

   Browse to the application root for API documentation:

    http://localhost:8080/


## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `/com/petz/api/PetzApplication.java` class from your IDE.



Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Tests 

Use maven goal 'test'

```shell
mvn test
```
## Heroku
Url to access API on Heroku  https://desafio-petz-api.herokuapp.com/swagger-ui.htm
 
## Config Security
The Basic authentication was implemented with Spring Security.

    Two users have been created with their permissions.

> The following Http Verbs (PUT, DELETE) are protected with permission only for the Administrator Profile. Just like the End Point: "rest/clientes" e "rest/pets"

> The End Point "/" must be authenticated with the default user profile

 ***1. Default User***

| userName  |  "user"|
|--|--|
| password | "123" |
| Roles |  "USER"   |


***2. Administrator User***

| userName  |  "admin"|
|--|--|
| password | "123" |
| Roles |  "USER" , "ADMIN"  |




## Documentation

We used the [Swagger](http://swagger.com) library .
Url to access API Endpoint documentation:	`http://server/swagger-ui.html`

## Information

- Spring boot automatically provides an embedded Tomcat server and a persistence layer based on Hibernate (as JPA provider).

- All REST endpoints *can be tested locally with the Swagger* UI frontend:


## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.

 

## Introduction

This project is a Demo-Backend of how to use Spring-Security[[1]](https://spring.io/projects/spring-security) in combination with JSON Web Token (JWT) 
and MongoDB to perform authentication and authorization of a Rest-Api. 

### JSON Web Token
JWT defines a compact and self-contained way 
for securely transmitting information between parties as a JSON object. 
This information can be verified and trusted because it is digitally signed. 
JWTs can be signed using a secret (with the **HMAC** algorithm) or a public/private key pair 
using **RSA** or **ECDSA** [[2]](https://jwt.io).
 
## Prerequisites  
+ Java 11  
+ Maven
+ Docker  
    + docker-compose

## Getting Started  
#### Run the Application
1. Checkout the project.
    ```
    $ git clone https://github.com/rw026/spring-boot-jwt.git
    ```
2. Change directory into install/local and fire up MongoDB with docker-compose.
    ```
    $ cd spring-boot-jwt/install/local  
    $ docker-compose up -d
    ```
3. Change the directory back to the project root and run the application
    ```
    $ cd spring-boot-jwt
    $ mvn install
    $ mvn spring-boot:run
    ```
#### Making Request
1. Register a new user.
    ```text
    $  curl -X POST http://localhost:8080/register  -H "Content-Type: application/json" -d '{"username":"user1", "password":"SeCuRe"}'
    ```
2. Login with the previous created User credentials and show the Response Header to get the JWT.
    ```text
    $ curl -i -X POST http://localhost:8080/login  -H "application/x-www-form-urlencoded" -d "username=user1&password=SeCuRe"
    ```
3. Make Requests to protected endpoints. Therefore include the JSON Web Token in the **Authorization** 
header of your request. The JWT can be extracted from the previous Response Header.   

    3.1. Access authorized endpoint.
    ```text
    $ curl -X GET http://localhost:8080/api/user -H "Authorization: <enter_token>"
    ```
    3.2. Try to access unauthorized endpoint. 
    ```text
    $ curl -X GET http://localhost:8080/api/admin -H "Authorization: <enter_token>"
    ```
   The Admin endpoint can only be accessed if the user has the **ROLE_ADMIN**.
   

## References  
[1] https://spring.io/projects/spring-security  
[2] https://jwt.io

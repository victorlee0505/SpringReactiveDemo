# Welcome to Spring boot Demo!

This project default to use JDK 17.


## following dependencies are picked from Spring initialirz

- Spring Boot DevTools (auto reboot app on code change)

- Lombok (boiler plate code helper)

- Spring Reactive Web (REST API)

- Spring Data R2DBC (Reactive repository, replace JPA)

- H2 Database (in-memory DB for POC)

- Spring Boot Actuator (System monitor tool)

## dependency add-on

- spring-boot-starter-log4j2 (and exclude spring-boot-starter-logging then config log4j2.xml)

- springdoc-openapi-starter-webflux-ui (swagger UI)

- org.apache.commons commons-lang3

- org.openapitools
    - openapi-generator
    - jackson-databind-nullable
    - openapi-generator-maven-plugin

- org.mapstruct (Mapper Utils)
    - mapstruct

- org.javers (Compare/Audit Utils)
    - javers-core


## H2 Database

please use DBeaver to connect to the DB.

|Field           |Value                          |
|----------------|-------------------------------|
|Driver          |H2                            |
|Host            |locahost                       |
|Port            |9092                           |
|Database/Schema |mem:testdb                     |
|Username        |sa                             |
|Password        | `leave blank`                 |

## Swagger UI

http://localhost:8080/swagger-ui.html
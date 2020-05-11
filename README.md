# Gabiri

Gabiri is a hiring management software. It is currently in pre-alpha.
Gabiri is ancient Sumerian for "mountain".

## Prerequisites

To run this application, you will need MySQL running on your computer.

### Getting Started

 Simply change the spring.datasource.username and spring.datasource.password settings in the application.properties file to the username and password of a MySQL user with schema-creation privileges on your server. If a schema does not already exist for this application, it will create one automatically.

Once you connect to Gabiri through your browser, you will be prompted to give a username and password. The defaults are “user” and “pass”, though you can change this in the application.properties file as well. Creation of new user accounts is a planned feature but has not yet been implemented yet.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [MySQL](https://www.mysql.com/) - The database system used
* [ThymeLeaf](https://www.thymeleaf.org/) - a template engine for the view layer.

## Acknowledgments

* I'd like to thank my wonderful family, who helped me test this program, and who made this project possible at all by loving me into existence.

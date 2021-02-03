# budgie

Budgie is a simple web application based on Spring Boot develop to support my personal budget management workflow.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

In order to build and run the application successfully, you need to have the following components installed and configured in your environment:

|Component|Description|Version|
|---------|-----------|---------------|
|JDK|Java development kit to build and run the application|8 or greater|
|Apache Maven|Dependency management and build process.|3.6.3|
|PostgreSQL Database|Application database.|11|

You can clone the application using:

```bash
git clone 
```

Build the project using the following command from the application root folder:

```bash
mvn clean install
```

And finally, run the application using:

```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

The above command will start the application using the Maven Spring Boot plugin, activating the **dev** profile. During the starting up process, all the migrations needed to create the database tables, constraints, indexes and default records will be executed.

At the end, the application will be ready to be used, accessing [http://localhost:8080](http://localhost:8080). You can use, *user* as username and *password* as password.

### Profiles

So far, the application is configured to be executed based on two profiles
* *default* which is intended to be used on production environment.
* *dev* which is intended to be used locally during development.

### *dev* Profile

The *dev* profile sets everything necessary to execute the application locally. By default, it take the following assumptions:

* The existence of a local database named **dev_budgie** which will be used by the application.
* The **postgres** database user has the word **password** as password.
* The user name is **user**.
* The user password is **password**.
* Both user name and password are defined in the settings files using the Base64 encoded representation of the bencrypted values.
* The passphrase to encode database text fields is **textFieldSecretPhrase**.
* The passphrase to encode database numeric fields is **decimalFieldSecretPhrase**.
* The passphrase to encode database binary fields is **byteFieldSecretPhrase**.

Those values are defined on ```/src/main/resources/application-dev.yml``` file and can be overwritten used environment variables or jvm parameters.


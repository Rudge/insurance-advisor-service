[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ce4d22c3f2c342638c92b4dc1e8ce955)](https://app.codacy.com/gh/Rudge/insurance-advisor-service?utm_source=github.com&utm_medium=referral&utm_content=Rudge/insurance-advisor-service&utm_campaign=Badge_Grade_Settings)
[![BCH compliance](https://bettercodehub.com/edge/badge/Rudge/insurance-advisor-service?branch=main)](https://bettercodehub.com/)

This project is a proof of concept to calculate the score of user for some lines of insurances.

# How it works

The application built in:

- [Kotlin](https://github.com/JetBrains/kotlin) as programming language
- [Javalin](https://github.com/tipsy/javalin) as web framework
- [Koin](https://github.com/InsertKoinIO/koin) as dependency injection
- [Jackson](https://github.com/FasterXML/jackson-module-kotlin) as data bind
  serialization/deserialization

Unit Tests:

- [kotlin.test](https://kotlinlang.org/api/latest/kotlin.test/)

Integration Tests:

- [RestAssured](https://rest-assured.io/) to call the endpoints in tests and assert response data

#### Structure

      + config/
        All app setups. Javalin and DI(Koin)
        Router definition to features and exceptions
      + domain/
        + service/
            Bussiness rules layer
      + web/
        + controllers
            Classes and methods to mapping actions of routes
        
      - App.kt <- The main class

# Getting started

You need just JVM installed.

#### Build:

The build run unit.

> ./gradlew clean build

The unit test can run just with:

> ./gradlew test

The integration test can run with:

> ./gradlew integrationTest

### Start the server:

This command start the server to use locally.

The server configured to start on [7000](http://localhost:7000/) with empty context, but you can
change in `koin.properties`.

> ./gradlew run
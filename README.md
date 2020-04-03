# Reachable Settlements Service
This service allows users to determine the reachable cities/towns/villages from a starting point in a given amount of time.

## Preparing input data
Service performs operations on data that's read during start-up from a JSON file and stored in heap memory.

The input data format must match to the following schema:
```
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "description": "JSON Schema that defines input data format that can be read by Reachable Settlements Service",
  "type": "array",
  "minItems": 2,
  "items": {
    "description": "Represents any type of human settlement (city, town, village, etc.) and possible direct commutes from it.",
    "type": "object",
    "properties": {
      "name": {
        "description": "The settlement name, must be unique and certain, for example: 'Saint Petersburg, Russia', 'Saint Petersburg, Florida, USA'.",
        "type": "string"
      },
      "commutes": {
        "type": "array",
        "minItems": 1,
        "description": "List of possible direct commutes from this settlement.",
        "items": {
          "description": "Represents commute time to specific settlement.",
          "type": "object",
          "properties": {
            "destPointName": {
              "description": "The name of destination point (must match to the existing settlement name).",
              "type": "string"
            },
            "time": {
              "description": "Time to commute to destination point in minutes.",
              "type": "number",
              "minimum": 1,
              "maximum": 2147483647
            }
          },
          "required": [
            "destPointName",
            "time"
          ]
        }
      }
    },
    "required": [
      "name",
      "commutes"
    ]
  }
}
```
For example: 
```
[
  {
    "name": "St. Petersburg, Russia",
    "commutes": [
      {
        "destPointName": "Minsk, Belarus",
        "time": 120
      },
      {
        "destPointName": "St. Petersburg, Florida, USA",
        "time": 600
      }
    ]
  },
  {
    "name": "Minsk, Belarus",
    "commutes": [
      {
        "destPointName": "St. Petersburg, Russia",
        "time": 120
      },
      {
        "destPointName": "St. Petersburg, Florida, USA",
        "time": 480
      },

    ]
  },
  {
    "name": "St. Petersburg, Florida, USA",
    "commutes": [
      {
        "destPointName": "St. Petersburg, Russia",
        "time": 600
      },
      {
        "destPointName": "Minsk, Belarus,
        "time": 480
      }
    ]
  }
]
```

## Configuration
The configuration properties might be defined in the ```{project_dir}/src/main/resources/application.properties``` 
file.

The properties can also be specified as run arguments, see [Run application](#run-application) section.

#### Required configuration
There is only one required application property to run the service: 
``json.data.file`` that must be set to the path of input data JSON file (described 
in the [Preparing input data](#preparing-input-data) section).

For example: 
```json.data.file=/home/rafael/setllements.json```

#### Optional configuration

  ``server.port`` - sets the port on which the service is running (default: ``8080``), e.g. ``server.port=8083``.
  
  ``logging.file`` - sets the file where logging information is written, e.g. ``logging.file=logfile.log``.

Please refer to the [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html) for advanced configuration.

## Building and running
The project is created using the [Spring Boot](https://spring.io/projects/spring-boot) solution that provides embedded 
web server and can be run as a stand-alone application.

#### Build executable jar using Maven
From the project folder (which contains pom.xml) run the following command:

```mvn package```

The executable jar file will be created in the ``target`` folder:

```{project_dir}/target/reachable-settlements-service-0.0.1.jar```

#### Run application
The application can be run from the project folder trough the Maven command: 

```mvn spring-boot:run```

or using java -jar command:

```java -jar /target/reachable-settlements-service-0.0.1.jar```

run with arguments:

```java -jar /target/reachable-settlements-service-0.0.1.jar --server.port=8083 --json.data.file=/home/rafael/setllements.json```

## Service API:

Finding the reachable settlements from a starting point in a given amount of time is only operation that can be 
performed by service. 
In order to perform this operation, the ``POST`` request must be sent to the ``{host}/reachable-settlements`` URL-path 
with body that matches to the following JSON-schema:

```
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "description": "JSON Schema that defines data format of POST-request bodies that can be sent to '/reachable-settlements'",
  "type": "object",
  "properties": {
    "startingPointName": {
      "description": "The name of starting point (must match to the existing settlement name).",
      "type": "string"
    },
    "commuteTimeMin": {
      "description": "Commute time from starting point, in minutes.",
      "type": "number",
      "minimum": 1,
      "maximum": 2147483647
    }
  },
  "required": [
    "startingPointName",
    "time"
  ]
}

```
The service returns response (``Content-Type: application/json``) which body contains the list of reachable settlement names.

##### Request example:

```curl -d '{"startingPointName":"Berlin, Germany","commuteTimeMin":60}' -H "Content-Type: application/json" -X POST http://localhost:8080/reachable-settlements```

##### Response example: 

```["St. Petersburg, Russia","St. Petersburg, Florida, USA","Minsk, Belarus","New Delhi, India"]```


## What might be improved

- Instead of settlement name some another unique identifier might be used. When client sends unclear starting point 
the service should suggest possible options.
- More deep validation of input JSON-data should be implemented.
- The service should be able to read data runtime by chunks, merge it and store it into database. 
The input data should be split by geographic regions and be loaded from database only when searching in specific 
region is required. The algorithm must be able to join results from different regions.
- More advanced caching should be implemented with possibility to store data partially on disk and load it into 
heap memory when its required.
- The service should support clustering. 

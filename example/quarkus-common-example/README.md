# Example of simple application

This is an example for application which using H2 database with REST endpoints and token security.

## Dependencies

Look on [pom.xml](./pom.xml).

Need to add next dependencies:
* common-engine - for enable DI and other functions.
  * quarkus-common-engine - quarkus based implementation.
* action-engine - for enable action engine.
  * quarkus-action-engine
* data-engine-core - for work with data.
  * data-engine-jpa - JPA based implementation.
* endpoint-engine-rest - activate REST endpoint.
* security-engine-token-rest - token based security for REST.
* security-engine-token-jpa - JPAs for token based security.

## Quarkus

### Config
See directory [resources](./src/main/resources):

* [Config interceptor](./src/main/resources/META-INF/services/io.smallrye.config.ConfigSourceInterceptor)
  * Need to add CryptConfigInterceptor to support encrypted properties
* [Properties](./src/main/resources/application.properties) 
  * Lines before are Quarkus specific and better see all these properties in official page.
  * Application setting
    * engine.system.username - need to set here a username which use to execute action by system user.

#### Notes
In native build, classes which uses reflection need to register for reflection.
You can make it using [reflection-config.json](./src/main/resources/reflection-config.json) file or
using annotations in class [RegisterReflection](./src/main/java/insideworld/engine/example/quarkus/common/quarkus/RegisterReflection.java)

## Enable REST endpoint
You need to implement you own rest endpoint. 
Because Quarkus support imperative and reactive method, we can use both.

To activate imperative need to add dependency quarkus-resteasy-jackson
To activate reactive need to add dependency quarkus-resteasy-jackson

Also, implementation of rest endpoint will different.
If you use imperative - just return Output. In case of reactive you need to return Uni<Output>


Need to override some classes.
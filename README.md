# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.Scimv2BulkApi;

import java.io.File;
import java.util.*;

public class Scimv2BulkApiExample {

    public static void main(String[] args) {
        
        Scimv2BulkApi apiInstance = new Scimv2BulkApi();
        List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
        List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
        String body = "body_example"; // String | 
        try {
            apiInstance.createBulk(attributes, excludedAttributes, body);
        } catch (ApiException e) {
            System.err.println("Exception when calling Scimv2BulkApi#createBulk");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://localhost*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*Scimv2BulkApi* | [**createBulk**](docs/Scimv2BulkApi.md#createBulk) | **POST** /scim/v2/Bulk | Return the bulk which was created.
*Scimv2GroupsApi* | [**createGroup**](docs/Scimv2GroupsApi.md#createGroup) | **POST** /scim/v2/Groups | Return the group which was created
*Scimv2GroupsApi* | [**deleteGroup**](docs/Scimv2GroupsApi.md#deleteGroup) | **DELETE** /scim/v2/Groups/{id} | Delete the group with the given id
*Scimv2GroupsApi* | [**getGroup**](docs/Scimv2GroupsApi.md#getGroup) | **GET** /scim/v2/Groups | Return groups according to the filter, sort and pagination parameters
*Scimv2GroupsApi* | [**getGroupById**](docs/Scimv2GroupsApi.md#getGroupById) | **GET** /scim/v2/Groups/{id} | Return the group with the given id
*Scimv2GroupsApi* | [**getGroupsByPost**](docs/Scimv2GroupsApi.md#getGroupsByPost) | **POST** /scim/v2/Groups/.search | Return groups according to the filter, sort and pagination parameters
*Scimv2GroupsApi* | [**updateGroup**](docs/Scimv2GroupsApi.md#updateGroup) | **PUT** /scim/v2/Groups/{id} | Return the updated group
*Scimv2MeApi* | [**createMe**](docs/Scimv2MeApi.md#createMe) | **POST** /scim/v2/Me | Return the user which was anonymously created
*Scimv2MeApi* | [**deleteMe**](docs/Scimv2MeApi.md#deleteMe) | **DELETE** /scim/v2/Me | Delete the authenticated user.
*Scimv2MeApi* | [**getMe**](docs/Scimv2MeApi.md#getMe) | **GET** /scim/v2/Me | Return the authenticated user.
*Scimv2MeApi* | [**updateMe**](docs/Scimv2MeApi.md#updateMe) | **PUT** /scim/v2/Me | Return the updated user
*Scimv2ResourceTypeApi* | [**getResourceType**](docs/Scimv2ResourceTypeApi.md#getResourceType) | **GET** /scim/v2/ResourceType | Return the ResourceType schema.
*Scimv2ServiceProviderConfigApi* | [**getServiceProviderConfig**](docs/Scimv2ServiceProviderConfigApi.md#getServiceProviderConfig) | **GET** /scim/v2/ServiceProviderConfig | Return the ServiceProviderConfig schema.
*Scimv2UsersApi* | [**createUser**](docs/Scimv2UsersApi.md#createUser) | **POST** /scim/v2/Users | Return the user which was created
*Scimv2UsersApi* | [**deleteUser**](docs/Scimv2UsersApi.md#deleteUser) | **DELETE** /scim/v2/Users/{id} | Delete the user with the given id
*Scimv2UsersApi* | [**getUser**](docs/Scimv2UsersApi.md#getUser) | **GET** /scim/v2/Users | Return users according to the filter, sort and pagination parameters
*Scimv2UsersApi* | [**getUserById**](docs/Scimv2UsersApi.md#getUserById) | **GET** /scim/v2/Users/{id} | Return the user with the given id
*Scimv2UsersApi* | [**getUsersByPost**](docs/Scimv2UsersApi.md#getUsersByPost) | **POST** /scim/v2/Users/.search | Return users according to the filter, sort and pagination parameters
*Scimv2UsersApi* | [**updateUser**](docs/Scimv2UsersApi.md#updateUser) | **PUT** /scim/v2/Users/{id} | Return the updated user


## Documentation for Models



## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

dev@wso2.org


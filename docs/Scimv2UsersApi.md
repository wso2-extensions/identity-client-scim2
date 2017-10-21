# Scimv2UsersApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createUser**](Scimv2UsersApi.md#createUser) | **POST** /scim/v2/Users | Return the user which was created
[**deleteUser**](Scimv2UsersApi.md#deleteUser) | **DELETE** /scim/v2/Users/{id} | Delete the user with the given id
[**getUser**](Scimv2UsersApi.md#getUser) | **GET** /scim/v2/Users | Return users according to the filter, sort and pagination parameters
[**getUserById**](Scimv2UsersApi.md#getUserById) | **GET** /scim/v2/Users/{id} | Return the user with the given id
[**getUsersByPost**](Scimv2UsersApi.md#getUsersByPost) | **POST** /scim/v2/Users/.search | Return users according to the filter, sort and pagination parameters
[**updateUser**](Scimv2UsersApi.md#updateUser) | **PUT** /scim/v2/Users/{id} | Return the updated user


<a name="createUser"></a>
# **createUser**
> createUser(attributes, excludedAttributes, body)

Return the user which was created

Returns HTTP 201 if the user is successfully created.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2UsersApi;


Scimv2UsersApi apiInstance = new Scimv2UsersApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String body = "body_example"; // String | 
try {
    apiInstance.createUser(attributes, excludedAttributes, body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2UsersApi#createUser");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attributes** | [**List&lt;String&gt;**](String.md)| SCIM defined attributes parameter. | [optional]
 **excludedAttributes** | [**List&lt;String&gt;**](String.md)| SCIM defined excludedAttribute parameter. | [optional]
 **body** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/scim+json
 - **Accept**: application/json, application/scim+json

<a name="deleteUser"></a>
# **deleteUser**
> deleteUser(id)

Delete the user with the given id

Returns HTTP 204 if the user is successfully deleted.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2UsersApi;


Scimv2UsersApi apiInstance = new Scimv2UsersApi();
String id = "id_example"; // String | Unique id of the resource type.
try {
    apiInstance.deleteUser(id);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2UsersApi#deleteUser");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Unique id of the resource type. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/scim+json

<a name="getUser"></a>
# **getUser**
> getUser(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder)

Return users according to the filter, sort and pagination parameters

Returns HTTP 404 if the users are not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2UsersApi;


Scimv2UsersApi apiInstance = new Scimv2UsersApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String filter = "filter_example"; // String | Filter expression for filtering
Integer startIndex = 56; // Integer | The 1-based index of the first query result
Integer count = 56; // Integer | Specifies the desired maximum number of query results per page.
String sortBy = "sortBy_example"; // String | Specifies the attribute whose value SHALL be used to order the returned responses
String sortOder = "sortOder_example"; // String | The order in which the \"sortBy\" parameter is applied.
try {
    apiInstance.getUser(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2UsersApi#getUser");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attributes** | [**List&lt;String&gt;**](String.md)| SCIM defined attributes parameter. | [optional]
 **excludedAttributes** | [**List&lt;String&gt;**](String.md)| SCIM defined excludedAttribute parameter. | [optional]
 **filter** | **String**| Filter expression for filtering | [optional]
 **startIndex** | **Integer**| The 1-based index of the first query result | [optional]
 **count** | **Integer**| Specifies the desired maximum number of query results per page. | [optional]
 **sortBy** | **String**| Specifies the attribute whose value SHALL be used to order the returned responses | [optional]
 **sortOder** | **String**| The order in which the \&quot;sortBy\&quot; parameter is applied. | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/scim+json

<a name="getUserById"></a>
# **getUserById**
> getUserById(id, attributes, excludedAttributes)

Return the user with the given id

Returns HTTP 200 if the user is found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2UsersApi;


Scimv2UsersApi apiInstance = new Scimv2UsersApi();
String id = "id_example"; // String | Unique id of the resource type.
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
try {
    apiInstance.getUserById(id, attributes, excludedAttributes);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2UsersApi#getUserById");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Unique id of the resource type. |
 **attributes** | [**List&lt;String&gt;**](String.md)| SCIM defined attributes parameter. | [optional]
 **excludedAttributes** | [**List&lt;String&gt;**](String.md)| SCIM defined excludedAttribute parameter. | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/scim+json

<a name="getUsersByPost"></a>
# **getUsersByPost**
> getUsersByPost(body)

Return users according to the filter, sort and pagination parameters

Returns HTTP 404 if the users are not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2UsersApi;


Scimv2UsersApi apiInstance = new Scimv2UsersApi();
String body = "body_example"; // String | 
try {
    apiInstance.getUsersByPost(body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2UsersApi#getUsersByPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/scim+json
 - **Accept**: application/json, application/scim+json

<a name="updateUser"></a>
# **updateUser**
> updateUser(id, attributes, excludedAttributes, body)

Return the updated user

Returns HTTP 404 if the user is not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2UsersApi;


Scimv2UsersApi apiInstance = new Scimv2UsersApi();
String id = "id_example"; // String | Unique id of the resource type.
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String body = "body_example"; // String | 
try {
    apiInstance.updateUser(id, attributes, excludedAttributes, body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2UsersApi#updateUser");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Unique id of the resource type. |
 **attributes** | [**List&lt;String&gt;**](String.md)| SCIM defined attributes parameter. | [optional]
 **excludedAttributes** | [**List&lt;String&gt;**](String.md)| SCIM defined excludedAttribute parameter. | [optional]
 **body** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/scim+json
 - **Accept**: application/json, application/scim+json


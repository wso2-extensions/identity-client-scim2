# Scimv2GroupsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createGroup**](Scimv2GroupsApi.md#createGroup) | **POST** /scim/v2/Groups | Return the group which was created
[**deleteGroup**](Scimv2GroupsApi.md#deleteGroup) | **DELETE** /scim/v2/Groups/{id} | Delete the group with the given id
[**getGroup**](Scimv2GroupsApi.md#getGroup) | **GET** /scim/v2/Groups | Return groups according to the filter, sort and pagination parameters
[**getGroupById**](Scimv2GroupsApi.md#getGroupById) | **GET** /scim/v2/Groups/{id} | Return the group with the given id
[**getGroupsByPost**](Scimv2GroupsApi.md#getGroupsByPost) | **POST** /scim/v2/Groups/.search | Return groups according to the filter, sort and pagination parameters
[**updateGroup**](Scimv2GroupsApi.md#updateGroup) | **PUT** /scim/v2/Groups/{id} | Return the updated group


<a name="createGroup"></a>
# **createGroup**
> createGroup(attributes, excludedAttributes, body)

Return the group which was created

Returns HTTP 201 if the group is successfully created.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2GroupsApi;


Scimv2GroupsApi apiInstance = new Scimv2GroupsApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String body = "body_example"; // String | 
try {
    apiInstance.createGroup(attributes, excludedAttributes, body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2GroupsApi#createGroup");
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

<a name="deleteGroup"></a>
# **deleteGroup**
> deleteGroup(id)

Delete the group with the given id

Returns HTTP 204 if the group is successfully deleted.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2GroupsApi;


Scimv2GroupsApi apiInstance = new Scimv2GroupsApi();
String id = "id_example"; // String | Unique id of the resource type.
try {
    apiInstance.deleteGroup(id);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2GroupsApi#deleteGroup");
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

<a name="getGroup"></a>
# **getGroup**
> getGroup(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder)

Return groups according to the filter, sort and pagination parameters

Returns HTTP 404 if the groups are not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2GroupsApi;


Scimv2GroupsApi apiInstance = new Scimv2GroupsApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String filter = "filter_example"; // String | Filter expression for filtering
Integer startIndex = 56; // Integer | The 1-based index of the first query result
Integer count = 56; // Integer | Specifies the desired maximum number of query results per page.
String sortBy = "sortBy_example"; // String | Specifies the attribute whose value SHALL be used to order the returned responses
String sortOder = "sortOder_example"; // String | The order in which the \"sortBy\" parameter is applied.
try {
    apiInstance.getGroup(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2GroupsApi#getGroup");
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

<a name="getGroupById"></a>
# **getGroupById**
> getGroupById(id, attributes, excludedAttributes)

Return the group with the given id

Returns HTTP 200 if the group is found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2GroupsApi;


Scimv2GroupsApi apiInstance = new Scimv2GroupsApi();
String id = "id_example"; // String | Unique id of the resource type.
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
try {
    apiInstance.getGroupById(id, attributes, excludedAttributes);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2GroupsApi#getGroupById");
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

<a name="getGroupsByPost"></a>
# **getGroupsByPost**
> getGroupsByPost(body)

Return groups according to the filter, sort and pagination parameters

Returns HTTP 404 if the groups are not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2GroupsApi;


Scimv2GroupsApi apiInstance = new Scimv2GroupsApi();
String body = "body_example"; // String | 
try {
    apiInstance.getGroupsByPost(body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2GroupsApi#getGroupsByPost");
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

<a name="updateGroup"></a>
# **updateGroup**
> updateGroup(id, attributes, excludedAttributes, body)

Return the updated group

Returns HTTP 404 if the group is not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2GroupsApi;


Scimv2GroupsApi apiInstance = new Scimv2GroupsApi();
String id = "id_example"; // String | Unique id of the resource type.
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String body = "body_example"; // String | 
try {
    apiInstance.updateGroup(id, attributes, excludedAttributes, body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2GroupsApi#updateGroup");
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


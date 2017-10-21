# Scimv2MeApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createMe**](Scimv2MeApi.md#createMe) | **POST** /scim/v2/Me | Return the user which was anonymously created
[**deleteMe**](Scimv2MeApi.md#deleteMe) | **DELETE** /scim/v2/Me | Delete the authenticated user.
[**getMe**](Scimv2MeApi.md#getMe) | **GET** /scim/v2/Me | Return the authenticated user.
[**updateMe**](Scimv2MeApi.md#updateMe) | **PUT** /scim/v2/Me | Return the updated user


<a name="createMe"></a>
# **createMe**
> createMe(attributes, excludedAttributes, body)

Return the user which was anonymously created

Returns HTTP 201 if the user is successfully created.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2MeApi;


Scimv2MeApi apiInstance = new Scimv2MeApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String body = "body_example"; // String | 
try {
    apiInstance.createMe(attributes, excludedAttributes, body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2MeApi#createMe");
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

<a name="deleteMe"></a>
# **deleteMe**
> deleteMe()

Delete the authenticated user.

Returns HTTP 204 if the user is successfully deleted.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2MeApi;


Scimv2MeApi apiInstance = new Scimv2MeApi();
try {
    apiInstance.deleteMe();
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2MeApi#deleteMe");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/scim+json

<a name="getMe"></a>
# **getMe**
> getMe(attributes, excludedAttributes)

Return the authenticated user.

Returns HTTP 200 if the user is found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2MeApi;


Scimv2MeApi apiInstance = new Scimv2MeApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
try {
    apiInstance.getMe(attributes, excludedAttributes);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2MeApi#getMe");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attributes** | [**List&lt;String&gt;**](String.md)| SCIM defined attributes parameter. | [optional]
 **excludedAttributes** | [**List&lt;String&gt;**](String.md)| SCIM defined excludedAttribute parameter. | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/scim+json

<a name="updateMe"></a>
# **updateMe**
> updateMe(attributes, excludedAttributes, body)

Return the updated user

Returns HTTP 404 if the user is not found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2MeApi;


Scimv2MeApi apiInstance = new Scimv2MeApi();
List<String> attributes = Arrays.asList("attributes_example"); // List<String> | SCIM defined attributes parameter.
List<String> excludedAttributes = Arrays.asList("excludedAttributes_example"); // List<String> | SCIM defined excludedAttribute parameter.
String body = "body_example"; // String | 
try {
    apiInstance.updateMe(attributes, excludedAttributes, body);
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2MeApi#updateMe");
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


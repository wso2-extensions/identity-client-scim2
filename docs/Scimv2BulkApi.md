# Scimv2BulkApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createBulk**](Scimv2BulkApi.md#createBulk) | **POST** /scim/v2/Bulk | Return the bulk which was created.


<a name="createBulk"></a>
# **createBulk**
> createBulk(attributes, excludedAttributes, body)

Return the bulk which was created.

Returns HTTP 201 if the bulk is successfully created.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2BulkApi;


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


# Scimv2ResourceTypeApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getResourceType**](Scimv2ResourceTypeApi.md#getResourceType) | **GET** /scim/v2/ResourceType | Return the ResourceType schema.


<a name="getResourceType"></a>
# **getResourceType**
> getResourceType()

Return the ResourceType schema.

Returns HTTP 200 if the schema is found.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.Scimv2ResourceTypeApi;


Scimv2ResourceTypeApi apiInstance = new Scimv2ResourceTypeApi();
try {
    apiInstance.getResourceType();
} catch (ApiException e) {
    System.err.println("Exception when calling Scimv2ResourceTypeApi#getResourceType");
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


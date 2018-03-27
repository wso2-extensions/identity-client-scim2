# Scimv2ServiceProviderConfigApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getServiceProviderConfig**](Scimv2ServiceProviderConfigApi.md#getServiceProviderConfig) | **GET** /scim/v2/ServiceProviderConfig | Return the ServiceProviderConfig schema.


<a name="getServiceProviderConfig"></a>
# **getServiceProviderConfig**
> getServiceProviderConfig()

Return the ServiceProviderConfig schema.

Returns HTTP 200 if the schema is found.

### Example
```java

Scimv2ServiceProviderConfigApi apiInstance = new Scimv2ServiceProviderConfigApi();
try {
    apiInstance.getServiceProviderConfig();
} catch (ScimApiException e) {
    System.err.println("Exception when calling Scimv2ServiceProviderConfigApi#getServiceProviderConfig");
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


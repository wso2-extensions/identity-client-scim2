/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.client.api;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import io.swagger.client.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scimv2UsersApi extends Scimv2BaseApi{

    public Scimv2UsersApi() {
        this(Configuration.getDefaultApiClient());
    }

    public Scimv2UsersApi(ApiClient apiClient) {
        super(apiClient);
    }
    
    private Call createUserValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
          
        Call call = createResourceCall(attributes, excludedAttributes, body);
        return call;    
    }

    /**
     * Return the user which was created
     * Returns HTTP 201 if the user is successfully created.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> createUser(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {

        ApiResponse<String> resp = createUserWithHttpInfo(attributes, excludedAttributes, body);
        return resp;
    }

    /**
     * Return the user which was created
     * Returns HTTP 201 if the user is successfully created.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> createUserWithHttpInfo(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {

        Call call = createUserValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
    
    private Call deleteUserValidateBeforeCall() throws ApiException {

        Call call = deleteResourceCall();
        return call;    
    }

    /**
     * Delete the user with the given id
     * Returns HTTP 204 if the user is successfully deleted.
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> deleteUser() throws ApiException {

        ApiResponse<String> resp = deleteUserWithHttpInfo();
        return resp;
    }

    /**
     * Delete the user with the given id
     * Returns HTTP 204 if the user is successfully deleted.
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> deleteUserWithHttpInfo() throws ApiException {

        Call call = deleteUserValidateBeforeCall();
        return apiClient.execute(call);
    }

    /**
     * Build call for getUser
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter Filter expression for filtering (optional)
     * @param startIndex The 1-based index of the first query result (optional)
     * @param count Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOrder The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getUserCall(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOrder) throws ApiException {

        Object localVarPostBody = null;

        List<Pair> localVarQueryParams = new ArrayList<>();
        if (attributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));
        if (filter != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "filter", filter));
        if (startIndex != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "startIndex", startIndex));
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (sortBy != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "sortBy", sortBy));
        if (sortOrder != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "sortOrder", sortOrder));

        Map<String, String> localVarHeaderParams = new HashMap<>();

        Map<String, Object> localVarFormParams = new HashMap<>();

        /*final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);*/

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call getUserValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOder) throws ApiException {
    
        Call call = getUserCall(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
        return call;      
    }

    /**
     * Return users according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the users are not found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter Filter expression for filtering (optional)
     * @param startIndex The 1-based index of the first query result (optional)
     * @param count Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOrder The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getUser(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOrder) throws ApiException {
        ApiResponse<String> resp = getUserWithHttpInfo(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOrder);
        return resp;
    }

    /**
     * Return users according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the users are not found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter Filter expression for filtering (optional)
     * @param startIndex The 1-based index of the first query result (optional)
     * @param count Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOrder The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getUserWithHttpInfo(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOrder) throws ApiException {
        Call call = getUserValidateBeforeCall(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOrder);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
    
    private Call getUserByIdValidateBeforeCall(List<String> attributes, List<String> excludedAttributes) throws ApiException {
         
        Call call = getResourceCall(attributes, excludedAttributes);
        return call;   
    }

    /**
     * Return the user with the given id
     * Returns HTTP 200 if the user is found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getUserById(List<String> attributes, List<String> excludedAttributes) throws ApiException {

        ApiResponse<String> resp = getUserByIdWithHttpInfo(attributes, excludedAttributes);
        return resp.getData();
    }

    /**
     * Return the user with the given id
     * Returns HTTP 200 if the user is found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getUserByIdWithHttpInfo(List<String> attributes, List<String> excludedAttributes) throws ApiException {

        Call call = getUserByIdValidateBeforeCall(attributes, excludedAttributes);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
    
    private Call getUsersByPostValidateBeforeCall(String body) throws ApiException {
               
        Call call = getResourcesByPostCall(body);
        return call;  
    }

    /**
     * Return users according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the users are not found.
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getUsersByPost(String body) throws ApiException {

        ApiResponse<String> resp = getUsersByPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Return users according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the users are not found.
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getUsersByPostWithHttpInfo(String body) throws ApiException {

        Call call = getUsersByPostValidateBeforeCall(body);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Build call for updateUser
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call updateUserCall(List<String> attributes, List<String> excludedAttributes, String body, String httpMethod) throws ApiException {

        Object localVarPostBody = body;

        List<Pair> localVarQueryParams = new ArrayList<>();
        if (attributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<>();

        Map<String, Object> localVarFormParams = new HashMap<>();

        final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/scim+json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(httpMethod, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call updateUserValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body, String httpMethod) throws ApiException {
               
        Call call = updateUserCall(attributes, excludedAttributes, body, httpMethod);
        return call;  
    }

    /**
     * Return the updated user
     * Returns HTTP 404 if the user is not found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> updateUser(List<String> attributes, List<String> excludedAttributes, String body, String httpMethod) throws ApiException {

        ApiResponse<String> resp = updateUserWithHttpInfo(attributes, excludedAttributes, body, httpMethod);
        return resp;
    }

    /**
     * Return the updated user
     * Returns HTTP 404 if the user is not found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> updateUserWithHttpInfo(List<String> attributes, List<String> excludedAttributes, String body, String httpMethod) throws ApiException {
        Call call = updateUserValidateBeforeCall(attributes, excludedAttributes, body, httpMethod);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

}

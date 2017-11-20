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
import io.swagger.client.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.okhttp.Call;

public class Scimv2GroupsApi {
    private ApiClient apiClient;

    public Scimv2GroupsApi() {
        this(Configuration.getDefaultApiClient());
    }

    public Scimv2GroupsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for createGroup
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call createGroupCall(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        Object localVarPostBody = body;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        /*final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);*/

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall("POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call createGroupValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {

        Call call = createGroupCall(attributes, excludedAttributes, body);
        return call;
    }

    /**
     * Return the group which was created
     * Returns HTTP 201 if the group is successfully created.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> createGroup(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        ApiResponse<String> resp = createGroupWithHttpInfo(attributes, excludedAttributes, body);
        return resp;
    }

    /**
     * Return the group which was created
     * Returns HTTP 201 if the group is successfully created.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> createGroupWithHttpInfo(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        Call call = createGroupValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Build call for deleteGroup
     * @param id Unique id of the resource type. (required)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call deleteGroupCall(String id) throws ApiException {
        Object localVarPostBody = null;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall("DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call deleteGroupValidateBeforeCall(String id) throws ApiException {
        
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling deleteGroup(Async)");
        }

        Call call = deleteGroupCall(id);
        return call;
    }

    /**
     * Delete the group with the given id
     * Returns HTTP 204 if the group is successfully deleted.
     * @param id Unique id of the resource type. (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> deleteGroup(String id) throws ApiException {

        ApiResponse<String> resp = deleteGroupWithHttpInfo(id);
        return resp;
    }

    /**
     * Delete the group with the given id
     * Returns HTTP 204 if the group is successfully deleted.
     * @param id Unique id of the resource type. (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> deleteGroupWithHttpInfo(String id) throws ApiException {

        Call call = deleteGroupValidateBeforeCall(id);
        return apiClient.execute(call);
    }

    /**
     * Build call for getGroup
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter Filter expression for filtering (optional)
     * @param startIndex The 1-based index of the first query result (optional)
     * @param count Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOder The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getGroupCall(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOder) throws ApiException {
        Object localVarPostBody = null;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
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
        if (sortOder != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "sortOder", sortOder));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call getGroupValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOder) throws ApiException {
        
        Call call = getGroupCall(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
        return call;
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter Filter expression for filtering (optional)
     * @param startIndex The 1-based index of the first query result (optional)
     * @param count Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOder The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getGroup(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOder) throws ApiException {
        ApiResponse<String> resp = getGroupWithHttpInfo(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
        return resp;
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter Filter expression for filtering (optional)
     * @param startIndex The 1-based index of the first query result (optional)
     * @param count Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOder The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getGroupWithHttpInfo(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex, Integer count, String sortBy, String sortOder) throws ApiException {
        Call call = getGroupValidateBeforeCall(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Build call for getGroupById
     * @param id Unique id of the resource type. (required)
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getGroupByIdCall(String id, List<String> attributes, List<String> excludedAttributes) throws ApiException {
        Object localVarPostBody = null;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call getGroupByIdValidateBeforeCall(String id, List<String> attributes, List<String> excludedAttributes) throws ApiException {
        
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling getGroupById(Async)");
        }

        Call call = getGroupByIdCall(id, attributes, excludedAttributes);
        return call;
    }

    /**
     * Return the group with the given id
     * Returns HTTP 200 if the group is found.
     * @param id Unique id of the resource type. (required)
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getGroupById(String id, List<String> attributes, List<String> excludedAttributes) throws ApiException {
        ApiResponse<String> resp = getGroupByIdWithHttpInfo(id, attributes, excludedAttributes);
        return resp.getData();
    }

    /**
     * Return the group with the given id
     * Returns HTTP 200 if the group is found.
     * @param id Unique id of the resource type. (required)
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getGroupByIdWithHttpInfo(String id, List<String> attributes, List<String> excludedAttributes) throws ApiException {
        Call call = getGroupByIdValidateBeforeCall(id, attributes, excludedAttributes);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Build call for getGroupsByPost
     * @param body  (optional)
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getGroupsByPostCall(String body) throws ApiException {
        Object localVarPostBody = body;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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
        return apiClient.buildCall("POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call getGroupsByPostValidateBeforeCall(String body) throws ApiException {

        Call call = getGroupsByPostCall(body);
        return call;
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getGroupsByPost(String body) throws ApiException {
        ApiResponse<String> resp = getGroupsByPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getGroupsByPostWithHttpInfo(String body) throws ApiException {
        Call call = getGroupsByPostValidateBeforeCall(body);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Build call for updateGroup
     * @param id Unique id of the resource type. (required)
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call updateGroupCall(String id, List<String> attributes, List<String> excludedAttributes, String body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        Object localVarPostBody = body;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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
        return apiClient.buildCall("PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call updateGroupValidateBeforeCall(String id, List<String> attributes, List<String> excludedAttributes, String body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling updateGroup(Async)");
        }

        Call call = updateGroupCall(id, attributes, excludedAttributes, body, progressListener, progressRequestListener);
        return call;
    }

    /**
     * Return the updated group
     * Returns HTTP 404 if the group is not found.
     * @param id Unique id of the resource type. (required)
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> updateGroup(String id, List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        ApiResponse<String> resp = updateGroupWithHttpInfo(id, attributes, excludedAttributes, body);
        return resp;
    }

    /**
     * Return the updated group
     * Returns HTTP 404 if the group is not found.
     * @param id Unique id of the resource type. (required)
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> updateGroupWithHttpInfo(String id, List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        Call call = updateGroupValidateBeforeCall(id, attributes, excludedAttributes, body, null, null);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
}

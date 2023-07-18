/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package io.scim2.swagger.client.api;

import com.google.gson.reflect.TypeToken;
import io.scim2.swagger.client.ScimApiClient;
import io.scim2.swagger.client.Configuration;
import io.scim2.swagger.client.Pair;
import io.scim2.swagger.client.ScimApiResponse;
import io.scim2.swagger.client.ScimApiException;
import org.apache.http.client.methods.HttpUriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scimv2UsersApi extends Scimv2BaseApi {

    public Scimv2UsersApi() {
        this(Configuration.getDefaultScimApiClient());
    }

    public Scimv2UsersApi(ScimApiClient scimApiClient) {
        super(scimApiClient);
    }

    private HttpUriRequest createUserValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return createResourceCall(attributes, excludedAttributes, body);
    }

    /**
     * Return the user which was created
     * Returns HTTP 201 if the user is successfully created.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> createUser(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return createUserWithHttpInfo(attributes, excludedAttributes, body);
    }

    /**
     * Return the user which was created
     * Returns HTTP 201 if the user is successfully created.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> createUserWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                          String body) throws ScimApiException {
        HttpUriRequest call = createUserValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest deleteUserValidateBeforeCall() throws ScimApiException {

        return deleteResourceCall();
    }

    /**
     * Delete the user with the given id
     * Returns HTTP 204 if the user is successfully deleted.
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> deleteUser() throws ScimApiException {

        return deleteUserWithHttpInfo();
    }

    /**
     * Delete the user with the given id
     * Returns HTTP 204 if the user is successfully deleted.
     *
     * @return ScimApiResponse&lt;Void&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> deleteUserWithHttpInfo() throws ScimApiException {
        HttpUriRequest call = deleteUserValidateBeforeCall();
        return scimApiClient.execute(call);
    }

    /**
     * Build call for getUser
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter             Filter expression for filtering (optional)
     * @param startIndex         The 1-based index of the first query result (optional)
     * @param count              Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy             Specifies the attribute whose value SHALL be used to order the returned responses
     *                           (optional)
     * @param sortOrder          The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest getUserCall(List<String> attributes, List<String> excludedAttributes, String filter, Integer startIndex,
                            Integer count, String sortBy, String sortOrder) throws ScimApiException {

        Object localVarPostBody = null;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "attributes",
                    attributes));
        if (excludedAttributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "excludedAttributes",
                    excludedAttributes));
        if (filter != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("", "filter", filter));
        if (startIndex != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("", "startIndex",
                    startIndex));
        if (count != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("", "count", count));
        if (sortBy != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("", "sortBy", sortBy));
        if (sortOrder != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("", "sortOrder", sortOrder));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        /*final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);*/

        final String[] localVarContentTypes = {};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[]{"basicAuth"};
        return scimApiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, localVarAuthNames);
    }

    private HttpUriRequest getUserValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String filter,
                                           Integer startIndex, Integer count, String sortBy, String sortOder)
            throws ScimApiException {

        return getUserCall(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
    }

    /**
     * Return users according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the users are not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter             Filter expression for filtering (optional)
     * @param startIndex         The 1-based index of the first query result (optional)
     * @param count              Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy             Specifies the attribute whose value SHALL be used to order the returned responses
     *                           (optional)
     * @param sortOrder          The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getUser(List<String> attributes, List<String> excludedAttributes, String filter,
                                           Integer startIndex, Integer count, String sortBy, String sortOrder)
            throws ScimApiException {

        return getUserWithHttpInfo(attributes, excludedAttributes, filter, startIndex, count,
                sortBy, sortOrder);
    }

    /**
     * Return users according to the filter, sort and pagination parameters.
     * Returns HTTP 404 if the users are not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter             Filter expression for filtering (optional)
     * @param startIndex         The 1-based index of the first query result (optional)
     * @param count              Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy             Specifies the attribute whose value SHALL be used to order the returned responses
     *                           (optional)
     * @param sortOrder          The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getUserWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                       String filter, Integer startIndex, Integer count, String sortBy,
                                                       String sortOrder) throws ScimApiException {
        HttpUriRequest call = getUserValidateBeforeCall(attributes, excludedAttributes, filter, startIndex, count, sortBy,
                sortOrder);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest getUserByIdValidateBeforeCall(List<String> attributes, List<String> excludedAttributes)
            throws ScimApiException {

        return getResourceCall(attributes, excludedAttributes);
    }

    /**
     * Return the user with the given id.
     * Returns HTTP 200 if the user is found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getUserById(List<String> attributes, List<String> excludedAttributes) throws ScimApiException {
        ScimApiResponse<String> resp = getUserByIdWithHttpInfo(attributes, excludedAttributes);
        return resp.getData();
    }

    /**
     * Return the user with the given id
     * Returns HTTP 200 if the user is found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getUserByIdWithHttpInfo(List<String> attributes, List<String> excludedAttributes)
            throws ScimApiException {
        HttpUriRequest call = getUserByIdValidateBeforeCall(attributes, excludedAttributes);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest getUsersByPostValidateBeforeCall(String body) throws ScimApiException {

        return getResourcesByPostCall(body);
    }

    /**
     * Return users according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the users are not found.
     *
     * @param body (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getUsersByPost(String body) throws ScimApiException {
        ScimApiResponse<String> resp = getUsersByPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Return users according to the filter, sort and pagination parameters.
     * Returns HTTP 404 if the users are not found.
     *
     * @param body (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getUsersByPostWithHttpInfo(String body) throws ScimApiException {
        HttpUriRequest call = getUsersByPostValidateBeforeCall(body);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    /**
     * Build call for updateUser.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest updateUserCall(List<String> attributes, List<String> excludedAttributes, String body, String httpMethod)
            throws ScimApiException {

        Object localVarPostBody = body;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "attributes",
                    attributes));
        if (excludedAttributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "excludedAttributes",
                    excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {"application/scim+json"};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        String[] localVarAuthNames = new String[]{"basicAuth"};
        return scimApiClient.buildCall(httpMethod, localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, localVarAuthNames);
    }

    private HttpUriRequest updateUserValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body,
                                              String httpMethod) throws ScimApiException {

        return updateUserCall(attributes, excludedAttributes, body, httpMethod);
    }

    /**
     * Return the updated user
     * Returns HTTP 404 if the user is not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> updateUser(List<String> attributes, List<String> excludedAttributes, String body,
                                              String httpMethod) throws ScimApiException {

        return updateUserWithHttpInfo(attributes, excludedAttributes, body, httpMethod);
    }

    /**
     * Return the updated user
     * Returns HTTP 404 if the user is not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> updateUserWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                          String body, String httpMethod) throws ScimApiException {
        HttpUriRequest call = updateUserValidateBeforeCall(attributes, excludedAttributes, body, httpMethod);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }
}

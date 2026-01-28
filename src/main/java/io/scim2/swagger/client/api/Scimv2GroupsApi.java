/*
 * Copyright (c) 2018-2026, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.scim2.swagger.client.api;

import com.google.gson.reflect.TypeToken;
import io.scim2.swagger.client.Configuration;
import io.scim2.swagger.client.Pair;
import io.scim2.swagger.client.ScimApiClient;
import io.scim2.swagger.client.ScimApiException;
import io.scim2.swagger.client.ScimApiResponse;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scimv2GroupsApi extends Scimv2BaseApi {

    public Scimv2GroupsApi() {
        this(Configuration.getDefaultScimApiClient());
    }

    public Scimv2GroupsApi(ScimApiClient scimApiClient) {
        super(scimApiClient);
    }

    /**
     * Build call for createGroup
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest createGroupCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {
        Object localVarPostBody = body;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        /*final String[] localVarAccepts = {
            "application/json", "application/scim+json"
        };
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);*/
        final String[] localVarContentTypes = {
                "application/json"
        };
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[]{"basicAuth"};
        return scimApiClient.buildCall("POST", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, localVarAuthNames);
    }

    private HttpUriRequest createGroupValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return createGroupCall(attributes, excludedAttributes, body);
    }

    /**
     * Return the group which was created
     * Returns HTTP 201 if the group is successfully created.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> createGroup(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return createGroupWithHttpInfo(attributes, excludedAttributes, body);
    }

    /**
     * Return the group which was created
     * Returns HTTP 201 if the group is successfully created.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> createGroupWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                           String body) throws ScimApiException {
        HttpUriRequest call = createGroupValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>() {
        }.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest deleteGroupValidateBeforeCall() throws ScimApiException {

        return deleteResourceCall();
    }

    /**
     * Delete the group with the given id
     * Returns HTTP 204 if the group is successfully deleted.
     *
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> deleteGroup() throws ScimApiException {

        return deleteGroupWithHttpInfo();
    }

    /**
     * Delete the group with the given id.
     * Returns HTTP 204 if the group is successfully deleted.
     *
     * @return ScimApiResponse&lt;Void&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body.
     */
    public ScimApiResponse<String> deleteGroupWithHttpInfo() throws ScimApiException {
        HttpUriRequest call = deleteGroupValidateBeforeCall();
        return scimApiClient.execute(call);
    }

    /**
     * Build call for getGroup.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter             Filter expression for filtering (optional)
     * @param startIndex         The 1-based index of the first query result (optional)
     * @param count              Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy             Specifies the attribute whose value SHALL be used to order the returned responses
     *                           (optional)
     * @param sortOder           The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest getGroupCall(List<String> attributes, List<String> excludedAttributes, String filter,
                             Integer startIndex, Integer count, String sortBy, String sortOder)
            throws ScimApiException {
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
        if (sortOder != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("", "sortOder", sortOder));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[]{"basicAuth"};
        return scimApiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, localVarAuthNames);
    }

    private HttpUriRequest getGroupValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String filter,
                                            Integer startIndex, Integer count, String sortBy, String sortOder) throws
            ScimApiException {

        return getGroupCall(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter             Filter expression for filtering (optional)
     * @param startIndex         The 1-based index of the first query result (optional)
     * @param count              Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy             Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOder           The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getGroup(List<String> attributes, List<String> excludedAttributes, String filter,
                                            Integer startIndex, Integer count, String sortBy, String sortOder)
            throws ScimApiException {

        return getGroupWithHttpInfo(attributes, excludedAttributes, filter, startIndex, count,
                sortBy, sortOder);
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param filter             Filter expression for filtering (optional)
     * @param startIndex         The 1-based index of the first query result (optional)
     * @param count              Specifies the desired maximum number of query results per page. (optional)
     * @param sortBy             Specifies the attribute whose value SHALL be used to order the returned responses (optional)
     * @param sortOder           The order in which the \&quot;sortBy\&quot; parameter is applied. (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getGroupWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                        String filter, Integer startIndex, Integer count, String sortBy,
                                                        String sortOder) throws ScimApiException {
        HttpUriRequest call = getGroupValidateBeforeCall(attributes, excludedAttributes, filter, startIndex, count, sortBy,
                sortOder);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest getGroupByIdValidateBeforeCall(List<String> attributes, List<String> excludedAttributes)
            throws ScimApiException {

        return getResourceCall(attributes, excludedAttributes);
    }

    /**
     * Return the group with the given id
     * Returns HTTP 200 if the group is found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getGroupById(List<String> attributes, List<String> excludedAttributes) throws ScimApiException {
        ScimApiResponse<String> resp = getGroupByIdWithHttpInfo(attributes, excludedAttributes);
        return resp.getData();
    }

    /**
     * Return the group with the given id
     * Returns HTTP 200 if the group is found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getGroupByIdWithHttpInfo(List<String> attributes, List<String> excludedAttributes)
            throws ScimApiException {
        HttpUriRequest call = getGroupByIdValidateBeforeCall(attributes, excludedAttributes);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest getGroupsByPostValidateBeforeCall(String body) throws ScimApiException {

        return getResourcesByPostCall(body);
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     *
     * @param body (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getGroupsByPost(String body) throws ScimApiException {
        ScimApiResponse<String> resp = getGroupsByPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * Return groups according to the filter, sort and pagination parameters
     * Returns HTTP 404 if the groups are not found.
     *
     * @param body (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getGroupsByPostWithHttpInfo(String body) throws ScimApiException {
        HttpUriRequest call = getGroupsByPostValidateBeforeCall(body);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest updateGroupValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return updateResourceCall(attributes, excludedAttributes, body);
    }

    /**
     * Return the updated group
     * Returns HTTP 404 if the group is not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> updateGroup(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return updateGroupWithHttpInfo(attributes, excludedAttributes, body);
    }

    /**
     * Return the updated group
     * Returns HTTP 404 if the group is not found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> updateGroupWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                           String body) throws ScimApiException {
        HttpUriRequest call = updateGroupValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }
}

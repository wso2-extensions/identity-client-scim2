/*
 * Copyright (c) 2018, WSO2 LLC. (http://www.wso2.com).
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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.scim2.swagger.client.api;

import io.scim2.swagger.client.ScimApiClient;
import io.scim2.swagger.client.ScimApiException;
import io.scim2.swagger.client.Configuration;
import io.scim2.swagger.client.Pair;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.wso2.scim2.util.SCIM2CommonConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scimv2BaseApi {

    protected ScimApiClient scimApiClient;
    protected String[] authNames;

    public Scimv2BaseApi() {
        this(Configuration.getDefaultScimApiClient());
    }

    public Scimv2BaseApi(ScimApiClient scimApiClient) {
        this.scimApiClient = scimApiClient;
        this.authNames = new String[]{SCIM2CommonConstants.AUTH_SCHEME_BASIC};
    }

    public ScimApiClient getScimApiClient() {
        return scimApiClient;
    }

    public void setScimApiClient(ScimApiClient scimApiClient) {
        this.scimApiClient = scimApiClient;
    }

    /**
     * Set the authentication names to use for API calls.
     *
     * @param authNames Array of authentication names (e.g., AUTH_SCHEME_BASIC, AUTH_SCHEME_BEARER, AUTH_SCHEME_API_KEY)
     */
    public void setAuthNames(String[] authNames) {
        this.authNames = authNames;
    }

    /**
     * Get the authentication names used for API calls.
     *
     * @return Array of authentication names
     */
    public String[] getAuthNames() {
        return authNames;
    }

    /**
     * Build call for create
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest createResourceCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        Object localVarPostBody = body;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {"application/scim+json"};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        return scimApiClient.buildCall("POST", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, authNames);
    }

    /**
     * Build call for delete
     *
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest deleteResourceCall() throws ScimApiException {

        Object localVarPostBody = null;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);
        final String[] localVarContentTypes = {};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        return scimApiClient.buildCall("DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, authNames);
    }

    /**
     * Build call
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest getResourceCall(List<String> attributes, List<String> excludedAttributes) throws ScimApiException {

        Object localVarPostBody = null;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        return scimApiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, authNames);
    }

    /**
     * Build call
     *
     * @param body (optional)
     * @return Call to execute
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest getResourcesByPostCall(String body) throws ScimApiException {

        Object localVarPostBody = body;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {"application/scim+json"};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        return scimApiClient.buildCall("POST", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, authNames);
    }

    /**
     * Build call for update.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @param httpMethod         HTTP Method for update operation. (e.g., PUT, PATCH)
     * @return Call to execute.
     * @throws ScimApiException If fail to serialize the request body object.
     */
    public HttpUriRequest updateResourceCall(List<String> attributes, List<String> excludedAttributes, String body,
                                             String httpMethod) throws ScimApiException {

        String method = StringUtils.isNotBlank(httpMethod) ? httpMethod.trim().toUpperCase() : "PUT";
        Object localVarPostBody = body;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (attributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "attributes", attributes));
        if (excludedAttributes != null)
            localVarQueryParams.addAll(scimApiClient.parameterToPairs("csv", "excludedAttributes", excludedAttributes));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {"application/scim+json"};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        return scimApiClient.buildCall(method, localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, authNames);
    }

    /**
     * Build call for update.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return Call to execute.
     * @throws ScimApiException If fail to serialize the request body object.
     */
    public HttpUriRequest updateResourceCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return updateResourceCall(attributes, excludedAttributes, body, "PUT");
    }

    /**
     * Build call.
     *
     * @return Call to execute.
     * @throws ScimApiException If fail to serialize the request body object.
     */
    public HttpUriRequest getResourceTypeCall() throws ScimApiException {

        Object localVarPostBody = null;
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json", "application/scim+json"};
        final String localVarAccept = scimApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {};
        final String localVarContentType = scimApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        return scimApiClient.buildCall("GET", localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarFormParams, authNames);
    }
}

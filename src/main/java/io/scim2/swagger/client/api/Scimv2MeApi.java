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
import io.scim2.swagger.client.ScimApiClient;
import io.scim2.swagger.client.ScimApiException;
import io.scim2.swagger.client.ScimApiResponse;
import io.scim2.swagger.client.Configuration;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import java.lang.reflect.Type;
import java.util.List;

public class Scimv2MeApi extends Scimv2BaseApi {

    public Scimv2MeApi() {
        this(Configuration.getDefaultScimApiClient());
    }

    public Scimv2MeApi(ScimApiClient scimApiClient) {
        super(scimApiClient);
    }

    private HttpUriRequest createMeValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return createResourceCall(attributes, excludedAttributes, body);
    }

    /**
     * Return the user which was anonymously created
     * Returns HTTP 201 if the user is successfully created.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String createMe(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        ScimApiResponse<String> resp = createMeWithHttpInfo(attributes, excludedAttributes, body);
        return resp.getData();
    }

    /**
     * Return the user which was anonymously created
     * Returns HTTP 201 if the user is successfully created.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body               (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> createMeWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                        String body) throws ScimApiException {
        HttpUriRequest call = createMeValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest deleteMeValidateBeforeCall() throws ScimApiException {

        return deleteResourceCall();
    }

    /**
     * Delete the authenticated user.
     * Returns HTTP 204 if the user is successfully deleted.
     *
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void deleteMe() throws ScimApiException {

        deleteMeWithHttpInfo();
    }

    /**
     * Delete the authenticated user.
     * Returns HTTP 204 if the user is successfully deleted.
     *
     * @return ScimApiResponse&lt;Void&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<Void> deleteMeWithHttpInfo() throws ScimApiException {

        HttpUriRequest call = deleteMeValidateBeforeCall();
        return scimApiClient.execute(call);
    }

    private HttpUriRequest getMeValidateBeforeCall(List<String> attributes, List<String> excludedAttributes)
            throws ScimApiException {

        return getResourceCall(attributes, excludedAttributes);
    }

    /**
     * Return the authenticated user.
     * Returns HTTP 200 if the user is found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getMe(List<String> attributes, List<String> excludedAttributes) throws ScimApiException {

        ScimApiResponse<String> resp = getMeWithHttpInfo(attributes, excludedAttributes);
        return resp.getData();
    }

    /**
     * Return the authenticated user.
     * Returns HTTP 200 if the user is found.
     *
     * @param attributes         SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getMeWithHttpInfo(List<String> attributes, List<String> excludedAttributes)
            throws ScimApiException {
        HttpUriRequest call = getMeValidateBeforeCall(attributes, excludedAttributes);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }

    private HttpUriRequest updateMeValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {

        return updateResourceCall(attributes, excludedAttributes, body);
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
    public String updateMe(List<String> attributes, List<String> excludedAttributes, String body)
            throws ScimApiException {
        ScimApiResponse<String> resp = updateMeWithHttpInfo(attributes, excludedAttributes, body);
        return resp.getData();
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
    public ScimApiResponse<String> updateMeWithHttpInfo(List<String> attributes, List<String> excludedAttributes,
                                                        String body) throws ScimApiException {
        HttpUriRequest call = updateMeValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }
}

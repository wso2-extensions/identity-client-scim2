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
import io.scim2.swagger.client.ScimApiClient;
import io.scim2.swagger.client.ScimApiException;
import io.scim2.swagger.client.ScimApiResponse;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

import java.lang.reflect.Type;

public class Scimv2ResourceTypeApi extends Scimv2BaseApi {

    public Scimv2ResourceTypeApi() {
        this(Configuration.getDefaultScimApiClient());
    }

    public Scimv2ResourceTypeApi(ScimApiClient scimApiClient) {
        super(scimApiClient);
    }

    private HttpUriRequest getResourceTypeValidateBeforeCall() throws ScimApiException {

        return getResourceTypeCall();
    }

    /**
     * Return the ResourceType schema.
     * Returns HTTP 200 if the schema is found.
     *
     * @return String
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getResourceType() throws ScimApiException {
        ScimApiResponse<String> resp = getResourceTypeWithHttpInfo();
        return resp.getData();
    }

    /**
     * Return the ResourceType schema.
     * Returns HTTP 200 if the schema is found.
     *
     * @return ScimApiResponse&lt;String&gt;
     * @throws ScimApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ScimApiResponse<String> getResourceTypeWithHttpInfo() throws ScimApiException {

        HttpUriRequest call = getResourceTypeValidateBeforeCall();
        Type localVarReturnType = new TypeToken<String>() {}.getType();
        return scimApiClient.execute(call, localVarReturnType);
    }
}

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
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;

import java.lang.reflect.Type;
import java.util.List;

public class Scimv2BulkApi extends Scimv2BaseApi {

    public Scimv2BulkApi() {

        this(Configuration.getDefaultApiClient());
    }

    public Scimv2BulkApi(ApiClient apiClient) {

        super(apiClient);
    }
    
    private Call createBulkValidateBeforeCall(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {

        Call call = createResourceCall(attributes, excludedAttributes, body);
        return call;
    }

    /**
     * Return the bulk which was created.
     * Returns HTTP 201 if the bulk is successfully created.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String createBulk(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        ApiResponse<String> resp = createBulkWithHttpInfo(attributes, excludedAttributes, body);
        return resp.getData();
    }

    /**
     * Return the bulk which was created.
     * Returns HTTP 201 if the bulk is successfully created.
     * @param attributes SCIM defined attributes parameter. (optional)
     * @param excludedAttributes SCIM defined excludedAttribute parameter. (optional)
     * @param body  (optional)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> createBulkWithHttpInfo(List<String> attributes, List<String> excludedAttributes, String body) throws ApiException {
        Call call = createBulkValidateBeforeCall(attributes, excludedAttributes, body);
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
}

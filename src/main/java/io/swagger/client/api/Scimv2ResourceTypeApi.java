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

public class Scimv2ResourceTypeApi extends Scimv2BaseApi {

    public Scimv2ResourceTypeApi() {

        this(Configuration.getDefaultApiClient());
    }

    public Scimv2ResourceTypeApi(ApiClient apiClient) {

        super(apiClient);
    }
    
    private Call getResourceTypeValidateBeforeCall() throws ApiException {
        
        Call call = getResourceTypeCall();
        return call;
    }

    /**
     * Return the ResourceType schema.
     * Returns HTTP 200 if the schema is found.
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getResourceType() throws ApiException {

        ApiResponse<String> resp = getResourceTypeWithHttpInfo();
        return resp.getData();
    }

    /**
     * Return the ResourceType schema.
     * Returns HTTP 200 if the schema is found.
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getResourceTypeWithHttpInfo() throws ApiException {

        Call call = getResourceTypeValidateBeforeCall();
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
}

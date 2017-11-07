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

public class Scimv2ServiceProviderConfigApi {
    private ApiClient apiClient;

    public Scimv2ServiceProviderConfigApi() {
        this(Configuration.getDefaultApiClient());
    }

    public Scimv2ServiceProviderConfigApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for getServiceProviderConfig
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public Call getServiceProviderConfigCall() throws ApiException {

        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/scim/v2/ServiceProviderConfig";

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
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames);
    }
    
    private Call getServiceProviderConfigValidateBeforeCall() throws ApiException {

        Call call = getServiceProviderConfigCall();
        return call;
    }

    /**
     * Return the ServiceProviderConfig schema.
     * Returns HTTP 200 if the schema is found.
     * @return String
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public String getServiceProviderConfig() throws ApiException {
        ApiResponse<String> resp = getServiceProviderConfigWithHttpInfo();
        return resp.getData();
    }

    /**
     * Return the ServiceProviderConfig schema.
     * Returns HTTP 200 if the schema is found.
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<String> getServiceProviderConfigWithHttpInfo() throws ApiException {
        Call call = getServiceProviderConfigValidateBeforeCall();
        Type localVarReturnType = new TypeToken<String>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
}

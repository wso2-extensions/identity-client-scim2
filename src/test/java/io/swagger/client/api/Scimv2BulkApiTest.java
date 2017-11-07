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

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for Scimv2BulkApi
 */
@Ignore
public class Scimv2BulkApiTest {

    private final Scimv2BulkApi api = new Scimv2BulkApi();

    
    /**
     * Return the bulk which was created.
     *
     * Returns HTTP 201 if the bulk is successfully created.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createBulkTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.createBulk(attributes, excludedAttributes, body);

        // TODO: test validations
    }
    
}

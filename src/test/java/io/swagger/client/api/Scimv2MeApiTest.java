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
 * API tests for Scimv2MeApi
 */
@Ignore
public class Scimv2MeApiTest {

    private final Scimv2MeApi api = new Scimv2MeApi();

    
    /**
     * Return the user which was anonymously created
     *
     * Returns HTTP 201 if the user is successfully created.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createMeTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.createMe(attributes, excludedAttributes, body);

        // TODO: test validations
    }
    
    /**
     * Delete the authenticated user.
     *
     * Returns HTTP 204 if the user is successfully deleted.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteMeTest() throws ApiException {
        api.deleteMe();

        // TODO: test validations
    }
    
    /**
     * Return the authenticated user.
     *
     * Returns HTTP 200 if the user is found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getMeTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        api.getMe(attributes, excludedAttributes);

        // TODO: test validations
    }
    
    /**
     * Return the updated user
     *
     * Returns HTTP 404 if the user is not found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateMeTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.updateMe(attributes, excludedAttributes, body);

        // TODO: test validations
    }
    
}

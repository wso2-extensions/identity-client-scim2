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
 * API tests for Scimv2UsersApi
 */
@Ignore
public class Scimv2UsersApiTest {

    private final Scimv2UsersApi api = new Scimv2UsersApi();

    
    /**
     * Return the user which was created
     *
     * Returns HTTP 201 if the user is successfully created.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createUserTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.createUser(attributes, excludedAttributes, body);

        // TODO: test validations
    }
    
    /**
     * Delete the user with the given id
     *
     * Returns HTTP 204 if the user is successfully deleted.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteUserTest() throws ApiException {
        String id = null;
        api.deleteUser(id);

        // TODO: test validations
    }
    
    /**
     * Return users according to the filter, sort and pagination parameters
     *
     * Returns HTTP 404 if the users are not found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getUserTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String filter = null;
        Integer startIndex = null;
        Integer count = null;
        String sortBy = null;
        String sortOder = null;
        api.getUser(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);

        // TODO: test validations
    }
    
    /**
     * Return the user with the given id
     *
     * Returns HTTP 200 if the user is found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getUserByIdTest() throws ApiException {
        String id = null;
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        api.getUserById(id, attributes, excludedAttributes);

        // TODO: test validations
    }
    
    /**
     * Return users according to the filter, sort and pagination parameters
     *
     * Returns HTTP 404 if the users are not found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getUsersByPostTest() throws ApiException {
        String body = null;
        api.getUsersByPost(body);

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
    public void updateUserTest() throws ApiException {
        String id = null;
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.updateUser(id, attributes, excludedAttributes, body, "PUT");

        // TODO: test validations
    }
    
}

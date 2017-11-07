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
 * API tests for Scimv2GroupsApi
 */
@Ignore
public class Scimv2GroupsApiTest {

    private final Scimv2GroupsApi api = new Scimv2GroupsApi();

    
    /**
     * Return the group which was created
     *
     * Returns HTTP 201 if the group is successfully created.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createGroupTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.createGroup(attributes, excludedAttributes, body);

        // TODO: test validations
    }
    
    /**
     * Delete the group with the given id
     *
     * Returns HTTP 204 if the group is successfully deleted.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteGroupTest() throws ApiException {
        String id = null;
        api.deleteGroup(id);

        // TODO: test validations
    }
    
    /**
     * Return groups according to the filter, sort and pagination parameters
     *
     * Returns HTTP 404 if the groups are not found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getGroupTest() throws ApiException {
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String filter = null;
        Integer startIndex = null;
        Integer count = null;
        String sortBy = null;
        String sortOder = null;
        api.getGroup(attributes, excludedAttributes, filter, startIndex, count, sortBy, sortOder);

        // TODO: test validations
    }
    
    /**
     * Return the group with the given id
     *
     * Returns HTTP 200 if the group is found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getGroupByIdTest() throws ApiException {
        String id = null;
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        api.getGroupById(id, attributes, excludedAttributes);

        // TODO: test validations
    }
    
    /**
     * Return groups according to the filter, sort and pagination parameters
     *
     * Returns HTTP 404 if the groups are not found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getGroupsByPostTest() throws ApiException {
        String body = null;
        api.getGroupsByPost(body);

        // TODO: test validations
    }
    
    /**
     * Return the updated group
     *
     * Returns HTTP 404 if the group is not found.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateGroupTest() throws ApiException {
        String id = null;
        List<String> attributes = null;
        List<String> excludedAttributes = null;
        String body = null;
        api.updateGroup(id, attributes, excludedAttributes, body);

        // TODO: test validations
    }
    
}

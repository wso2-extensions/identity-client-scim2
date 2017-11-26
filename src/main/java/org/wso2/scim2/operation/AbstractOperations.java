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

package org.wso2.scim2.operation;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.Scimv2GroupsApi;
import io.swagger.client.api.Scimv2UsersApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.config.CharonConfiguration;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.util.SCIM2CommonConstants;
import org.wso2.scim2.util.SCIMClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractOperations {

    public static final String USER_FILTER = "userName%20Eq%20";
    public static final String GROUP_FILTER = "displayName%20Eq%20";

    private static Logger logger = LoggerFactory.getLogger(AbstractOperations.class
            .getName());

    protected SCIMObject scimObject;
    protected SCIMProvider provider;
    protected String userEPURL;
    protected String groupEPURL;
    protected String userName;
    protected Map<String, Object> additionalInformation;
    protected ApiClient client;

    public AbstractOperations(SCIMProvider scimProvider, SCIMObject object,
                           Map<String, Object> additionalInformation) {

        provider = scimProvider;
        scimObject = object;
        this.additionalInformation = additionalInformation;

        userEPURL = provider.getProperty(SCIM2CommonConstants.ELEMENT_NAME_USER_ENDPOINT);
        groupEPURL = provider.getProperty(SCIM2CommonConstants.ELEMENT_NAME_GROUP_ENDPOINT);
        userName = provider.getProperty(SCIMConstants.UserSchemaConstants.USER_NAME);

        client = new ApiClient();
        client.setUsername(userName);
        client.setPassword(provider.getProperty(SCIMConstants.UserSchemaConstants.PASSWORD));
    }

    public List<SCIMObject> listWithGet(List<String> attributes,
                                   List<String> excludedAttributes, String filter, int startIndex,
                                   int count, String sortBy, String sortOrder, int resourceType) throws ApiException, AbstractCharonException, IOException {


        List<SCIMObject> returnedSCIMObject = new ArrayList<>();

        SCIMClient scimClient = new SCIMClient();

        if (startIndex < 1) {
            startIndex = 1;
        }

        if (count == 0) {
            count = CharonConfiguration.getInstance()
                    .getCountValueForPagination();
        }

        if (sortOrder != null) {
            if (!(sortOrder
                    .equalsIgnoreCase(SCIMConstants.OperationalConstants.ASCENDING) || sortOrder
                    .equalsIgnoreCase(SCIMConstants.OperationalConstants.DESCENDING))) {
                String error = " Invalid sortOrder value is specified";
                throw new BadRequestException(error,
                        ResponseCodeConstants.INVALID_VALUE);
            }
        }

        if (sortOrder == null && sortBy != null) {
            sortOrder = SCIMConstants.OperationalConstants.ASCENDING;
        }

        ApiResponse<String> response;
        if(resourceType == SCIM2CommonConstants.USER) {
            client.setURL(userEPURL);
            response = new Scimv2UsersApi(client).getUser(attributes,
                    excludedAttributes, filter, startIndex, count, sortBy,
                    sortOrder);
        } else {
            client.setURL(groupEPURL);
            response = new Scimv2GroupsApi(client).getGroup(attributes,
                    excludedAttributes, filter, startIndex, count, sortBy,
                    sortOrder);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("SCIM - filter operation inside 'get' " +
                    "returned with response code: " + response.getStatusCode());
            logger.debug("Filter Response: " + response.getData());
        }

        if (scimClient.evaluateResponseStatus(response.getStatusCode())) {

            returnedSCIMObject = scimClient.decodeSCIMResponseWithListedResource(response.getData(), SCIMConstants.JSON, resourceType);

            if (returnedSCIMObject.isEmpty()) {
                String error = "No results found.";
                throw new NotFoundException(error);
            }
        } else {
            //decode scim exception and extract the specific error message.
            AbstractCharonException exception =
                    scimClient.decodeSCIMException(
                            response.getData(), SCIMConstants.JSON);
            logger.error(exception.getMessage());
        }

        return returnedSCIMObject;
    }

    public void handleSCIMErrorResponse(ApiResponse<String> response) throws CharonException {

        SCIMClient scimClient = new SCIMClient();
        if (!scimClient.evaluateResponseStatus(response.getStatusCode())) {
            //decode scim exception and extract the specific error message.
            AbstractCharonException exception =
                    scimClient.decodeSCIMException(
                            response.getData(), SCIMConstants.JSON);
            logger.error(exception.getMessage());
        }
    }
}

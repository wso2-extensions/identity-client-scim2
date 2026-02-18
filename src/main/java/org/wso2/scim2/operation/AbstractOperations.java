/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import io.scim2.swagger.client.ScimApiClient;
import io.scim2.swagger.client.ScimApiException;
import io.scim2.swagger.client.ScimApiResponse;
import io.scim2.swagger.client.api.Scimv2GroupsApi;
import io.scim2.swagger.client.api.Scimv2UsersApi;
import io.scim2.swagger.client.auth.ApiKeyAuth;
import io.scim2.swagger.client.auth.HttpBasicAuth;
import io.scim2.swagger.client.auth.OAuth;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.charon3.core.config.CharonConfiguration;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.util.AuthenticationType;
import org.wso2.scim2.util.SCIM2CommonConstants;
import org.wso2.scim2.util.SCIMClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractOperations implements AutoCloseable {

    public static final String USER_FILTER = "userName Eq ";
    public static final String GROUP_FILTER = "displayName Eq ";

    private static final Log logger = LogFactory.getLog(AbstractOperations.class.getName());

    protected SCIMObject scimObject;
    protected SCIMProvider provider;
    protected String userEPURL;
    protected String groupEPURL;
    protected String userName;
    protected Map<String, Object> additionalInformation;
    protected ScimApiClient client;
    protected String[] authNames;

    public AbstractOperations(SCIMProvider scimProvider, SCIMObject object,
                              Map<String, Object> additionalInformation) throws ScimApiException {

        provider = scimProvider;
        scimObject = object;
        this.additionalInformation = additionalInformation;

        userEPURL = provider.getProperty(SCIM2CommonConstants.ELEMENT_NAME_USER_ENDPOINT);
        groupEPURL = provider.getProperty(SCIM2CommonConstants.ELEMENT_NAME_GROUP_ENDPOINT);
        userName = provider.getProperty(SCIMConstants.UserSchemaConstants.USER_NAME);

        client = new ScimApiClient();
        configureAuthentication(client);
    }

    /**
     * Configure authentication on the SCIM API client based on authentication type.
     *
     * @param client ScimApiClient to configure.
     * @throws ScimApiException If authentication configuration fails.
     */
    private void configureAuthentication(ScimApiClient client) throws ScimApiException {

        String authTypeValue = provider.getProperty(SCIM2CommonConstants.AUTHENTICATION_TYPE);

        // Parse authentication type from string value, default to BASIC if not specified.
        AuthenticationType authType = AuthenticationType.fromValue(authTypeValue);

        switch (authType) {
            case BASIC:
                // Configure BASIC authentication.
                HttpBasicAuth basicAuth = (HttpBasicAuth) client.getAuthentication(
                        SCIM2CommonConstants.AUTH_SCHEME_BASIC);
                if (basicAuth != null) {
                    String username = provider.getProperty(SCIMConstants.UserSchemaConstants.USER_NAME);
                    String password = provider.getProperty(SCIMConstants.UserSchemaConstants.PASSWORD);
                    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                        basicAuth.setUsername(username);
                        basicAuth.setPassword(password);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Configured BASIC authentication for SCIM client");
                        }
                    }
                }
                authNames = new String[]{SCIM2CommonConstants.AUTH_SCHEME_BASIC};
                break;
            case BEARER:
                // Configure BEARER authentication.
                OAuth bearerAuth = (OAuth) client.getAuthentication(SCIM2CommonConstants.AUTH_SCHEME_BEARER);
                if (bearerAuth != null) {
                    String accessToken = provider.getProperty(SCIM2CommonConstants.ACCESS_TOKEN);
                    if (StringUtils.isNotBlank(accessToken)) {
                        bearerAuth.setAccessToken(accessToken);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Configured BEARER authentication for SCIM client");
                        }
                    }
                }
                authNames = new String[]{SCIM2CommonConstants.AUTH_SCHEME_BEARER};
                break;
            case API_KEY:
                // Configure API_KEY authentication.
                String apiKeyHeader = provider.getProperty(SCIM2CommonConstants.API_KEY_HEADER);
                String apiKeyValue = provider.getProperty(SCIM2CommonConstants.API_KEY_VALUE);

                if (StringUtils.isNotBlank(apiKeyHeader) && StringUtils.isNotBlank(apiKeyValue)) {
                    ApiKeyAuth apiKeyAuth = (ApiKeyAuth) client.getAuthentication(
                            SCIM2CommonConstants.AUTH_SCHEME_API_KEY);
                    if (apiKeyAuth != null) {
                        apiKeyAuth.setApiKey(apiKeyValue);
                        apiKeyAuth.setApiKeyPrefix(null); // No prefix, just the key.
                        // Update the header name.
                        apiKeyAuth = new ApiKeyAuth("header", apiKeyHeader);
                        apiKeyAuth.setApiKey(apiKeyValue);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Configured API_KEY authentication for SCIM client with header: " +
                                    apiKeyHeader);
                        }
                    }
                }
                authNames = new String[]{SCIM2CommonConstants.AUTH_SCHEME_API_KEY};
                break;
            case NONE:
                // No authentication.
                if (logger.isDebugEnabled()) {
                    logger.debug("No authentication configured for SCIM client");
                }
                authNames = new String[]{};
                break;
            default:
                logger.warn("Unsupported authentication type: " + authTypeValue +
                        ". Defaulting to BASIC authentication.");
                // Fallback to BASIC authentication.
                HttpBasicAuth defaultBasicAuth = (HttpBasicAuth) client.getAuthentication(
                        SCIM2CommonConstants.AUTH_SCHEME_BASIC);
                if (defaultBasicAuth != null) {
                    String username = provider.getProperty(SCIMConstants.UserSchemaConstants.USER_NAME);
                    String password = provider.getProperty(SCIMConstants.UserSchemaConstants.PASSWORD);
                    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                        defaultBasicAuth.setUsername(username);
                        defaultBasicAuth.setPassword(password);
                    }
                }
                authNames = new String[]{SCIM2CommonConstants.AUTH_SCHEME_BASIC};
                break;
        }
    }

    public List<SCIMObject> listWithGet(List<String> attributes, List<String> excludedAttributes, String filter,
                                        int startIndex, int count, String sortBy, String sortOrder, int resourceType)
            throws ScimApiException, AbstractCharonException, IOException {

        List<SCIMObject> returnedSCIMObject = new ArrayList<SCIMObject>();
        SCIMClient scimClient = new SCIMClient();
        if (startIndex < 1) {
            startIndex = 1;
        }
        if (count == 0) {
            count = CharonConfiguration.getInstance().getCountValueForPagination();
        }
        if (sortOrder != null) {
            if (!(sortOrder
                    .equalsIgnoreCase(SCIMConstants.OperationalConstants.ASCENDING) || sortOrder
                    .equalsIgnoreCase(SCIMConstants.OperationalConstants.DESCENDING))) {
                String error = " Invalid sortOrder value is specified";
                throw new BadRequestException(error, ResponseCodeConstants.INVALID_VALUE);
            }
        }
        if (sortOrder == null && sortBy != null) {
            sortOrder = SCIMConstants.OperationalConstants.ASCENDING;
        }
        ScimApiResponse<String> response;
        if (resourceType == SCIM2CommonConstants.USER) {
            client.setURL(userEPURL);
            Scimv2UsersApi usersApi = new Scimv2UsersApi(client);
            usersApi.setAuthNames(authNames);
            response = usersApi.getUser(attributes, excludedAttributes, filter, startIndex, count,
                    sortBy, sortOrder);
        } else {
            client.setURL(groupEPURL);
            Scimv2GroupsApi groupsApi = new Scimv2GroupsApi(client);
            groupsApi.setAuthNames(authNames);
            response = groupsApi.getGroup(attributes, excludedAttributes, filter, startIndex, count,
                    sortBy, sortOrder);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SCIM - filter operation inside 'get' " + "returned with response code: " +
                    response.getStatusCode());
            logger.debug("Filter Response: " + response.getData());
        }
        if (scimClient.evaluateResponseStatus(response.getStatusCode())) {
            returnedSCIMObject = scimClient.decodeSCIMResponseWithListedResource(response.getData(), SCIMConstants.JSON,
                    resourceType);
            if (returnedSCIMObject.isEmpty()) {
                String error = "No results found.";
                throw new NotFoundException(error);
            }
        } else {
            //decode scim exception and extract the specific error message.
            AbstractCharonException exception =
                    scimClient.decodeSCIMException(response.getData(), SCIMConstants.JSON);
            logger.error(exception.getMessage());
        }
        return returnedSCIMObject;
    }

    public void handleSCIMErrorResponse(ScimApiResponse<String> response) throws CharonException {

        SCIMClient scimClient = new SCIMClient();
        if (!scimClient.evaluateResponseStatus(response.getStatusCode())) {
            //decode scim exception and extract the specific error message.
            AbstractCharonException exception =
                    scimClient.decodeSCIMException(response.getData(), SCIMConstants.JSON);
            logger.error(exception.getMessage());
        }
    }

    /**
     * Close the HTTP client and release all associated resources.
     * This method should be called when the operation is complete.
     */
    @Override
    public void close() {

        if (client != null) {
            try {
                client.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("Successfully closed SCIM API client");
                }
            } catch (IOException e) {
                logger.warn("Error closing SCIM API client", e);
            }
        }
    }
}

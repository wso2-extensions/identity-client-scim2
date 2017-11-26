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

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.Scimv2UsersApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.util.SCIM2CommonConstants;
import org.wso2.scim2.util.SCIMClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserOperations extends AbstractOperations {

    private static Logger logger = LoggerFactory.getLogger(UserOperations.class
            .getName());

    public UserOperations(SCIMProvider scimProvider, SCIMObject object,
                          Map<String, Object> additionalInformation) {

        super(scimProvider, object, additionalInformation);
    }

    public void createUser() throws IdentitySCIMException {

        try {
            SCIMClient scimClient = new SCIMClient();
            String encodedUser = scimClient.encodeSCIMObject((AbstractSCIMObject) scimObject,
                    SCIMConstants.JSON);

            if (logger.isDebugEnabled()) {
                logger.debug("User to provision : useName" + userName);
            }

            client.setURL(userEPURL);
            Scimv2UsersApi api = new Scimv2UsersApi(client);
            ApiResponse<String> response = api.createUser(null, null,
                    encodedUser);
            logger.info("SCIM - create user operation returned with response code: " + response.getStatusCode());
            if (logger.isDebugEnabled()) {
                logger.debug("Create User Response: " + response.getData());
            }

            if (scimClient.evaluateResponseStatus(response.getStatusCode())) {
                //try to decode the scim object to verify that it gets decoded without issue.
                scimClient.decodeSCIMResponse(response.getData(), SCIMConstants.JSON, SCIM2CommonConstants.USER);
            } else {
                //decode scim exception and extract the specific error message.
                AbstractCharonException exception =
                        scimClient.decodeSCIMException(response.getData(), SCIMConstants.JSON);
                logger.error(exception.getMessage());
            }

        } catch (CharonException e) {
            throw new IdentitySCIMException(
                    "Error in encoding the object to be provisioned for user with userName: "
                            + userName, e);
        } catch (ApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (AbstractCharonException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with userName: "
                            + userName, e);
        }
    }

    public void deleteUser() throws IdentitySCIMException {

        try {
            String filter = USER_FILTER + ((User) scimObject).getUserName();
            List<User> users = (List<User>) (List<?>) listWithGet(null, null, filter, 1, 1, null, null, SCIM2CommonConstants.USER);
            User user;

            if (users != null && users.size() > 0) {
                user = users.get(0);
                String userId = user.getId();
                if (userId == null) {
                    logger.error("Trying to delete a user entry which doesn't support SCIM. " +
                            "Usually internal carbon User entries such as admin role doesn't support SCIM 2.0 attributes.");
                    return;
                }

                client.setURL(userEPURL+"/"+userId);
                Scimv2UsersApi api = new Scimv2UsersApi(client);
                ApiResponse response = api.deleteUser();

                handleSCIMErrorResponse(response);
            } else {
                logger.error("No Users found with userName: " + ((User) scimObject).getUserName());
            }
        } catch (ApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (IOException | AbstractCharonException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with id: " + userName, e);
        }
    }

    public void updateUser(String httpMethod) throws IdentitySCIMException {

        try {
            String filter = USER_FILTER + ((User) scimObject).getUserName();
            List<User> users = (List<User>) (List<?>) listWithGet(null, null, filter, 1, 1, null, null, SCIM2CommonConstants.USER);
            User user;

            SCIMClient scimClient = new SCIMClient();
            if (users != null && users.size() > 0) {
                user = users.get(0);
                String userId = user.getId();
                if (userId == null) {
                    logger.error("Trying to update a user entry which doesn't support SCIM. " +
                            "Usually internal carbon User entries such as admin role doesn't support SCIM 2.0 attributes.");
                    return;
                }

                String encodedObject;

                if (httpMethod.equals("PUT")) {
                    encodedObject = scimClient.encodeSCIMObject((AbstractSCIMObject) scimObject, SCIMConstants.JSON);
                } else {
                    logger.error("Not supported update operation type: " + httpMethod);
                    return;
                }

                client.setURL(userEPURL+"/"+userId);
                Scimv2UsersApi api = new Scimv2UsersApi(client);
                ApiResponse<String> response = api.updateUser(null, null, encodedObject, httpMethod);

                if (scimClient.evaluateResponseStatus(response.getStatusCode())) {
                    scimClient.decodeSCIMResponse(response.getData(), SCIMConstants.JSON, SCIM2CommonConstants.USER);
                } else {
                    AbstractCharonException exception =
                            scimClient.decodeSCIMException(response.getData(), SCIMConstants.JSON);
                    logger.error(exception.getMessage());
                }
            } else {
                logger.error("No Users found with userName: " + ((User) scimObject).getUserName());
            }

        } catch (CharonException e) {
            throw new IdentitySCIMException(
                    "Error in encoding the object to be provisioned for user : "
                            + userName, e);
        } catch (ApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (IOException | AbstractCharonException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with id: " + userName, e);
        }
    }

    public void updateUser() throws IdentitySCIMException {

        this.updateUser("PUT");
    }
}

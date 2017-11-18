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

import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.Scimv2UsersApi;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.encoder.JSONDecoder;
import org.wso2.charon3.core.encoder.JSONEncoder;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMResourceSchemaManager;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;
import org.wso2.charon3.core.utils.codeutils.PatchOperation;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.model.Error;
import org.wso2.scim2.util.PatchOperationEncoder;
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

    public User createUser() throws IdentitySCIMException {

        Gson gson;
        User user = null;

        try {

            String encodedUser = new JSONEncoder().encodeSCIMObject(scimObject);

            if (logger.isDebugEnabled()) {
                logger.debug("User to provision : useName" + userName);
            }

            Scimv2UsersApi api = new Scimv2UsersApi(client);
            ApiResponse<String> response = api.createUser(null, null,
                    encodedUser);
            if (logger.isDebugEnabled()) {
                logger.debug("Create User Response: " + response);
            }

            if (response.getStatusCode() == 201) {
                JSONDecoder jsonDecoder = new JSONDecoder();
                SCIMResourceTypeSchema schema = SCIMResourceSchemaManager
                        .getInstance().getUserResourceSchema();
                user = (User) jsonDecoder.decodeResource(response.getData(),
                        schema, new User());
            }

        } catch (CharonException e) {
            throw new IdentitySCIMException(
                    "Error in encoding the object to be provisioned for user with id: "
                            + userName, e);
        } catch (ApiException e) {
            gson = new Gson();
            Error err = gson.fromJson(e.getResponseBody(), Error.class);
            throw new IdentitySCIMException(err.getDetail(), e);

        } catch (BadRequestException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with id: "
                            + userName, e);
        } catch (InternalErrorException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with id: "
                            + userName, e);
        }

        return user;
    }

    public void deleteUser() throws IdentitySCIMException {

        if (this.scimObject != null) {
            Gson gson = new Gson();
            String userId = null;
            try {
                String filter = USER_FILTER + ((User) scimObject).getUserName();
                List<User> users = (List<User>)(List<?>)listWithGet(null, null, filter, 1, 1, null, null, SCIMClient.USER);
                User user = users.get(0);

                userId = user.getId();
                if (userId == null) {
                    logger.error("Trying to delete a user entry which doesn't support SCIM. " +
                            "Usually internal carbon User entries such as admin role doesn't support SCIM 2.0 attributes.");
                    return;
                }

                Scimv2UsersApi api = new Scimv2UsersApi(client);
                api.deleteUser(userId);

            } catch (AbstractCharonException e) {
                throw new IdentitySCIMException("Error in encoding the object", e);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                Error err = gson.fromJson(e.getResponseBody(), Error.class);
                throw new IdentitySCIMException(err.getDetail(), e);
            }
        }
    }

    public User updateUser(String httpMethod) throws IdentitySCIMException {

        User updatedUser = null;
        Gson gson;
        try {
            String filter = USER_FILTER + ((User) scimObject).getUserName();
            List<User> users = (List<User>)(List<?>)listWithGet(null, null, filter, 1, 1, null, null, SCIMClient.USER);
            User user = users.get(0);

            String userId = user.getId();
            if (userId == null) {
                logger.error("Trying to update a user entry which doesn't support SCIM. " +
                        "Usually internal carbon User entries such as admin role doesn't support SCIM 2.0 attributes.");
                return updatedUser;
            }

            String encodedObject = null;

            if(httpMethod.equals("PUT")) {
                encodedObject = new JSONEncoder().encodeSCIMObject(scimObject);
            } else if(httpMethod.equals("PATCH")) {
                List<PatchOperation> patchOperations = provider.getPatchOperationList();
                encodedObject = new PatchOperationEncoder().encodeRequest(patchOperations);
            } else {
                logger.error("Not supported update operation type: " + httpMethod);
            }


            Scimv2UsersApi api = new Scimv2UsersApi(client);
            ApiResponse<String> response = api.updateUser(userId, null, null, encodedObject, httpMethod);

            if (response.getStatusCode() == 201) {
                JSONDecoder jsonDecoder = new JSONDecoder();
                SCIMResourceTypeSchema schema = SCIMResourceSchemaManager
                        .getInstance().getUserResourceSchema();
                updatedUser = (User) jsonDecoder.decodeResource(response.getData(),
                        schema, new User());

            }

        } catch (AbstractCharonException e) {
            throw new IdentitySCIMException(
                    "Error in encoding the object to be provisioned for user : "
                            + userName, e);
        } catch (ApiException e) {
            gson = new Gson();
            Error err = gson.fromJson(e.getResponseBody(), Error.class);
            throw new IdentitySCIMException(err.getDetail(), e);
        } catch (IOException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (JSONException e) {
            throw new IdentitySCIMException("Error while encoding patch operations");
        }

        return updatedUser;
    }

    public User updateUser() throws IdentitySCIMException {

        return updateUser("PUT");
    }

    public User patchUser() throws IdentitySCIMException {

        return updateUser("PATCH");
    }
}

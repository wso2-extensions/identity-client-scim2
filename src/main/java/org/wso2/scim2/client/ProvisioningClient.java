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

package org.wso2.scim2.client;

import io.scim2.swagger.client.ScimApiException;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.operation.GroupOperations;
import org.wso2.scim2.operation.UserOperations;

import java.util.Map;

/**
 * This class implements logic to initiate SCIM 2.0 provisioning operations to
 * other SCIM 2.0 provider endpoints. Since SCIM provisioning operations are
 * usually run asynchronously, this runs in a separate thread.
 */
public class ProvisioningClient {


    SCIMObject scimObject;
    SCIMProvider provider;
    private Map<String, Object> additionalProvisioningInformation;

    /**
     * Initialize parameters to be used in the SCIM operation which will be
     * invoked by the run operation of the thread.
     *
     * @param scimProvider
     * @param object
     * @param additionalInformation
     */
    public ProvisioningClient(SCIMProvider scimProvider, SCIMObject object,
                              Map<String, Object> additionalInformation) {

        provider = scimProvider;
        scimObject = object;
        additionalProvisioningInformation = additionalInformation;
    }

    /**
     * Provision the SCIM User Object passed to the provisioning client in the constructor, to the
     * SCIM Provider whose details are also sent at the initialization.
     */
    public void provisionCreateUser() throws IdentitySCIMException {

        UserOperations operation;
        try {
            operation = new UserOperations(provider, scimObject, additionalProvisioningInformation);
            operation.createUser();
        } catch (ScimApiException | IdentitySCIMException e) {
            throw new IdentitySCIMException("Unable to create the User.", e);
        }
    }

    public void provisionDeleteUser() throws IdentitySCIMException {

        UserOperations operation;
        try {
            operation = new UserOperations(provider, scimObject, additionalProvisioningInformation);
            operation.deleteUser();
        } catch (ScimApiException | IdentitySCIMException e) {
            throw new IdentitySCIMException("Unable to delete the User.", e);
        }
    }

    public void provisionUpdateUser() throws IdentitySCIMException {

        UserOperations operation;
        try {
            operation = new UserOperations(provider, scimObject, additionalProvisioningInformation);
            operation.updateUser();
        } catch (ScimApiException | IdentitySCIMException e) {
            throw new IdentitySCIMException("Unable to update the User.", e);
        }
    }

    public void provisionCreateGroup() throws IdentitySCIMException {

        GroupOperations operation;
        try {
            operation = new GroupOperations(provider, scimObject, additionalProvisioningInformation);
            operation.createGroup();
        } catch (ScimApiException | IdentitySCIMException e) {
            throw new IdentitySCIMException("Unable to create the Group.", e);
        }
    }

    public void provisionDeleteGroup() throws IdentitySCIMException {

        GroupOperations operation;
        try {
            operation = new GroupOperations(provider, scimObject, additionalProvisioningInformation);
            operation.deleteGroup();
        } catch (ScimApiException | IdentitySCIMException e) {
            throw new IdentitySCIMException("Unable to delete the Group.", e);
        }
    }

    public void provisionUpdateGroup() throws IdentitySCIMException {

        GroupOperations operation;
        try {
            operation = new GroupOperations(provider, scimObject, additionalProvisioningInformation);
            operation.updateGroup();
        } catch (ScimApiException | IdentitySCIMException e) {
            throw new IdentitySCIMException("Unable to update the Group.", e);
        }
    }
}

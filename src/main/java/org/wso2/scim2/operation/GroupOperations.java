/*
 * Copyright (c) 2018-2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.scim2.operation;

import io.scim2.swagger.client.ScimApiException;
import io.scim2.swagger.client.ScimApiResponse;
import io.scim2.swagger.client.api.Scimv2GroupsApi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.Group;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.utils.CopyUtil;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.util.CollectionUtils;
import org.wso2.scim2.util.SCIM2CommonConstants;
import org.wso2.scim2.util.SCIMClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GroupOperations extends AbstractOperations {

    private static final Log logger = LogFactory.getLog(UserOperations.class.getName());

    public GroupOperations(SCIMProvider scimProvider, SCIMObject object,
                           Map<String, Object> additionalInformation) throws ScimApiException {
        super(scimProvider, object, additionalInformation);
    }

    public void createGroup() throws IdentitySCIMException {

        try {
            SCIMClient scimClient = new SCIMClient();
            //get list of users in the group, if any, by userNames
            List<String> users = ((Group) scimObject).getMembersWithDisplayName();
            Group copiedGroup = null;
            if (CollectionUtils.isNotEmpty(users)) {
                //create a deep copy of the group since we are going to update the member ids
                copiedGroup = (Group) CopyUtil.deepCopy(scimObject);
                //delete existing members in the group since we are going to update it with
                copiedGroup.deleteAttribute(SCIMConstants.GroupSchemaConstants.MEMBERS);

                //get corresponding userIds
                for (String user : users) {
                    String filter = USER_FILTER + user;
                    try {
                        List<SCIMObject> filteredUsers = listWithGet(null, null, filter, 1, 1, null, null,
                                SCIM2CommonConstants.USER);
                        String userId = null;
                        for (SCIMObject filteredUser : filteredUsers) {
                            userId = ((User) filteredUser).getId();
                        }
                        copiedGroup.setMember(userId, user);
                    } catch (NotFoundException e) {
                        // Skip the not existing users from the SCIM2/groups create request.
                        logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                                "added to the group: " + copiedGroup.getDisplayName());
                    }
                }
            }

            String encodedGroup;
            if (copiedGroup != null) {
                encodedGroup = scimClient.encodeSCIMObject(copiedGroup,
                        SCIMConstants.JSON);
            } else {
                encodedGroup = scimClient.encodeSCIMObject((AbstractSCIMObject) scimObject, SCIMConstants.JSON);
            }
            client.setURL(groupEPURL);
            Scimv2GroupsApi api = new Scimv2GroupsApi(client);
            ScimApiResponse<String> response = api.createGroup(null, null, encodedGroup);
            logger.info("SCIM - create group operation returned with response code: " + response.getStatusCode());
            if (logger.isDebugEnabled()) {
                logger.debug("Create Group Response: " + response.getData());
            }
            if (scimClient.evaluateResponseStatus(response.getStatusCode())) {
                //try to decode the scim object to verify that it gets decoded without issue.
                scimClient.decodeSCIMResponse(response.getData(), SCIMConstants.JSON, SCIM2CommonConstants.GROUP);
            } else {
                //decode scim exception and extract the specific error message.
                AbstractCharonException exception =
                        scimClient.decodeSCIMException(response.getData(), SCIMConstants.JSON);
                logger.error(exception.getMessage());
            }
        } catch (IOException e) {
            throw new IdentitySCIMException("Error in provisioning 'create group' operation for user : " + userName, e);
        } catch (ScimApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch ( AbstractCharonException e){
            throw new IdentitySCIMException("Error in provisioning 'create group' operation for user : " + userName, e);
        }
    }

    public void deleteGroup() throws IdentitySCIMException {

        try {
            String filter = GROUP_FILTER + ((Group) scimObject).getDisplayName();
            List<Group> groups = (List<Group>) (List<?>) listWithGet(null, null, filter, 1, 1, null, null,
                    SCIM2CommonConstants.GROUP);
            if (groups != null && groups.size() > 0) {
                String groupId = groups.get(0).getId();
                if (groupId == null) {
                    return;
                }
                client.setURL(groupEPURL + "/" + groupId);
                Scimv2GroupsApi api = new Scimv2GroupsApi(client);
                ScimApiResponse<String> response = api.deleteGroup();
                logger.info("SCIM - delete group operation returned with response code: " + response.getStatusCode());
                handleSCIMErrorResponse(response);
            }
        } catch (AbstractCharonException e) {
            throw new IdentitySCIMException("Error in provisioning 'delete group' operation for user : " + userName, e);
        } catch (ScimApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IdentitySCIMException("Error in provisioning 'delete group' operation for user : " + userName, e);
        }
    }

    public void updateGroup() throws IdentitySCIMException {

        String filter;
        try {
            //check if role name is updated
            if (additionalInformation != null && (Boolean) additionalInformation.get(
                    SCIM2CommonConstants.IS_ROLE_NAME_CHANGED_ON_UPDATE)) {
                filter = GROUP_FILTER + additionalInformation.get(SCIM2CommonConstants.OLD_GROUP_NAME);
            } else {
                filter = GROUP_FILTER + ((Group) scimObject).getDisplayName();
            }
            List<Group> groups = (List<Group>) (List<?>) listWithGet(null, null, filter, 1, 1, null, null,
                    SCIM2CommonConstants.GROUP);
            if (groups != null && groups.size() > 0) {
                SCIMClient scimClient = new SCIMClient();
                String groupId = groups.get(0).getId();
                if (groupId == null) {
                    return;
                }

                String encodedGroup;
                List<String> users = ((Group) scimObject).getMembersWithDisplayName();
                if (CollectionUtils.isEmpty(users)) {
                    encodedGroup = scimClient.encodeSCIMObject((AbstractSCIMObject) scimObject, SCIMConstants.JSON);
                } else {
                    // Find corresponding userIds of group members and enrich the scimObject.
                    Group updatedGroup = addUserIDForMembersOfGroup();
                    encodedGroup = scimClient.encodeSCIMObject(updatedGroup, SCIMConstants.JSON);
                }
                client.setURL(groupEPURL + "/" + groupId);
                Scimv2GroupsApi api = new Scimv2GroupsApi(client);
                ScimApiResponse<String> response = api.updateGroup(null, null, encodedGroup);
                logger.info("SCIM - update group operation returned with response code: " + response.getStatusCode());
                if (logger.isDebugEnabled()) {
                    logger.debug("Update Group Response: " + response.getData());
                }
                if (scimClient.evaluateResponseStatus(response.getStatusCode())) {
                    //try to decode the scim object to verify that it gets decoded without issue.
                    scimClient.decodeSCIMResponse(response.getData(), SCIMConstants.JSON, SCIM2CommonConstants.GROUP);
                } else {
                    //decode scim exception and extract the specific error message.
                    AbstractCharonException exception =
                            scimClient.decodeSCIMException(response.getData(), SCIMConstants.JSON);
                    logger.error(exception.getMessage());
                }
            }
        } catch (AbstractCharonException e) {
            throw new IdentitySCIMException("Error in provisioning 'update group' operation for user : " + userName, e);
        } catch (ScimApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IdentitySCIMException("Error in provisioning 'update group' operation for user : " + userName, e);
        }
    }

    private Group addUserIDForMembersOfGroup() throws AbstractCharonException, ScimApiException, IOException {

        List<String> users = ((Group) scimObject).getMembersWithDisplayName();

        //create a deep copy of the group since we are going to update the member ids
        Group copiedGroup = (Group) CopyUtil.deepCopy(scimObject);
        //delete existing members in the group since we are going to update it with
        copiedGroup.deleteAttribute(SCIMConstants.GroupSchemaConstants.MEMBERS);

        for (String user : users) {
            try {
                List<SCIMObject> filteredUsers = listWithGet(null, null, USER_FILTER + user, 1, 1, null, null,
                        SCIM2CommonConstants.USER);
                String userId = null;
                for (SCIMObject filteredUser : filteredUsers) {
                    userId = ((User) filteredUser).getId();
                }
                copiedGroup.setMember(userId, user);
            } catch (NotFoundException e) {
                // Skip the not existing users from the SCIM2/groups update request.
                logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                        "added to the group: " + copiedGroup.getDisplayName());
            }
        }
        return copiedGroup;
    }
}

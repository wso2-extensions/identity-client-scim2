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
import org.json.JSONException;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.Group;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.utils.CopyUtil;
import org.wso2.charon3.core.utils.codeutils.PatchOperation;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.util.CollectionUtils;
import org.wso2.scim2.util.PatchOperationEncoder;
import org.wso2.scim2.util.SCIM2CommonConstants;
import org.wso2.scim2.util.SCIMClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                    try {
                        Optional<String> userId = getUserIdFromUserName(user);
                        if (userId.isPresent()) {
                            copiedGroup.setMember(userId.get(), user);
                        } else {
                            logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                                    "added to the group: " + copiedGroup.getDisplayName());
                        }
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
            Optional<Group> group = getGroupByFilter(filter);

            if (group.isPresent()) {
                String groupId = group.get().getId();
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

    public void updateGroup(String httpMethod) throws IdentitySCIMException {

        String filter;
        try {
            //check if role name is updated
            if (additionalInformation != null) {
                Object flag = additionalInformation.get(SCIM2CommonConstants.IS_ROLE_NAME_CHANGED_ON_UPDATE);
                boolean changed = flag != null && Boolean.parseBoolean(flag.toString());

                filter = GROUP_FILTER + (changed
                        ? additionalInformation.get(SCIM2CommonConstants.OLD_GROUP_NAME)
                        : ((Group) scimObject).getDisplayName());
            } else {
                filter = GROUP_FILTER + ((Group) scimObject).getDisplayName();
            }

            Optional<Group> group = getGroupByFilter(filter);
            if (group.isPresent()) {
                SCIMClient scimClient = new SCIMClient();
                String groupId = group.get().getId();
                if (groupId == null) {
                    return;
                }

                String encodedGroup;
                if (httpMethod.equals("PUT")) {
                    List<String> users = ((Group) scimObject).getMembersWithDisplayName();
                    if (CollectionUtils.isEmpty(users)) {
                        encodedGroup = scimClient.encodeSCIMObject((AbstractSCIMObject) scimObject, SCIMConstants.JSON);
                    } else {
                        // Find corresponding userIds of group members and enrich the scimObject.
                        Group updatedGroup = addUserIDForMembersOfGroup();
                        encodedGroup = scimClient.encodeSCIMObject(updatedGroup, SCIMConstants.JSON);
                    }
                } else if (httpMethod.equals("PATCH")) {
                    // Get patch operations from additionalInformation, or fall back to provider.
                    List<PatchOperation> patchOperations = null;
                    if (additionalInformation != null &&
                            additionalInformation.containsKey(SCIM2CommonConstants.PATCH_OPERATIONS)) {
                        patchOperations =
                                (List<PatchOperation>) additionalInformation.get(SCIM2CommonConstants.PATCH_OPERATIONS);
                    }

                    // Fall back to provider's patch operations list if not in additionalInformation.
                    if ((patchOperations == null || patchOperations.isEmpty())
                            && CollectionUtils.isNotEmpty(provider.getPatchOperationList())) {
                        patchOperations = provider.getPatchOperationList();
                    }

                    // Initialize patchOperations if still null.
                    if (patchOperations == null) {
                        patchOperations = new java.util.ArrayList<>();
                    }

                    // Handle member updates via PATCH.
                    if (additionalInformation != null) {
                        List<String> newMembers = (List<String>) additionalInformation.get(
                                SCIM2CommonConstants.NEW_MEMBERS);
                        List<String> deletedMembers = (List<String>) additionalInformation.get(
                                SCIM2CommonConstants.DELETED_MEMBERS);

                        // Create patch operations for adding new members.
                        if (CollectionUtils.isNotEmpty(newMembers)) {
                            List<PatchOperation> addMemberOperations = createAddMembersPatchOperations(newMembers);
                            patchOperations.addAll(addMemberOperations);
                        }

                        // Create patch operations for removing deleted members.
                        if (CollectionUtils.isNotEmpty(deletedMembers)) {
                            List<PatchOperation> removeMemberOperations = createRemoveMembersPatchOperations(
                                    deletedMembers);
                            patchOperations.addAll(removeMemberOperations);
                        }
                    }

                    if (patchOperations.isEmpty()) {
                        logger.warn("No patch operations provided for Group PATCH request");
                        return;
                    }

                    PatchOperationEncoder patchOperationEncoder = new PatchOperationEncoder();
                    encodedGroup = patchOperationEncoder.encodeRequest(patchOperations);
                } else {
                    logger.error("Not supported update operation type: " + httpMethod);
                    return;
                }

                client.setURL(groupEPURL + "/" + groupId);
                Scimv2GroupsApi api = new Scimv2GroupsApi(client);
                ScimApiResponse<String> response =
                        api.updateGroup(null, null, encodedGroup, httpMethod);
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
        } catch (CharonException e) {
            throw new IdentitySCIMException(
                    "Error in encoding the object to be provisioned for group : " +
                            ((Group) scimObject).getDisplayName(), e);
        } catch (BadRequestException e) {
            throw new IdentitySCIMException(
                    "Error in encoding patch operations for group : " + ((Group) scimObject).getDisplayName(), e);
        } catch (JSONException e) {
            throw new IdentitySCIMException(
                    "Error in building JSON for patch operations for group : " + ((Group) scimObject).getDisplayName(),
                    e);
        } catch (AbstractCharonException e) {
            throw new IdentitySCIMException("Error in provisioning 'update group' operation for user : " + userName, e);
        } catch (ScimApiException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        } catch (IOException e) {
            throw new IdentitySCIMException("Error in provisioning 'update group' operation for user : " + userName, e);
        }
    }

    public void updateGroup() throws IdentitySCIMException {

        this.updateGroup("PUT");
    }

    /**
     * Update group with PATCH method.
     * @throws IdentitySCIMException if an error occurs while patching the group.
     */
    public void patchGroup() throws IdentitySCIMException {

        this.updateGroup("PATCH");
    }

    private Group addUserIDForMembersOfGroup() throws AbstractCharonException, ScimApiException, IOException {

        List<String> users = ((Group) scimObject).getMembersWithDisplayName();

        // create a deep copy of the group since we are going to update the member ids.
        Group copiedGroup = (Group) CopyUtil.deepCopy(scimObject);
        // delete existing members in the group since we are going to update it with.
        copiedGroup.deleteAttribute(SCIMConstants.GroupSchemaConstants.MEMBERS);

        for (String user : users) {
            try {
                Optional<String> userId = getUserIdFromUserName(user);
                if (userId.isPresent()) {
                    copiedGroup.setMember(userId.get(), user);
                } else {
                    logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                            "added to the group: " + copiedGroup.getDisplayName());
                }
            } catch (NotFoundException e) {
                // Skip the not existing users from the SCIM2/groups update request.
                logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                        "added to the group: " + copiedGroup.getDisplayName());
            }
        }
        return copiedGroup;
    }

    /**
     * Retrieves a group by filter from the SCIM provider.
     *
     * @param filter The filter to search for the group.
     * @return Optional containing the Group if found, empty otherwise.
     * @throws ScimApiException if an error occurs during API calls.
     * @throws AbstractCharonException if an error occurs during SCIM operations.
     * @throws IOException if an error occurs during I/O operations.
     */
    private Optional<Group> getGroupByFilter(String filter)
            throws ScimApiException, AbstractCharonException, IOException {

        List<Group> groups = (List<Group>) (List<?>) listWithGet(null, null, filter,
                1, 1, null, null, SCIM2CommonConstants.GROUP);

        if (groups != null && !groups.isEmpty()) {
            return Optional.ofNullable(groups.get(0));
        }
        return Optional.empty();
    }

    /**
     * Retrieves the user ID for a given username by querying the SCIM provider.
     *
     * @param userName The username to search for.
     * @return Optional containing the user ID if found, empty otherwise.
     * @throws ScimApiException if an error occurs during API calls.
     * @throws AbstractCharonException if an error occurs during SCIM operations.
     * @throws IOException if an error occurs during I/O operations.
     */
    private Optional<String> getUserIdFromUserName(String userName)
            throws ScimApiException, AbstractCharonException, IOException {

        String filter = USER_FILTER + userName;
        List<SCIMObject> filteredUsers = listWithGet(null, null, filter,
                1, 1, null, null, SCIM2CommonConstants.USER);

        for (SCIMObject filteredUser : filteredUsers) {
            String userId = ((User) filteredUser).getId();
            if (userId != null) {
                return Optional.of(userId);
            }
        }
        return Optional.empty();
    }

    /**
     * Creates PATCH operations for adding members to a group.
     * For each user in the list, retrieves the user ID from the SCIM provider and creates
     * a single "add" patch operation with all members.
     *
     * @param userNames List of user names to add to the group.
     * @return List of PatchOperation objects for adding members.
     * @throws AbstractCharonException if an error occurs during SCIM operations.
     * @throws ScimApiException if an error occurs during API calls.
     * @throws IOException if an error occurs during I/O operations.
     */
    private List<PatchOperation> createAddMembersPatchOperations(List<String> userNames)
            throws AbstractCharonException, ScimApiException, IOException {

        List<PatchOperation> patchOperations = new ArrayList<>();
        List<Map<String, String>> members = new ArrayList<>();

        for (String userName : userNames) {
            try {
                Optional<String> userId = getUserIdFromUserName(userName);

                if (userId.isPresent()) {
                    Map<String, String> member = new HashMap<>();
                    member.put(SCIMConstants.CommonSchemaConstants.VALUE, userId.get());
                    member.put(SCIMConstants.GroupSchemaConstants.DISPLAY, userName);
                    members.add(member);
                } else {
                    logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                            "added to the group: " + ((Group) scimObject).getDisplayName());
                }
            } catch (NotFoundException e) {
                // Skip the not existing users from the SCIM2/groups patch request.
                logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                        "added to the group: " + ((Group) scimObject).getDisplayName());
            }
        }

        // Create a single "add" patch operation with all members.
        if (!members.isEmpty()) {
            PatchOperation addOperation = new PatchOperation();
            addOperation.setOperation(SCIMConstants.OperationalConstants.ADD);
            addOperation.setPath(SCIMConstants.GroupSchemaConstants.MEMBERS);
            addOperation.setValues(members);
            patchOperations.add(addOperation);
        }

        return patchOperations;
    }

    /**
     * Creates PATCH operations for removing members from a group.
     * For each user in the list, retrieves the user ID from the SCIM provider and creates
     * a "remove" patch operation with filter path: members[value eq "userId"].
     *
     * @param userNames List of usernames to remove from the group.
     * @return List of PatchOperation objects for removing members.
     * @throws AbstractCharonException if an error occurs during SCIM operations.
     * @throws ScimApiException if an error occurs during API calls.
     * @throws IOException if an error occurs during I/O operations.
     */
    private List<PatchOperation> createRemoveMembersPatchOperations(List<String> userNames)
            throws AbstractCharonException, ScimApiException, IOException {

        List<PatchOperation> patchOperations = new ArrayList<>();

        for (String userName : userNames) {
            try {
                Optional<String> userId = getUserIdFromUserName(userName);
                if (userId.isPresent()) {
                    PatchOperation removeOperation = new PatchOperation();
                    removeOperation.setOperation(SCIMConstants.OperationalConstants.REMOVE);
                    removeOperation.setPath(SCIMConstants.GroupSchemaConstants.MEMBERS + "[" +
                            SCIMConstants.GroupSchemaConstants.VALUE +
                            SCIMConstants.OperationalConstants.EQ + "\"" + userId.get() + "\"]");
                    patchOperations.add(removeOperation);
                } else {
                    logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                            "removed from the group: " + ((Group) scimObject).getDisplayName());
                }
            } catch (NotFoundException e) {
                // Skip the not existing users from the SCIM2/groups patch request.
                logger.warn("User not found in the provisioned store. Hence, the user will not be " +
                        "removed from the group: " + ((Group) scimObject).getDisplayName());
            }
        }

        return patchOperations;
    }
}

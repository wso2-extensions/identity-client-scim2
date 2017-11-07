package org.wso2.scim2.operation;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.Scimv2UsersApi;

import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.config.CharonConfiguration;
import org.wso2.charon3.core.encoder.JSONDecoder;
import org.wso2.charon3.core.encoder.JSONEncoder;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.schema.SCIMConstants.UserSchemaConstants;
import org.wso2.charon3.core.schema.SCIMResourceSchemaManager;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;
import org.wso2.charon3.core.utils.codeutils.FilterTreeManager;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.model.Error;
import org.wso2.scim2.model.UserList;

import com.google.gson.Gson;

public class UserOperation {

    public static final String USER_ENDPOINT = "user-endpoint";
    private static Logger logger = LoggerFactory.getLogger(UserOperation.class
            .getName());
    private final String USER_FILTER = "userName%20Eq%20";

    SCIMObject scimObject;
    SCIMProvider provider;
    String userEPURL;
    String userName;
    private Map<String, Object> additionalInformation;
    private ApiClient client;

    public UserOperation(SCIMProvider scimProvider, SCIMObject object,
                         Map<String, Object> additionalInformation) {

        provider = scimProvider;
        scimObject = object;
        this.additionalInformation = additionalInformation;

        userEPURL = provider.getProperty(USER_ENDPOINT);
        userName = provider.getProperty(UserSchemaConstants.USER_NAME);

        client = new ApiClient();
        client.setUsername(userName);
        client.setPassword(provider.getProperty(UserSchemaConstants.PASSWORD));
    }

    public User provisionCreateUser() throws IdentitySCIMException {

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

    /*
     * To delete the user by giving user object
     */
    public void provisionDeleteUser() throws IdentitySCIMException {

        if (this.scimObject != null) {
            User user = (User) scimObject;
            try {
                provisionDeleteUserById(user.getId());
            } catch (CharonException e) {
                throw new IdentitySCIMException("Error in encoding the object", e);
            }
        }
    }

    /*
     * To delete the user by giving user Id
     *
     * @param Id
     */
    public void provisionDeleteUserById(String id) throws IdentitySCIMException {

        Scimv2UsersApi api = new Scimv2UsersApi(client);

        try {
            api.deleteUser(id);
        } catch (ApiException e) {
            Gson gson = new Gson();
            Error err = gson.fromJson(e.getResponseBody(), Error.class);
            throw new IdentitySCIMException(err.getDetail(), e);

        }
    }

    public List<User> listWithGet(List<String> attributes,
                                  List<String> excludedAttributes, String filter, int startIndex,
                                  int count, String sortBy, String sortOrder) throws BadRequestException, ApiException, InternalErrorException, CharonException, NotFoundException, IOException {

        FilterTreeManager filterTreeManager;
        Scimv2UsersApi api;
        JSONDecoder jsonDecoder;
        Gson gson;
        List<User> returnedUsers = new ArrayList<>();

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

        SCIMResourceTypeSchema schema = SCIMResourceSchemaManager
                .getInstance().getUserResourceSchema();

        if (filter != null) {
            filterTreeManager = new FilterTreeManager(filter, schema);
            filterTreeManager.buildTree();
        }

        new JSONEncoder();

        api = new Scimv2UsersApi(client);
        ApiResponse<String> response = api.getUser(attributes,
                excludedAttributes, filter, startIndex, count, sortBy,
                sortOrder);
        if (logger.isDebugEnabled()) {
            logger.debug("SCIM - filter operation inside 'delete user' provisioning " +
                    "returned with response code: " + response.getStatusCode());
            logger.debug("Filter User Response: " + response.getData());
        }

        if (response.getStatusCode() == 200 && response.getData() != null) {
            gson = new Gson();
            jsonDecoder = new JSONDecoder();

            UserList userList = gson.fromJson(response.getData(),
                    UserList.class);
            Iterator<Object> iterator = userList.getResources().iterator();
            while (iterator.hasNext()) {
                returnedUsers.add((User) jsonDecoder.decodeResource(
                        gson.toJson(iterator.next()), schema, new User()));
            }

            if (returnedUsers.isEmpty()) {
                String error = "No resulted users found in the user store.";
                throw new NotFoundException(error);
            }

        } else {

        }

        return returnedUsers;

    }

    public User provisionUpdateUser() throws IdentitySCIMException {

        Scimv2UsersApi api;
        User updatedUser = null;
        Gson gson;
        try {
            String filter = USER_FILTER + ((User) scimObject).getUserName();
            List<User> users = listWithGet(null, null, filter, 1, 1, null, null);
            User user = users.get(0);

            String userId = user.getId();
            if (userId == null) {
                logger.error("Trying to update a user entry which doesn't support SCIM. " +
                        "Usually internal carbon User entries such as admin role doesn't support SCIM 2.0 attributes.");
                return updatedUser;
            }

            String encodedUser = new JSONEncoder().encodeSCIMObject(scimObject);

            api = new Scimv2UsersApi(client);
            ApiResponse<String> response = api.updateUser(userId, null, null, encodedUser);

            if (response.getStatusCode() == 201) {
                JSONDecoder jsonDecoder = new JSONDecoder();
                SCIMResourceTypeSchema schema = SCIMResourceSchemaManager
                        .getInstance().getUserResourceSchema();
                updatedUser = (User) jsonDecoder.decodeResource(response.getData(),
                        schema, new User());

            }

        } catch (CharonException e) {
            throw new IdentitySCIMException(
                    "Error in encoding the object to be provisioned for user with id: "
                            + userName, e);
        } catch (BadRequestException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with id: "
                            + userName, e);
        } catch (ApiException e) {
            gson = new Gson();
            Error err = gson.fromJson(e.getResponseBody(), Error.class);
            throw new IdentitySCIMException(err.getDetail(), e);
        } catch (InternalErrorException e) {
            throw new IdentitySCIMException(
                    "Error in invoking provisioning operation for the user with id: "
                            + userName, e);
        } catch (NotFoundException e) {
            throw new IdentitySCIMException("No resulted users found in the user store.", e);
        } catch (IOException e) {
            throw new IdentitySCIMException(e.getMessage(), e);
        }

        return updatedUser;
    }
}

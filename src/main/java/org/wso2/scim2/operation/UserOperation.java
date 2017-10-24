package org.wso2.scim2.operation;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.Scimv2UsersApi;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.encoder.JSONDecoder;
import org.wso2.charon3.core.encoder.JSONEncoder;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants.UserSchemaConstants;
import org.wso2.charon3.core.schema.SCIMResourceSchemaManager;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.model.Errors;

import com.google.gson.Gson;

public class UserOperation {

	private static Logger logger = LoggerFactory.getLogger(UserOperation.class
			.getName());

	public static final String USER_ENDPOINT = "user-endpoint";
	public static final String ATTRIBUTES = "attributes";
	public static final String EXCLUDE_ATTRIBUTES = "excludedAttributes";

	SCIMObject scimObject;
	SCIMProvider provider;
	int provisioningMethod;
	private Map<String, Object> additionalInformation;

	String userEPURL;
	String userName;

	private ApiClient client;

	public UserOperation(SCIMProvider scimProvider, SCIMObject object,
			int httpMethod, Map<String, Object> additionalInformation) {

		provider = scimProvider;
		scimObject = object;
		provisioningMethod = httpMethod;
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
			Errors errors = gson.fromJson(e.getResponseBody(), Errors.class);
			throw new IdentitySCIMException(errors.getErrors().get(0)
					.getDescription(), e);
		
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
	
	public void provisionDeleteUser() throws IdentitySCIMException {
		
		if(this.scimObject != null && scimObject.getClass().isInstance(User.class)) {
			User user = (User)scimObject;
			try {
				provisionDeleteUserById(user.getId());
			} catch (CharonException e) {			
				throw new IdentitySCIMException("Error in encoding the object", e);
			}
		}
	}
	
	public void provisionDeleteUserById(String id) throws IdentitySCIMException {
		
		Scimv2UsersApi api = new Scimv2UsersApi(client);
		
		try {
			api.deleteUser(id);
		} catch (ApiException e) {
			System.out.println(e.getMessage());
			Gson gson = new Gson();
			Errors errors = gson.fromJson(e.getResponseBody(), Errors.class);
			throw new IdentitySCIMException(errors.getErrors().get(0)
					.getDescription(), e);
		
		}
	}
}

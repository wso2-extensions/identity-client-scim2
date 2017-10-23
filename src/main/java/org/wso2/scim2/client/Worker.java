package org.wso2.scim2.client;

import org.wso2.charon3.core.encoder.JSONDecoder;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMResourceSchemaManager;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;
import org.wso2.charon3.core.schema.SCIMConstants.UserSchemaConstants;
import org.wso2.scim2.exception.IdentitySCIMException;

public class Worker {

	public static String DEFINED_USER = "{\"externalID\":\"abergin2\",\"displayName\":\"Andy Bergin\",\"userName\":\"black1300\",\"password\":\"admin\",\"name\":{\"givenName\":\"Andy\",\"familyName\":\"Bergin\"},\"phoneNumbers\":[{\"type\":\"work\",\"value\":\"+1 408 555 8585\"},{\"type\":\"fax\",\"value\":\"+1 408 555 7472\"}],\"emails\":[{\"type\":\"work\",\"value\":\"abergin@example.com\"}]}";

	public static void main(String[] args) {

		SCIMProvider scim = new SCIMProvider();
		scim.setProperty(UserSchemaConstants.USER_NAME, "admin");
		scim.setProperty(UserSchemaConstants.PASSWORD, "admin");

		SCIMResourceTypeSchema schema = SCIMResourceSchemaManager.getInstance()
				.getUserResourceSchema();

		JSONDecoder jsonDecoder = new JSONDecoder();
		User user = null;

		try {
			user = (User) jsonDecoder.decodeResource(DEFINED_USER, schema,
					new User());
		} catch (BadRequestException | CharonException | InternalErrorException e) {
			
		}

		ProvisioningClient client = new ProvisioningClient(scim, user, 1, null);

		try {
			client.provisionCreateUser();
		} catch (IdentitySCIMException e) {
			
		}
	}

}

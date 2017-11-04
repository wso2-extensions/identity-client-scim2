package org.wso2.scim2.client;

import io.swagger.client.ApiException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.wso2.charon3.core.encoder.JSONDecoder;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.exceptions.NotFoundException;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants.UserSchemaConstants;
import org.wso2.charon3.core.schema.SCIMResourceSchemaManager;
import org.wso2.charon3.core.schema.SCIMResourceTypeSchema;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.operation.UserOperation;

import java.io.IOException;
import java.util.List;

public class Worker {

    public static String DEFINED_USER = "{\"userName\":\"black_thunder3\",\"name\":{\"givenName\":\"Andy\",\"familyName\":\"Gonja\"},\"emails\":[{\"type\":\"work\",\"value\":\"abergin@example.com\"}]}";

    public static void main(String[] args) throws InternalErrorException, BadRequestException, CharonException, IdentitySCIMException, ApiException, NotFoundException, IOException {
        Logger.getRootLogger().setLevel(Level.INFO);
        //BasicConfigurator.configure();

        SCIMProvider scim = new SCIMProvider();
        scim.setProperty(UserSchemaConstants.USER_NAME, "admin");
        scim.setProperty(UserSchemaConstants.PASSWORD, "admin");

        SCIMResourceTypeSchema schema = SCIMResourceSchemaManager.getInstance()
                .getUserResourceSchema();

        JSONDecoder jsonDecoder = new JSONDecoder();
        User user = null;

        user = (User) jsonDecoder.decodeResource(DEFINED_USER, schema,
                new User());

        ProvisioningClient client = new ProvisioningClient(scim, user, null);

        //user = client.provisionCreateUser();

        UserOperation o = new UserOperation(scim, user,null);
        //o.provisionCreateUser();
        List<User> users = o.listWithGet(null, null, null,0,5,null, null);

        client.provisionUpdateUser();

        for(int i = 0; i < users.size(); i++) {
            System.out.print(users.get(i).getUserName()+"   ");
            System.out.println(users.get(i).getLastModified());
        }

    }

}

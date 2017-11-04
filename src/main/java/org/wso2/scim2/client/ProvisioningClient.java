package org.wso2.scim2.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.charon3.core.objects.Group;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.scim2.exception.IdentitySCIMException;
import org.wso2.scim2.operation.UserOperation;

/**
 * This class implements logic to initiate SCIM 2.0 provisioning operations to
 * other SCIM 2.0 provider endpoints. Since SCIM provisioning operations are
 * usually run asynchronously, this runs in a separate thread.
 */
public class ProvisioningClient implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(ProvisioningClient.class.getName());
	

	SCIMObject scimObject;
	SCIMProvider provider;
	private String objectType;
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

		if (SCIMObject.class.isAssignableFrom(User.class)) {
			this.objectType = SCIMConstants.USER;
		} else if (SCIMObject.class.isAssignableFrom(Group.class)) {
			this.objectType = SCIMConstants.GROUP;
		}

		provider = scimProvider;
		scimObject = object;
		additionalProvisioningInformation = additionalInformation;
	}
	
	 /**
     * Provision the SCIM User Object passed to the provisioning client in the constructor, to the
     * SCIM Provider whose details are also sent at the initialization.
     */
	public User provisionCreateUser() throws IdentitySCIMException {

		UserOperation operation = new UserOperation(provider, scimObject,
				additionalProvisioningInformation);
		User user = operation.provisionCreateUser();
		
		return user;
	}
	
	public void provisionDeleteUser() throws IdentitySCIMException {

		UserOperation operation = new UserOperation(provider, scimObject,
				additionalProvisioningInformation);
		operation.provisionDeleteUser();
	}

	public void provisionUpdateUser() throws IdentitySCIMException {

	    UserOperation operation = new UserOperation(provider, scimObject,
                additionalProvisioningInformation);
        operation.provisionUpdateUser();
    }

	/**
	 * When an object implementing interface <code>Runnable</code> is used to
	 * create a thread, starting the thread causes the object's <code>run</code>
	 * method to be called in that separately executing thread.
	 * <p/>
	 * The general contract of the method <code>run</code> is that it may take
	 * any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		try {
			if (SCIMConstants.USER.equals(objectType)) {
				UserOperation operation = new UserOperation(provider,
						scimObject, additionalProvisioningInformation);
				operation.provisionCreateUser();
			} else if (SCIMConstants.GROUP.equals(objectType)) {

			}
		} catch (IdentitySCIMException e) {
			logger.error("Error occurred while user provisioning", e);
		}
	}
}

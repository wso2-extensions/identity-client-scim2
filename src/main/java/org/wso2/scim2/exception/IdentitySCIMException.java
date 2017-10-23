package org.wso2.scim2.exception;

public class IdentitySCIMException extends Exception {
   
	private static final long serialVersionUID = 1L;

	public IdentitySCIMException(String error) {
        super(error);
    }

    public IdentitySCIMException(String message, Throwable cause) {
        super(message, cause);
    }
}

package org.wso2.scim2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {
	
	@SerializedName("schemas")
    @Expose
    private String schemas;
    
	@SerializedName("detail")
    @Expose
    private String detail;
    
	@SerializedName("status")
    @Expose
    private String status;

    public String getSchemas() {
        return schemas;
    }

    public void setSchemas(String schemas) {
        this.schemas = schemas;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getStatus() {
    	return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }

}
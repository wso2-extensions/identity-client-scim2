
package org.wso2.scim2.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserList {

    @SerializedName("totalResults")
    @Expose
    private Integer totalResults;
    @SerializedName("startIndex")
    @Expose
    private Integer startIndex;
    @SerializedName("itemsPerPage")
    @Expose
    private Integer itemsPerPage;
    @SerializedName("schemas")
    @Expose
    private List<String> schemas = null;
    @SerializedName("Resources")
    @Expose
    private List<Object> resources = null;

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public List<String> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<String> schemas) {
        this.schemas = schemas;
    }

    public List<Object> getResources() {
        return resources;
    }

    public void setResources(List<Object> resources) {
        this.resources = resources;
    }

}
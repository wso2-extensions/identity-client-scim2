package org.wso2.scim2.client;

import java.util.HashMap;
import java.util.Map;

public class SCIMProvider {
    private String id;
    private Map<String, String> properties = new HashMap<String, String>();

    public SCIMProvider() {
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProperty(String name, String value) {
        if (!this.properties.containsKey(name)) {
            this.properties.put(name, value);
        }

    }

    public String getProperty(String key) {
        return this.properties.containsKey(key) ? (String)this.properties.get(key) : null;
    }
}

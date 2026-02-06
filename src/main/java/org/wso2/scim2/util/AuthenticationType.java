/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.scim2.util;

/**
 * Enum representing different authentication types supported by SCIM2 client.
 */
public enum AuthenticationType {

    BASIC("basic"),
    BEARER("bearer"),
    API_KEY("apiKey"),
    NONE("none");

    private final String value;

    AuthenticationType(String value) {
        this.value = value;
    }

    /**
     * Get the string value of the authentication type.
     *
     * @return String value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get AuthenticationType from string value.
     *
     * @param value String value.
     * @return AuthenticationType enum value.
     * @throws IllegalArgumentException if the value doesn't match any authentication type.
     */
    public static AuthenticationType fromValue(String value) {

        if (value == null) {
            return BASIC; // Default
        }
        for (AuthenticationType type : AuthenticationType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown authentication type: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}

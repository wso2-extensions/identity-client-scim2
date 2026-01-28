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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration class for SCIM2 Client HTTP settings including retry mechanism.
 * Follows a similar pattern to CharonConfiguration for config registration.
 */
public class SCIM2ClientConfig {

    private static final SCIM2ClientConfig INSTANCE = new SCIM2ClientConfig();
    private static final Log LOG = LogFactory.getLog(SCIM2ClientConfig.class);

    private static final int DEFAULT_HTTP_REQUEST_RETRY_COUNT = 1;
    private static final int DEFAULT_HTTP_READ_TIMEOUT_IN_MILLIS = 5000;
    private static final int DEFAULT_HTTP_CONNECTION_TIMEOUT_IN_MILLIS = 2000;
    private static final int DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MILLIS = 2000;

    private Integer registeredRetryCount;
    private Integer registeredReadTimeout;
    private Integer registeredConnectionTimeout;
    private Integer registeredConnectionRequestTimeout;

    private SCIM2ClientConfig() {
    }

    public static SCIM2ClientConfig getInstance() {

        return INSTANCE;
    }

    /**
     * Returns the HTTP request retry count.
     * Priority: Registered config > Default value.
     *
     * @return The HTTP request retry count, or the default if not registered.
     */
    public int getHttpRequestRetryCount() {

        if (registeredRetryCount != null) {
            return registeredRetryCount;
        }
        return DEFAULT_HTTP_REQUEST_RETRY_COUNT;
    }

    /**
     * Returns the HTTP read timeout in milliseconds.
     * Priority: Registered config > Default value.
     *
     * @return The HTTP read timeout, or the default if not registered.
     */
    public int getHttpReadTimeoutInMillis() {

        if (registeredReadTimeout != null) {
            return registeredReadTimeout;
        }
        return DEFAULT_HTTP_READ_TIMEOUT_IN_MILLIS;
    }

    /**
     * Returns the HTTP connection timeout in milliseconds.
     * Priority: Registered config > Default value.
     *
     * @return The HTTP connection timeout, or the default if not registered.
     */
    public int getHttpConnectionTimeoutInMillis() {

        if (registeredConnectionTimeout != null) {
            return registeredConnectionTimeout;
        }
        return DEFAULT_HTTP_CONNECTION_TIMEOUT_IN_MILLIS;
    }

    /**
     * Returns the HTTP connection request timeout in milliseconds.
     * Priority: Registered config > Default value.
     *
     * @return The HTTP connection request timeout, or the default if not registered.
     */
    public int getHttpConnectionRequestTimeoutInMillis() {

        if (registeredConnectionRequestTimeout != null) {
            return registeredConnectionRequestTimeout;
        }
        return DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MILLIS;
    }

    /**
     * Registers HTTP retry count configuration.
     * This method allows external configuration providers (like IdentityUtil) to set retry count.
     * Retry is disabled when count is 0 (only original request is sent).
     * Retry is enabled when count > 0 (original request + N retries).
     *
     * @param retryCount Maximum number of retry attempts. 0 means no retries.
     */
    public void registerRetryConfig(int retryCount) {

        this.registeredRetryCount = retryCount;
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Registered SCIM2 client retry count: %d", retryCount));
        }
    }

    /**
     * Registers HTTP timeout configurations.
     * This method allows external configuration providers (like IdentityUtil) to set timeout configs.
     *
     * @param readTimeout Read timeout in milliseconds.
     * @param connectionTimeout Connection timeout in milliseconds.
     * @param connectionRequestTimeout Connection request timeout in milliseconds.
     */
    public void registerTimeoutConfig(int readTimeout, int connectionTimeout, int connectionRequestTimeout) {

        this.registeredReadTimeout = readTimeout;
        this.registeredConnectionTimeout = connectionTimeout;
        this.registeredConnectionRequestTimeout = connectionRequestTimeout;
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Registered SCIM2 client timeout config: read=%d, connection=%d, request=%d",
                    readTimeout, connectionTimeout, connectionRequestTimeout));
        }
    }

    /**
     * Clears all registered configurations.
     * Useful for testing or resetting to default behavior.
     */
    public void clearRegisteredConfigs() {

        this.registeredRetryCount = null;
        this.registeredReadTimeout = null;
        this.registeredConnectionTimeout = null;
        this.registeredConnectionRequestTimeout = null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Cleared all registered SCIM2 client configs");
        }
    }
}

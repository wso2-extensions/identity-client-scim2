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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for SCIM2ClientConfig class.
 * Tests the config registration pattern used by the framework.
 */
public class SCIM2ClientConfigTest {

    private SCIM2ClientConfig config;

    @Before
    public void setUp() {

        config = SCIM2ClientConfig.getInstance();
        // Clear any registered configs before each test.
        config.clearRegisteredConfigs();
    }

    @After
    public void tearDown() {

        // Clean up registered configs after each test.
        config.clearRegisteredConfigs();
    }

    @Test
    public void testDefaultRetryCount() {

        assertEquals(1, config.getHttpRequestRetryCount());
    }

    @Test
    public void testRegisteredRetryCount() {

        config.registerRetryConfig(5);
        assertEquals(5, config.getHttpRequestRetryCount());
    }

    @Test
    public void testDefaultReadTimeout() {

        assertEquals(5000, config.getHttpReadTimeoutInMillis());
    }

    @Test
    public void testRegisteredReadTimeout() {

        config.registerTimeoutConfig(10000, 2000, 2000);
        assertEquals(10000, config.getHttpReadTimeoutInMillis());
    }

    @Test
    public void testDefaultConnectionTimeout() {

        assertEquals(2000, config.getHttpConnectionTimeoutInMillis());
    }

    @Test
    public void testRegisteredConnectionTimeout() {

        config.registerTimeoutConfig(5000, 3000, 2000);
        assertEquals(3000, config.getHttpConnectionTimeoutInMillis());
    }

    @Test
    public void testDefaultConnectionRequestTimeout() {

        assertEquals(2000, config.getHttpConnectionRequestTimeoutInMillis());
    }

    @Test
    public void testRegisteredConnectionRequestTimeout() {

        config.registerTimeoutConfig(5000, 2000, 4000);
        assertEquals(4000, config.getHttpConnectionRequestTimeoutInMillis());
    }

    @Test
    public void testDefaultConnectionPoolSize() {

        assertEquals(20, config.getHttpConnectionPoolSize());
    }

    @Test
    public void testRegisteredConnectionPoolSize() {

        config.registerConnectionPoolConfig(50);
        assertEquals(50, config.getHttpConnectionPoolSize());
    }

    @Test
    public void testRetryCountZeroMeansNoRetries() {

        config.registerRetryConfig(0);
        assertEquals(0, config.getHttpRequestRetryCount());
    }

    @Test
    public void testClearRegisteredConfigs() {

        // Register some configs.
        config.registerRetryConfig(10);
        config.registerTimeoutConfig(15000, 8000, 6000);
        config.registerConnectionPoolConfig(100);

        // Verify registered values are used.
        assertEquals(10, config.getHttpRequestRetryCount());
        assertEquals(15000, config.getHttpReadTimeoutInMillis());
        assertEquals(8000, config.getHttpConnectionTimeoutInMillis());
        assertEquals(6000, config.getHttpConnectionRequestTimeoutInMillis());
        assertEquals(100, config.getHttpConnectionPoolSize());

        // Clear configs.
        config.clearRegisteredConfigs();

        // Verify defaults are used.
        assertEquals(1, config.getHttpRequestRetryCount());
        assertEquals(5000, config.getHttpReadTimeoutInMillis());
        assertEquals(2000, config.getHttpConnectionTimeoutInMillis());
        assertEquals(2000, config.getHttpConnectionRequestTimeoutInMillis());
        assertEquals(20, config.getHttpConnectionPoolSize());
    }

    @Test
    public void testSingletonInstance() {

        SCIM2ClientConfig instance1 = SCIM2ClientConfig.getInstance();
        SCIM2ClientConfig instance2 = SCIM2ClientConfig.getInstance();
        assertSame(instance1, instance2);
    }
}

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

package org.wso2.scim2.auth;

/**
 * Interface for managing OAuth access tokens.
 */
public interface TokenManager {

    /**
     * Get the current access token. May return a cached token if still valid.
     *
     * @return The access token.
     * @throws Exception If token acquisition fails.
     */
    String getAccessToken() throws Exception;

    /**
     * Force refresh the access token by invalidating any cached token and acquiring a new one.
     *
     * @param expiredToken The token that was found to be expired, used to avoid redundant refreshes.
     *                     May be null to force unconditional refresh.
     * @return The new (or already-refreshed) access token.
     * @throws Exception If token refresh fails.
     */
    String refreshToken(String expiredToken) throws Exception;
}

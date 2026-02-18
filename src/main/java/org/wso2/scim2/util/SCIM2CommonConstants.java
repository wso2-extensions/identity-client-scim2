/*
 * Copyright (c) 2018-2026, WSO2 LLC. (http://www.wso2.com).
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

public class SCIM2CommonConstants {

    public static final String IS_ROLE_NAME_CHANGED_ON_UPDATE = "ISRoleNameChangedOnUpdate";
    public static final String OLD_GROUP_NAME = "OldGroupName";
    public static final String ELEMENT_NAME_USER_ENDPOINT = "userEndpoint";
    public static final String ELEMENT_NAME_GROUP_ENDPOINT = "groupEndpoint";
    public static final String PATCH_OPERATIONS = "PatchOperations";
    public static final String NEW_MEMBERS = "NewMembers";
    public static final String DELETED_MEMBERS = "DeletedMembers";
    public static final int USER = 1;
    public static final int GROUP = 2;

    // Authentication constants.
    public static final String AUTHENTICATION_TYPE = "authenticationType";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String API_KEY_HEADER = "apiKeyHeader";
    public static final String API_KEY_VALUE = "apiKeyValue";

    // Authentication scheme names.
    public static final String AUTH_SCHEME_BASIC = "basicAuth";
    public static final String AUTH_SCHEME_BEARER = "bearerAuth";
    public static final String AUTH_SCHEME_API_KEY = "apiKeyAuth";

    // OAuth Client Credentials authentication constants.
    public static final String OAUTH_TOKEN_ENDPOINT = "oauthTokenEndpoint";
    public static final String OAUTH_CLIENT_ID = "oauthClientId";
    public static final String OAUTH_CLIENT_SECRET = "oauthClientSecret";
    public static final String OAUTH_SCOPE = "oauthScope";
    public static final String TOKEN_MANAGER = "tokenManager";
}

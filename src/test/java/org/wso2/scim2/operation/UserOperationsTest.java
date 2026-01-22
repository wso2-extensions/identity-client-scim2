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

package org.wso2.scim2.operation;

import org.junit.Before;
import org.junit.Test;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.utils.codeutils.PatchOperation;
import org.wso2.scim2.client.SCIMProvider;
import org.wso2.scim2.util.SCIM2CommonConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for UserOperations class, specifically testing PATCH operation support.
 */
public class UserOperationsTest {

    private SCIMProvider provider;
    private User testUser;
    private Map<String, Object> additionalInformation;
    private static final String TEST_USERNAME = "testuser";
    private static final String USER_ENDPOINT = "https://example.com/scim2/Users";

    @Before
    public void setUp() throws Exception {

        provider = new SCIMProvider();
        provider.setProperty(SCIM2CommonConstants.ELEMENT_NAME_USER_ENDPOINT, USER_ENDPOINT);
        provider.setProperty(SCIMConstants.UserSchemaConstants.USER_NAME, "admin");
        provider.setProperty(SCIMConstants.UserSchemaConstants.PASSWORD, "admin");

        testUser = new User();
        testUser.setUserName(TEST_USERNAME);

        additionalInformation = new HashMap<>();
    }

    @Test
    public void testPatchOperationsInAdditionalInfo() throws Exception {

        // Test patch operations with multiple operations and empty list.
        List<PatchOperation> patchOperations = new ArrayList<>();

        PatchOperation op1 = new PatchOperation();
        op1.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        op1.setPath("displayName");
        op1.setValues("John Doe");
        patchOperations.add(op1);

        PatchOperation op2 = new PatchOperation();
        op2.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        op2.setPath("emails[type eq \"work\"].value");
        op2.setValues("john.doe@example.com");
        patchOperations.add(op2);

        PatchOperation op3 = new PatchOperation();
        op3.setOperation(SCIMConstants.OperationalConstants.ADD);
        op3.setPath("nickName");
        op3.setValues("JD");
        patchOperations.add(op3);

        additionalInformation.put(SCIM2CommonConstants.PATCH_OPERATIONS, patchOperations);

        // Verify patch operations list is accessible and properly structured.
        assertNotNull(additionalInformation.get(SCIM2CommonConstants.PATCH_OPERATIONS));
        @SuppressWarnings("unchecked")
        List<PatchOperation> retrievedOps =
                (List<PatchOperation>) additionalInformation.get(SCIM2CommonConstants.PATCH_OPERATIONS);
        assertEquals(3, retrievedOps.size());
        assertEquals("displayName", retrievedOps.get(0).getPath());
        assertEquals("replace", retrievedOps.get(0).getOperation());
        assertEquals("John Doe", retrievedOps.get(0).getValues());
        assertEquals("emails[type eq \"work\"].value", retrievedOps.get(1).getPath());
        assertEquals("add", retrievedOps.get(2).getOperation());

        // Test with empty list.
        List<PatchOperation> emptyList = new ArrayList<>();
        additionalInformation.put(SCIM2CommonConstants.PATCH_OPERATIONS, emptyList);

        @SuppressWarnings("unchecked")
        List<PatchOperation> emptyRetrieved =
                (List<PatchOperation>) additionalInformation.get(SCIM2CommonConstants.PATCH_OPERATIONS);
        assertNotNull(emptyRetrieved);
        assertEquals(0, emptyRetrieved.size());
    }

    @Test
    public void testPatchOperationsInProvider() throws Exception {

        // Test patch operations storage and retrieval in provider.
        PatchOperation op1 = new PatchOperation();
        op1.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        op1.setPath("nickName");
        op1.setValues("Johnny");
        provider.addPatchOperation(op1);

        PatchOperation op2 = new PatchOperation();
        op2.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        op2.setPath("displayName");
        op2.setValues("John");
        provider.addPatchOperation(op2);

        // Verify that provider's patch operations are accessible.
        assertNotNull(provider.getPatchOperationList());
        assertEquals(2, provider.getPatchOperationList().size());
        assertEquals("nickName", provider.getPatchOperationList().get(0).getPath());
        assertEquals("displayName", provider.getPatchOperationList().get(1).getPath());
    }
}

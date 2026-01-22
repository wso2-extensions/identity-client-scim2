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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.utils.codeutils.PatchOperation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for PatchOperationEncoder class.
 */
public class PatchOperationEncoderTest {

    private PatchOperationEncoder encoder;

    @Before
    public void setUp() {
        encoder = new PatchOperationEncoder();
    }

    @Test
    public void testEncodeBasicOperations() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();

        // Replace operation.
        PatchOperation replaceOp = new PatchOperation();
        replaceOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        replaceOp.setPath("displayName");
        replaceOp.setValues("John Doe");
        operations.add(replaceOp);

        // Add operation.
        PatchOperation addOp = new PatchOperation();
        addOp.setOperation(SCIMConstants.OperationalConstants.ADD);
        addOp.setPath("emails");
        addOp.setValues("{\"type\":\"work\",\"value\":\"john@example.com\"}");
        operations.add(addOp);

        // Remove operation.
        PatchOperation removeOp = new PatchOperation();
        removeOp.setOperation(SCIMConstants.OperationalConstants.REMOVE);
        removeOp.setPath("phoneNumbers[type eq \"fax\"]");
        operations.add(removeOp);

        String encodedJson = encoder.encodeRequest(operations);

        JSONObject jsonObject = new JSONObject(encodedJson);
        assertTrue(jsonObject.has("schemas"));
        assertTrue(jsonObject.has("Operations"));

        JSONArray schemas = jsonObject.getJSONArray("schemas");
        assertEquals(1, schemas.length());
        assertEquals(PatchOperationEncoder.PATCH_OP_SCHEMA, schemas.getString(0));

        JSONArray operationsArray = jsonObject.getJSONArray("Operations");
        assertEquals(3, operationsArray.length());

        // Verify replace operation.
        JSONObject replaceOperation = operationsArray.getJSONObject(0);
        assertEquals("replace", replaceOperation.getString("op"));
        assertEquals("displayName", replaceOperation.getString("path"));
        assertEquals("John Doe", replaceOperation.get("value"));

        // Verify add operation.
        JSONObject addOperation = operationsArray.getJSONObject(1);
        assertEquals("add", addOperation.getString("op"));
        assertEquals("emails", addOperation.getString("path"));
        assertNotNull(addOperation.get("value"));

        // Verify remove operation.
        JSONObject removeOperation = operationsArray.getJSONObject(2);
        assertEquals("remove", removeOperation.getString("op"));
        assertEquals("phoneNumbers[type eq \"fax\"]", removeOperation.getString("path"));
    }

    @Test
    public void testEncodeMultipleOperations() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();

        PatchOperation replaceOp = new PatchOperation();
        replaceOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        replaceOp.setPath("displayName");
        replaceOp.setValues("Jane Doe");
        operations.add(replaceOp);

        PatchOperation addOp = new PatchOperation();
        addOp.setOperation(SCIMConstants.OperationalConstants.ADD);
        addOp.setPath("nickName");
        addOp.setValues("JD");
        operations.add(addOp);

        PatchOperation removeOp = new PatchOperation();
        removeOp.setOperation(SCIMConstants.OperationalConstants.REMOVE);
        removeOp.setPath("title");
        operations.add(removeOp);

        String encodedJson = encoder.encodeRequest(operations);

        JSONObject jsonObject = new JSONObject(encodedJson);
        JSONArray operationsArray = jsonObject.getJSONArray("Operations");
        assertEquals(3, operationsArray.length());

        // Verify each operation
        JSONObject op1 = operationsArray.getJSONObject(0);
        assertEquals("replace", op1.getString("op"));
        assertEquals("displayName", op1.getString("path"));

        JSONObject op2 = operationsArray.getJSONObject(1);
        assertEquals("add", op2.getString("op"));
        assertEquals("nickName", op2.getString("path"));

        JSONObject op3 = operationsArray.getJSONObject(2);
        assertEquals("remove", op3.getString("op"));
        assertEquals("title", op3.getString("path"));
    }

    @Test(expected = BadRequestException.class)
    public void testEncodeInvalidOperation() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();
        PatchOperation invalidOp = new PatchOperation();
        invalidOp.setOperation("invalidOp");
        invalidOp.setPath("displayName");
        invalidOp.setValues("Test");
        operations.add(invalidOp);

        // This should throw BadRequestException
        encoder.encodeRequest(operations);
    }

    @Test
    public void testEncodeEdgeCases() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();

        // Complex path with filter.
        PatchOperation complexPathOp = new PatchOperation();
        complexPathOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        complexPathOp.setPath("emails[type eq \"work\"].value");
        complexPathOp.setValues("newemail@example.com");
        operations.add(complexPathOp);

        // No path specified.
        PatchOperation noPathOp = new PatchOperation();
        noPathOp.setOperation(SCIMConstants.OperationalConstants.ADD);
        noPathOp.setValues("{\"displayName\":\"Test User\"}");
        operations.add(noPathOp);

        // Boolean value.
        PatchOperation booleanOp = new PatchOperation();
        booleanOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        booleanOp.setPath("active");
        booleanOp.setValues(true);
        operations.add(booleanOp);

        // Null value.
        PatchOperation nullValueOp = new PatchOperation();
        nullValueOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        nullValueOp.setPath("middleName");
        nullValueOp.setValues(null);
        operations.add(nullValueOp);

        String encodedJson = encoder.encodeRequest(operations);

        JSONObject jsonObject = new JSONObject(encodedJson);
        JSONArray operationsArray = jsonObject.getJSONArray("Operations");
        assertEquals(4, operationsArray.length());

        // Verify complex path.
        JSONObject complexOp = operationsArray.getJSONObject(0);
        assertEquals("emails[type eq \"work\"].value", complexOp.getString("path"));
        assertEquals("newemail@example.com", complexOp.get("value"));

        // Verify no path operation.
        JSONObject noPath = operationsArray.getJSONObject(1);
        assertEquals("add", noPath.getString("op"));
        assertFalse("Path should not be present if not set", noPath.has("path"));
        assertTrue(noPath.has("value"));

        // Verify boolean value.
        JSONObject boolOp = operationsArray.getJSONObject(2);
        assertEquals("active", boolOp.getString("path"));
        assertTrue(boolOp.getBoolean("value"));

        // Verify null value.
        JSONObject nullOp = operationsArray.getJSONObject(3);
        assertEquals("middleName", nullOp.getString("path"));
    }

    @Test
    public void testEncodeEmptyOperationsList() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();

        String encodedJson = encoder.encodeRequest(operations);

        JSONObject jsonObject = new JSONObject(encodedJson);
        assertTrue(jsonObject.has("schemas"));
        assertTrue(jsonObject.has("Operations"));

        JSONArray operationsArray = jsonObject.getJSONArray("Operations");
        assertEquals(0, operationsArray.length());
    }

    @Test
    public void testEncodeEnterpriseExtensions() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();

        // Simple enterprise attributes.
        PatchOperation deptOp = new PatchOperation();
        deptOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        deptOp.setPath("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department");
        deptOp.setValues("Sales");
        operations.add(deptOp);

        PatchOperation countryOp = new PatchOperation();
        countryOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        countryOp.setPath("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:country");
        countryOp.setValues("UK");
        operations.add(countryOp);

        PatchOperation empNumOp = new PatchOperation();
        empNumOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        empNumOp.setPath("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:employeeNumber");
        empNumOp.setValues("EMP12345");
        operations.add(empNumOp);

        // Complex enterprise attribute (manager).
        PatchOperation managerOp = new PatchOperation();
        managerOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        managerOp.setPath("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:manager");
        JSONObject managerValue = new JSONObject();
        managerValue.put("displayName", "Manager Name");
        managerValue.put("value", "manager-id-123");
        managerOp.setValues(managerValue);
        operations.add(managerOp);

        // Custom extension schema.
        PatchOperation customOp = new PatchOperation();
        customOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        customOp.setPath("urn:scim:schemas:extension:custom:User:customAttribute");
        customOp.setValues("customValue");
        operations.add(customOp);

        String encodedJson = encoder.encodeRequest(operations);

        JSONObject jsonObject = new JSONObject(encodedJson);
        JSONArray operationsArray = jsonObject.getJSONArray("Operations");
        assertEquals(5, operationsArray.length());

        // Verify simple enterprise attributes.
        JSONObject dept = operationsArray.getJSONObject(0);
        assertEquals("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department", dept.getString("path"));
        assertEquals("Sales", dept.get("value"));

        JSONObject country = operationsArray.getJSONObject(1);
        assertEquals("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:country", country.getString("path"));
        assertEquals("UK", country.get("value"));

        JSONObject empNum = operationsArray.getJSONObject(2);
        assertEquals("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:employeeNumber", empNum.getString("path"));
        assertEquals("EMP12345", empNum.get("value"));

        // Verify complex manager attribute.
        JSONObject manager = operationsArray.getJSONObject(3);
        assertEquals("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:manager", manager.getString("path"));
        JSONObject managerVal = manager.getJSONObject("value");
        assertEquals("Manager Name", managerVal.getString("displayName"));
        assertEquals("manager-id-123", managerVal.getString("value"));

        // Verify custom extension.
        JSONObject custom = operationsArray.getJSONObject(4);
        assertEquals("urn:scim:schemas:extension:custom:User:customAttribute", custom.getString("path"));
        assertEquals("customValue", custom.get("value"));

        // Verify path format (schema:attributeName).
        String deptPath = dept.getString("path");
        assertTrue(deptPath.startsWith("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:"));
        assertTrue(deptPath.endsWith(":department"));
        int lastColonIndex = deptPath.lastIndexOf(':');
        assertEquals("department", deptPath.substring(lastColonIndex + 1));
    }

    @Test
    public void testEncodeMixedCoreAndExtensionAttributes() throws BadRequestException, JSONException {

        List<PatchOperation> operations = new ArrayList<>();

        // Core attribute
        PatchOperation coreOp = new PatchOperation();
        coreOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        coreOp.setPath("displayName");
        coreOp.setValues("John Doe");
        operations.add(coreOp);

        // Extension attribute
        PatchOperation extOp = new PatchOperation();
        extOp.setOperation(SCIMConstants.OperationalConstants.REPLACE);
        extOp.setPath("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department");
        extOp.setValues("Engineering");
        operations.add(extOp);

        String encodedJson = encoder.encodeRequest(operations);

        JSONObject jsonObject = new JSONObject(encodedJson);
        JSONArray operationsArray = jsonObject.getJSONArray("Operations");
        assertEquals(2, operationsArray.length());

        // Verify core attribute (simple path)
        JSONObject coreOperation = operationsArray.getJSONObject(0);
        assertEquals("displayName", coreOperation.getString("path"));
        assertEquals("John Doe", coreOperation.get("value"));

        // Verify extension attribute (full schema:attribute path)
        JSONObject extOperation = operationsArray.getJSONObject(1);
        assertEquals("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department",
                extOperation.getString("path"));
        assertEquals("Engineering", extOperation.get("value"));
    }

}

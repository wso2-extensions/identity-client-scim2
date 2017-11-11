/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.scim2.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.utils.codeutils.PatchOperation;

import java.util.List;

public class PatchOperationEncoder {

    public static final String PATCH_OP_SCHEMA = "urn:ietf:params:scim:api:messages:2.0:PatchOp";

    public String encodeRequest(List<PatchOperation> patchOperations) throws BadRequestException, JSONException {

        JSONObject encodedObject = new JSONObject();

        JSONArray schemaArray = new JSONArray();
        schemaArray.put(PATCH_OP_SCHEMA);

        JSONArray operationsArray = new JSONArray();
        for (PatchOperation patchOperation : patchOperations) {
            JSONObject obj = new JSONObject();

            String operation = patchOperation.getOperation();
            if (operation != null && (operation.equals(SCIMConstants.OperationalConstants.ADD))
                    || operation.equals(SCIMConstants.OperationalConstants.REMOVE)
                    || operation.equals(SCIMConstants.OperationalConstants.REPLACE)) {
                obj.put(SCIMConstants.OperationalConstants.OP, patchOperation.getOperation());
            } else {
                throw new BadRequestException(ResponseCodeConstants.INVALID_SYNTAX);
            }
            if (patchOperation.getPath() != null) {
                obj.put(SCIMConstants.OperationalConstants.PATH, patchOperation.getPath());
            }
            if (patchOperation.getValues() != null) {
                obj.put(SCIMConstants.OperationalConstants.VALUE, patchOperation.getValues());
            }

            operationsArray.put(obj);
        }

        encodedObject.put(SCIMConstants.CommonSchemaConstants.SCHEMAS, schemaArray);
        encodedObject.put(SCIMConstants.OperationalConstants.OPERATIONS, operationsArray);

        return encodedObject.toString();
    }
}
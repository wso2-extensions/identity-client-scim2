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
import org.json.JSONTokener;
import org.wso2.charon3.core.encoder.JSONDecoder;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.schema.ResourceTypeSchema;
import org.wso2.charon3.core.schema.SCIMConstants;

import java.util.ArrayList;
import java.util.List;

public class SCIMDecoder {

    /**
     * Decode the listed resource sent in the payload of response for filter/retrieve requests.
     *
     * @param scimString
     * @return
     * @throws CharonException
     */
    public List<SCIMObject> decodeListedResource(String scimString,
                                               ResourceTypeSchema resourceSchemaOfListedResource,
                                               AbstractSCIMObject scimObjectOfListedResource)
            throws CharonException, BadRequestException {

        List<SCIMObject> scimObjects = null;
        JSONDecoder decoder;

        try {
            //decode the string into json representation
            JSONObject decodedJsonObj = new JSONObject(new JSONTokener(scimString));

            //we expect this to be a non-empty JSONArray according to the format
            Object resources = decodedJsonObj.opt(SCIMConstants.ListedResourceSchemaConstants.RESOURCES);
            scimObjects = new ArrayList<>();
            decoder = new JSONDecoder();
            for (int i = 0; i < (((JSONArray) resources).length()); i++) {
                Object object = ((JSONArray) resources).get(i);
                String scimResourceString = null;
                if(object instanceof String) {
                    scimResourceString = ((JSONArray) resources).getString(i);
                } else if (object instanceof JSONObject) {
                    scimResourceString = ((JSONArray) resources).getJSONObject(i).toString();
                }
                SCIMObject scimObject = decoder.decodeResource(scimResourceString, resourceSchemaOfListedResource,
                        scimObjectOfListedResource);
                scimObjects.add(scimObject);
            }

        } catch (JSONException e) {
            String error = "JSON string could not be decoded properly.";
            throw new BadRequestException(error);
        } catch (InternalErrorException e) {
            String error = "Error decoding SCIMObject";
        }

        return scimObjects;
    }
}

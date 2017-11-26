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
import org.wso2.charon3.core.encoder.JSONEncoder;
import org.wso2.charon3.core.exceptions.AbstractCharonException;
import org.wso2.charon3.core.exceptions.BadRequestException;
import org.wso2.charon3.core.exceptions.CharonException;
import org.wso2.charon3.core.exceptions.InternalErrorException;
import org.wso2.charon3.core.objects.AbstractSCIMObject;
import org.wso2.charon3.core.objects.Group;
import org.wso2.charon3.core.objects.SCIMObject;
import org.wso2.charon3.core.objects.User;
import org.wso2.charon3.core.objects.bulk.BulkData;
import org.wso2.charon3.core.objects.bulk.BulkResponseData;
import org.wso2.charon3.core.protocol.ResponseCodeConstants;
import org.wso2.charon3.core.schema.ResourceTypeSchema;
import org.wso2.charon3.core.schema.SCIMConstants;
import org.wso2.charon3.core.schema.SCIMSchemaDefinitions;

import java.util.List;

public class SCIMClient {

    /**
     * JSON encoder, decoder
     */
    private JSONEncoder jsonEncoder;
    private JSONDecoder jsonDecoder;

    public SCIMClient() {
        jsonEncoder = new JSONEncoder();
        jsonDecoder = new JSONDecoder();
    }

    /**
     * Return a SCIMUser object as defined in SCIM schema
     *
     * @return
     */
    public User createUser() {
        return new User();
    }

    public Group createGroup() {
        return new Group();
    }

    public BulkData createBulkRequestData() {
        return new BulkData();
    }

    /**
     * This is to decode SCIM Response received for a SCIM List/Filter requests.
     *
     * @param scimResponse
     * @param format
     * @param containedResourceType
     * @return
     */
    public List<SCIMObject> decodeSCIMResponseWithListedResource(String scimResponse, String format,
                                                                 int containedResourceType)
            throws CharonException, BadRequestException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            SCIMDecoder scimDecoder = new SCIMDecoder();
            switch (containedResourceType) {
                case 1:
                    return scimDecoder.decodeListedResource(scimResponse,
                            SCIMSchemaDefinitions.SCIM_USER_SCHEMA,
                            new User());
                case 2:
                    return scimDecoder.decodeListedResource(scimResponse,
                            SCIMSchemaDefinitions.SCIM_GROUP_SCHEMA,
                            new Group());
                default:
                    throw new CharonException("Resource type didn't match any existing types.");
            }
        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    /**
     * Decode the SCIMResponse, given the format and the resource type.
     * Here we assume the resource type is of an existing SCIMObject type.
     * Int type is given as parameter rather than AbstractSCIMObject - so that API user can
     * select out of existing type without worrying about which extended type to pass.
     *
     * @param scimResponse
     * @param format
     * @return
     */
    public SCIMObject decodeSCIMResponse(String scimResponse, String format, int resourceType)
            throws AbstractCharonException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            return decodeSCIMResponse(scimResponse, jsonDecoder, resourceType);

        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    private SCIMObject decodeSCIMResponse(String scimResponse, JSONDecoder decoder,
                                          int resourceType)
            throws AbstractCharonException {

        switch (resourceType) {
            case 1:
                User userObject = (User) decoder.decodeResource(scimResponse,
                        SCIMSchemaDefinitions.SCIM_USER_SCHEMA,
                        new User());
                ClientSideValidator.validateRetrievedSCIMObject(userObject,
                        SCIMSchemaDefinitions.SCIM_USER_SCHEMA);
                return userObject;
            case 2:
                Group groupObject = (Group) decoder.decodeResource(scimResponse,
                        SCIMSchemaDefinitions.SCIM_GROUP_SCHEMA,
                        new Group());
                ClientSideValidator.validateRetrievedSCIMObject(groupObject,
                        SCIMSchemaDefinitions.SCIM_GROUP_SCHEMA);
                return groupObject;
            default:
                throw new CharonException("Resource type didn't match any existing types.");
        }
    }

    /**
     * Decode the SCIMResponse which contains a custom SCIMObject which is based on an extended
     * Schema.
     *
     * @param scimResponse
     * @param format
     * @param resourceSchema
     * @return
     */
    public SCIMObject decodeSCIMResponse(String scimResponse, String format,
                                         ResourceTypeSchema resourceSchema,
                                         AbstractSCIMObject scimObject)
            throws CharonException, BadRequestException, InternalErrorException {

        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            return jsonDecoder.decodeResource(scimResponse, resourceSchema, scimObject);

        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }

    }

    /**
     * Once the response is identified as containing exception, decode the relevant e
     *
     * @param scimResponse
     * @param format
     * @return
     * @throws CharonException
     */
    public AbstractCharonException decodeSCIMException(String scimResponse, String format)
            throws CharonException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            return this.decodeException(scimResponse);

        } /*else if ((format.equals(SCIMConstants.XML)) && (xmlEncoder != null)) {
            return xmlDecoder.decodeException(scimResponse);

        }*/ else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    /**
     * Decode the string sent in the SCIM response payload, which is an exception.
     * JSON encoded exception is usually like:
     * {
     * "Errors":[
     * {
     * "description":"Resource 2819c223-7f76-453a-919d-413861904646 not found",
     * "code":"404"
     * }
     * ]
     * }
     *
     * @param scimExceptionString
     * @return
     */
    public AbstractCharonException decodeException(String scimExceptionString)
            throws CharonException {

        try {
            JSONObject decodedJsonObj = new JSONObject(new JSONTokener(scimExceptionString));
            Object jsonError = decodedJsonObj.opt(ResponseCodeConstants.ERRORS);
            //according to the SCIM spec, Error details returned as multivalued attribute.
            //so we assume the same
            if (jsonError instanceof JSONArray) {
                //according to the spec, ERRORS are composed as complex attributes with
                //"description" & "code" as the sub attributes. Since we return only one exception,
                //only the first error is read.
                JSONObject errorObject = (JSONObject) ((JSONArray) jsonError).get(0);
                //decode the details of the error
                String errorCode = (String) errorObject.opt("code");
                String errorDescription = (String) errorObject.opt("description");

                return new AbstractCharonException(errorDescription);
            }
        } catch (JSONException e) {
            //log error
            String error = "Error in building exception from the JSON representation";
            throw new CharonException(error);
        }
        return null;
    }

    /**
     * Encode the SCIM object, in the given format.
     *
     * @param scimObject
     * @param format
     * @return
     * @throws CharonException
     */
    public String encodeSCIMObject(AbstractSCIMObject scimObject, String format)
            throws CharonException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonEncoder != null)) {
            return jsonEncoder.encodeSCIMObject(scimObject);
        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    /**
     * Encode given BulkData object and return bulk data string
     *
     * @param bulkData
     * @param format
     * @return
     */
    public String encodeSCIMObject(BulkResponseData bulkData, String format) throws AbstractCharonException {

        if ((format.equals(SCIMConstants.JSON)) && (jsonEncoder != null)) {
            return jsonEncoder.encodeBulkResponseData(bulkData);

        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    /**
     * Identify whether the response includes a success response or failure response according to the
     * response status code.
     *
     * @param statusCode
     * @return
     */
    public boolean evaluateResponseStatus(int statusCode) {
        switch (statusCode) {
            //ok
            case ResponseCodeConstants.CODE_OK:
                return true;
            case ResponseCodeConstants.CODE_CREATED:
                return true;

            case ResponseCodeConstants.CODE_NO_CONTENT:
                return true;

            case ResponseCodeConstants.CODE_UNAUTHORIZED:
                return false;

            case ResponseCodeConstants.CODE_FORMAT_NOT_SUPPORTED:
                return false;

            case ResponseCodeConstants.CODE_INTERNAL_ERROR:
                return false;

            case ResponseCodeConstants.CODE_RESOURCE_NOT_FOUND:
                return false;

            case ResponseCodeConstants.CODE_BAD_REQUEST:
                return false;

            default:
                return false;
        }
    }
}

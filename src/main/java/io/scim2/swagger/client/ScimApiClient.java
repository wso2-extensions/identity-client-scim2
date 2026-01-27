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

package io.scim2.swagger.client;

import io.scim2.swagger.client.auth.Authentication;
import io.scim2.swagger.client.auth.ApiKeyAuth;
import io.scim2.swagger.client.auth.HttpBasicAuth;
import io.scim2.swagger.client.auth.OAuth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpOptions;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import okio.BufferedSink;
import okio.Okio;
import org.apache.commons.lang.StringUtils;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.wso2.scim2.util.SCIM2ClientConfig;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;

import java.net.ConnectException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScimApiClient implements AutoCloseable {

    private static final Log logger = LogFactory.getLog(ScimApiClient.class);
    private static final Set<Integer> RETRYABLE_STATUS_CODES =
            new HashSet<>(Arrays.asList(500, 502, 503, 504, 408, 429));

    // Exponential backoff configuration for 429 (Too Many Requests).
    private static final int BACKOFF_INITIAL_DELAY_MS = 1000;  // 1 second initial delay.
    private static final int BACKOFF_MAX_DELAY_MS = 32000;     // 32 seconds max delay.
    private static final double BACKOFF_MULTIPLIER = 2.0;      // Double delay each retry.

    public static final double JAVA_VERSION;

    static {
        JAVA_VERSION = Double.parseDouble(System.getProperty("java.specification.version"));
    }

    /**
     * The datetime format to be used when <code>lenientDatetimeFormat</code> is enabled.
     */
    public static final String LENIENT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static final String URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String MULTIPART_FORM_DATA_CONTENT_TYPE = "multipart/form-data";
    private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private boolean lenientOnJson = false;
    private boolean debugging = false;
    private final Map<String, String> defaultHeaderMap = new HashMap<>();
    private String tempFolderPath = null;

    private Map<String, Authentication> authentications;

    private DateFormat dateFormat;
    private DateFormat datetimeFormat;
    private boolean lenientDatetimeFormat;
    private int dateLength;

    private InputStream sslCaCert;
    private boolean verifyingSsl;
    private KeyManager[] keyManagers;
    private TrustManager[] cachedTrustManagers;

    private CloseableHttpAsyncClient asyncHttpClient;
    private RequestConfig requestConfig;
    private PoolingAsyncClientConnectionManager asyncConnManager;
    private volatile long cachedMaxOperationTimeMs;
    private JSON json;

    private String url;

    /*
     * Constructor for ScimApiClient
     */
    public ScimApiClient() {

        // Get configurations from SCIM2ClientConfig.
        SCIM2ClientConfig config = SCIM2ClientConfig.getInstance();
        int readTimeout = config.getHttpReadTimeoutInMillis();
        int connectionRequestTimeout = config.getHttpConnectionRequestTimeoutInMillis();

        // Build request config with request-level timeout settings.
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeout))
                .setResponseTimeout(Timeout.ofMilliseconds(readTimeout))
                .build();

        // Initialize async HTTP client with NIO.
        this.asyncHttpClient = createAsyncHttpClient(config);
        this.asyncHttpClient.start();

        // Calculate and cache max operation time based on configuration
        this.cachedMaxOperationTimeMs = calculateMaxOperationTime();

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("SCIM2 client initialized with NIO: %d max connections, timeout: %dms",
                    config.getHttpConnectionPoolSize(), cachedMaxOperationTimeMs));
        }

        verifyingSsl = true;
        json = new JSON(this);

        /*
         * Use RFC3339 format for date and datetime.
         * See http://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14
         */
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Always use UTC as the default time zone when dealing with date (without time).
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        initDatetimeFormat();

        // Be lenient on datetime formats when parsing datetime from string.
        // See <code>parseDatetime</code>.
        this.lenientDatetimeFormat = true;

        // Set default User-Agent.
        setUserAgent("wso2/scim2/client");

        // Setup authentications (key: authentication name, value: authentication).
        authentications = new HashMap<>();
        authentications.put("basicAuth", new HttpBasicAuth());
        // Prevent the authentications from being modified.
        authentications = Collections.unmodifiableMap(authentications);
    }

    /**
     * Create and configure async HTTP client for NIO-based non-blocking I/O.
     * HttpClient 5 manages the I/O reactor internally with sensible defaults.
     *
     * @param config SCIM2ClientConfig instance
     * @return Configured CloseableHttpAsyncClient
     */
    private CloseableHttpAsyncClient createAsyncHttpClient(SCIM2ClientConfig config) {

        // Configure connection pool manager for async client.
        this.asyncConnManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(config.getHttpConnectionPoolSize())
                .setMaxConnPerRoute(config.getHttpConnectionPoolSize())
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setSocketTimeout(Timeout.ofMilliseconds(config.getHttpReadTimeoutInMillis()))
                        .setConnectTimeout(Timeout.ofMilliseconds(config.getHttpConnectionTimeoutInMillis()))
                        .build())
                .build();

        // Build async HTTP client (SSL settings will be applied separately if needed).
        return HttpAsyncClients.custom()
                .setConnectionManager(this.asyncConnManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Get async HTTP client
     *
     * @return An instance of CloseableHttpAsyncClient
     */
    public CloseableHttpAsyncClient getAsyncHttpClient() {

        return asyncHttpClient;
    }

    /**
     * Get JSON
     *
     * @return JSON object
     */
    public JSON getJSON() {

        return json;
    }

    /**
     * Set JSON
     *
     * @param json JSON object
     * @return Api client
     */
    public ScimApiClient setJSON(JSON json) {

        this.json = json;
        return this;
    }

    /**
     * True if isVerifyingSsl flag is on
     *
     * @return True if isVerifySsl flag is on
     */
    public boolean isVerifyingSsl() {

        return verifyingSsl;
    }

    /**
     * Configure whether to verify certificate and hostname when making https requests.
     * Default to true.
     * NOTE: Do NOT set to false in production code, otherwise you would face multiple types of cryptographic attacks.
     *
     * @param verifyingSsl True to verify TLS/SSL connection
     * @return ScimApiClient
     */
    public ScimApiClient setVerifyingSsl(boolean verifyingSsl) throws ScimApiException {

        this.verifyingSsl = verifyingSsl;
        applySslSettings();
        return this;
    }

    /**
     * Get SSL CA cert.
     *
     * @return Input stream to the SSL CA cert
     */
    public InputStream getSslCaCert() {

        return sslCaCert;
    }

    /**
     * Configure the CA certificate to be trusted when making https requests.
     * Use null to reset to default.
     *
     * Note: This client takes ownership of the provided InputStream and will close it
     * when close() is called. Do not close the stream externally after passing it to this method.
     *
     * @param sslCaCert input stream for SSL CA cert
     * @return ScimApiClient
     */
    public ScimApiClient setSslCaCert(InputStream sslCaCert) throws ScimApiException {

        this.sslCaCert = sslCaCert;
        this.cachedTrustManagers = null;
        applySslSettings();
        return this;
    }

    public KeyManager[] getKeyManagers() {

        return keyManagers;
    }

    /**
     * Configure client keys to use for authorization in an SSL session.
     * Use null to reset to default.
     *
     * @param managers The KeyManagers to use
     * @return ScimApiClient
     */
    public ScimApiClient setKeyManagers(KeyManager[] managers) throws ScimApiException {

        this.keyManagers = managers;
        applySslSettings();
        return this;
    }

    public DateFormat getDateFormat() {

        return dateFormat;
    }

    public ScimApiClient setDateFormat(DateFormat dateFormat) {

        this.dateFormat = dateFormat;
        this.dateLength = this.dateFormat.format(new Date()).length();
        return this;
    }

    public DateFormat getDatetimeFormat() {

        return datetimeFormat;
    }

    public ScimApiClient setDatetimeFormat(DateFormat datetimeFormat) {

        this.datetimeFormat = datetimeFormat;
        return this;
    }

    /**
     * Whether to allow various ISO 8601 datetime formats when parsing a datetime string.
     *
     * @return True if lenientDatetimeFormat flag is set to true
     * @see #parseDatetime(String)
     */
    public boolean isLenientDatetimeFormat() {

        return lenientDatetimeFormat;
    }

    public ScimApiClient setLenientDatetimeFormat(boolean lenientDatetimeFormat) {

        this.lenientDatetimeFormat = lenientDatetimeFormat;
        return this;
    }

    /**
     * Parse the given date string into Date object.
     * The default <code>dateFormat</code> supports these ISO 8601 date formats:
     * 2015-08-16
     * 2015-8-16
     *
     * @param str String to be parsed
     * @return Date
     */
    public Date parseDate(String str) throws ScimApiException {

        if (StringUtils.isEmpty(str)) return null;
        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            throw new ScimApiException("Unable to parse the date.", e);
        }
    }

    /**
     * Parse the given datetime string into Date object.
     * When lenientDatetimeFormat is enabled, the following ISO 8601 datetime formats are supported:
     * 2015-08-16T08:20:05Z
     * 2015-8-16T8:20:05Z
     * 2015-08-16T08:20:05+00:00
     * 2015-08-16T08:20:05+0000
     * 2015-08-16T08:20:05.376Z
     * 2015-08-16T08:20:05.376+00:00
     * 2015-08-16T08:20:05.376+00
     * Note: The 3-digit milliseconds is optional. Time zone is required and can be in one of
     * these formats:
     * Z (same with +0000)
     * +08:00 (same with +0800)
     * -02 (same with -0200)
     * -0200
     *
     * @param str Date time string to be parsed
     * @return Date representation of the string
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
     */
    public Date parseDatetime(String str) throws ScimApiException {

        if (str == null)
            return null;

        DateFormat format;
        if (lenientDatetimeFormat) {
            /*
             * When lenientDatetimeFormat is enabled, normalize the date string
             * into <code>LENIENT_DATETIME_FORMAT</code> to support various formats
             * defined by ISO 8601.
             */
            // normalize time zone
            //   trailing "Z": 2015-08-16T08:20:05Z => 2015-08-16T08:20:05+0000
            str = str.replaceAll("[zZ]\\z", "+0000");
            //   remove colon in time zone: 2015-08-16T08:20:05+00:00 => 2015-08-16T08:20:05+0000
            str = str.replaceAll("([+-]\\d{2}):(\\d{2})\\z", "$1$2");
            //   expand time zone: 2015-08-16T08:20:05+00 => 2015-08-16T08:20:05+0000
            str = str.replaceAll("([+-]\\d{2})\\z", "$100");
            // add milliseconds when missing
            //   2015-08-16T08:20:05+0000 => 2015-08-16T08:20:05.000+0000
            str = str.replaceAll("(:\\d{1,2})([+-]\\d{4})\\z", "$1.000$2");
            format = new SimpleDateFormat(LENIENT_DATETIME_FORMAT);
        } else {
            format = this.datetimeFormat;
        }

        try {
            return format.parse(str);
        } catch (ParseException e) {
            throw new ScimApiException("Unable to parse the date time.", e);
        }
    }

    /*
     * Parse date or date time in string format into Date object.
     *
     * @param str Date time string to be parsed
     * @return Date representation of the string
     */
    public Date parseDateOrDatetime(String str) throws ScimApiException {

        if (str == null)
            return null;
        else if (str.length() <= dateLength)
            return parseDate(str);
        else
            return parseDatetime(str);
    }

    /**
     * Format the given Date object into string (Date format).
     *
     * @param date Date object
     * @return Formatted date in string representation
     */
    public String formatDate(Date date) {

        return dateFormat.format(date);
    }

    /**
     * Format the given Date object into string (Datetime format).
     *
     * @param date Date object
     * @return Formatted datetime in string representation
     */
    public String formatDatetime(Date date) {

        return datetimeFormat.format(date);
    }

    /**
     * Get authentications (key: authentication name, value: authentication).
     *
     * @return Map of authentication objects
     */
    public Map<String, Authentication> getAuthentications() {

        return authentications;
    }

    /**
     * Get authentication for the given name.
     *
     * @param authName The authentication name
     * @return The authentication, null if not found
     */
    public Authentication getAuthentication(String authName) {

        return authentications.get(authName);
    }

    /**
     * Helper method to set username for the first HTTP basic authentication.
     *
     * @param username Username
     */
    public void setUsername(String username) throws ScimApiException {

        for (Authentication auth : authentications.values()) {
            if (auth instanceof HttpBasicAuth) {
                ((HttpBasicAuth) auth).setUsername(username);
                return;
            }
        }
        throw new ScimApiException("No HTTP basic authentication configured!");
    }

    /**
     * Helper method to set password for the first HTTP basic authentication.
     *
     * @param password Password
     */
    public void setPassword(String password) throws ScimApiException {

        for (Authentication auth : authentications.values()) {
            if (auth instanceof HttpBasicAuth) {
                ((HttpBasicAuth) auth).setPassword(password);
                return;
            }
        }
        throw new ScimApiException("No HTTP basic authentication configured!");
    }

    /**
     * Helper method to set API key value for the first API key authentication.
     *
     * @param apiKey API key
     */
    public void setApiKey(String apiKey) throws ScimApiException {

        for (Authentication auth : authentications.values()) {
            if (auth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) auth).setApiKey(apiKey);
                return;
            }
        }
        throw new ScimApiException("No API key authentication configured!");
    }

    /**
     * Helper method to set API key prefix for the first API key authentication.
     *
     * @param apiKeyPrefix API key prefix
     */
    public void setApiKeyPrefix(String apiKeyPrefix) throws ScimApiException {

        for (Authentication auth : authentications.values()) {
            if (auth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) auth).setApiKeyPrefix(apiKeyPrefix);
                return;
            }
        }
        throw new ScimApiException("No API key authentication configured!");
    }

    /**
     * Helper method to set access token for the first OAuth2 authentication.
     *
     * @param accessToken Access token
     */
    public void setAccessToken(String accessToken) throws ScimApiException {

        for (Authentication auth : authentications.values()) {
            if (auth instanceof OAuth) {
                ((OAuth) auth).setAccessToken(accessToken);
                return;
            }
        }
        throw new ScimApiException("No OAuth2 authentication configured!");
    }

    /**
     * Set the User-Agent header's value (by adding to the default header map).
     *
     * @param userAgent HTTP request's user agent
     * @return ScimApiClient
     */
    public ScimApiClient setUserAgent(String userAgent) {

        addDefaultHeader("User-Agent", userAgent);
        return this;
    }

    /**
     * Add a default header.
     *
     * @param key   The header's key
     * @param value The header's value
     * @return ScimApiClient
     */
    public ScimApiClient addDefaultHeader(String key, String value) {

        defaultHeaderMap.put(key, value);
        return this;
    }

    /**
     * @return True if lenientOnJson is enabled, false otherwise.
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/stream/JsonReader.html
     * #setLenient(boolean)">setLenient</a>
     */
    public boolean isLenientOnJson() {

        return lenientOnJson;
    }

    /**
     * Set LenientOnJson
     *
     * @param lenient True to enable lenientOnJson
     * @return ScimApiClient
     */
    public ScimApiClient setLenientOnJson(boolean lenient) {

        this.lenientOnJson = lenient;
        return this;
    }

    /**
     * Check whether debugging is enabled for this API client.
     *
     * @return True if debugging is enabled, false otherwise.
     */
    @Deprecated
    public boolean isDebugging() {

        return debugging;
    }

    /**
     * Enable/disable debugging for this API client.
     * Note: Debugging with request/response interceptors is not currently supported with async NIO client.
     *
     * @param debugging To enable (true) or disable (false) debugging
     * @return ScimApiClient
     */
    @Deprecated
    public ScimApiClient setDebugging(boolean debugging) {

        this.debugging = debugging;
        if (debugging) {
            logger.warn("Debugging mode with request/response interceptors is not supported with NIO async client");
        }
        return this;
    }

    /**
     * The path of temporary folder used to store downloaded files from endpoints
     * with file response. The default value is <code>null</code>, i.e. using
     * the system's default temporary folder.
     *
     * @return Temporary folder path
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/io/File.html#createTempFile">createTempFile</a>
     */
    public String getTempFolderPath() {

        return tempFolderPath;
    }

    /**
     * Set the temporary folder path (for downloading files)
     *
     * @param tempFolderPath Temporary folder path
     * @return ScimApiClient
     */
    public ScimApiClient setTempFolderPath(String tempFolderPath) {

        this.tempFolderPath = tempFolderPath;
        return this;
    }

    /**
     * Get connection timeout (in milliseconds).
     * Note: In HttpClient 5, connect timeout is configured at connection level.
     *
     * @return Timeout in milliseconds
     */
    public int getConnectTimeout() {

        return SCIM2ClientConfig.getInstance().getHttpConnectionTimeoutInMillis();
    }

    /**
     * Sets the connection timeout (in milliseconds).
     * Note: Connection timeout should be set via SCIM2ClientConfig before creating the client.
     * Dynamic timeout changes are not supported with NIO async client.
     *
     * @param connectionTimeout connection timeout in milliseconds
     * @return Api client
     */
    @Deprecated
    public ScimApiClient setConnectTimeout(int connectionTimeout) {

        logger.warn("Dynamic connection timeout changes are not supported with NIO async client. " +
                "Use SCIM2ClientConfig.registerTimeoutConfig() before client initialization.");
        return this;
    }

    /**
     * Format the given parameter object into string.
     *
     * @param param Parameter
     * @return String representation of the parameter
     */
    public String parameterToString(Object param) {

        if (param == null) {
            return "";
        } else if (param instanceof Date) {
            return formatDatetime((Date) param);
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection) param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(String.valueOf(o));
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    /**
     * Format to {@code Pair} objects.
     *
     * @param collectionFormat collection format (e.g. csv, tsv)
     * @param name             Name
     * @param value            Value
     * @return A list of Pair objects
     */
    public List<Pair> parameterToPairs(String collectionFormat, String name, Object value) {

        List<Pair> params = new ArrayList<Pair>();
        // preconditions
        if (name == null || name.isEmpty() || value == null) return params;
        Collection valueCollection = null;
        if (value instanceof Collection) {
            valueCollection = (Collection) value;
        } else {
            params.add(new Pair(name, parameterToString(value)));
            return params;
        }
        if (valueCollection.isEmpty()) {
            return params;
        }
        // get the collection format
        collectionFormat = (collectionFormat == null || collectionFormat.isEmpty() ? "csv" : collectionFormat);
        // default: csv
        // create the params based on the collection format
        if (collectionFormat.equals("multi")) {
            for (Object item : valueCollection) {
                params.add(new Pair(name, parameterToString(item)));
            }
            return params;
        }
        String delimiter = ",";
        if (collectionFormat.equals("csv")) {
            delimiter = ",";
        } else if (collectionFormat.equals("ssv")) {
            delimiter = " ";
        } else if (collectionFormat.equals("tsv")) {
            delimiter = "\t";
        } else if (collectionFormat.equals("pipes")) {
            delimiter = "|";
        }
        StringBuilder sb = new StringBuilder();
        for (Object item : valueCollection) {
            sb.append(delimiter);
            sb.append(parameterToString(item));
        }
        params.add(new Pair(name, sb.substring(1)));
        return params;
    }

    /**
     * Sanitize filename by removing path.
     * e.g. ../../sun.gif becomes sun.gif
     *
     * @param filename The filename to be sanitized
     * @return The sanitized filename
     */
    public String sanitizeFilename(String filename) {

        return filename.replaceAll(".*[/\\\\]", "");
    }

    /**
     * Check if the given MIME is a JSON MIME.
     * JSON MIME examples:
     * application/json
     * application/json; charset=UTF8
     * APPLICATION/JSON
     * application/vnd.company+json
     *
     * @param mime MIME (Multipurpose Internet Mail Extensions)
     * @return True if the given MIME is JSON, false otherwise.
     */
    public boolean isJsonMime(String mime) {

        String jsonMime = "(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$";
        return mime != null && (mime.matches(jsonMime) || mime.equalsIgnoreCase("application/json-patch+json"));
    }

    /**
     * Select the Accept header's value from the given accepts array:
     * if JSON exists in the given array, use it;
     * otherwise use all of them (joining into a string)
     *
     * @param accepts The accepts array to select from
     * @return The Accept header to use. If the given array is empty,
     * null will be returned (not to set the Accept header explicitly).
     */
    public String selectHeaderAccept(String[] accepts) {
        /*if (accepts.length == 0) {
            return null;
        }
        for (String accept : accepts) {
            if (isJsonMime(accept)) {
                return accept;
            }
        }
        return StringUtil.join(accepts, ",");*/
        return "*/*";
    }

    /**
     * Select the Content-Type header's value from the given array:
     * if JSON exists in the given array, use it;
     * otherwise use the first one of the array.
     *
     * @param contentTypes The Content-Type array to select from
     * @return The Content-Type header to use. If the given array is empty,
     * JSON will be used.
     */
    public String selectHeaderContentType(String[] contentTypes) {

        if (contentTypes.length == 0) {
            return JSON_CONTENT_TYPE;
        }
        for (String contentType : contentTypes) {
            if (isJsonMime(contentType)) {
                return contentType;
            }
        }
        return contentTypes[0];
    }

    /**
     * Escape the given string to be used as URL query value.
     *
     * @param str String to be escaped
     * @return Escaped string
     */
    public String escapeString(String str) {

        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * Deserialize response body to Java object, according to the return type and
     * the Content-Type response header.
     *
     * @param <T>        Type
     * @param response   HTTP response
     * @param returnType The type of the Java object
     * @return The deserialized Java object
     * @throws ScimApiException If fail to deserialize response body, i.e. cannot read response body
     *                          or the Content-Type of the response is not supported.
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(SimpleHttpResponse response, Type returnType) throws ScimApiException {

        if (response == null || returnType == null) {
            return null;
        }

        if ("byte[]".equals(returnType.toString())) {
            // Handle binary response (byte array).
            byte[] bodyBytes = response.getBodyBytes();
            return (T) bodyBytes;
        } else if (returnType.equals(File.class)) {
            // Handle file downloading.
            return (T) downloadFileFromResponse(response);
        }

        String respBody = response.getBodyText();

        if (respBody == null || "".equals(respBody)) {
            return null;
        }

        String contentType = response.getFirstHeader("Content-Type").getValue();
        if (contentType == null) {
            // ensuring a default content type
            contentType = JSON_CONTENT_TYPE;
        }
        if (isJsonMime(contentType)) {
            return json.deserialize(respBody, returnType);
        } else if (returnType.equals(String.class)) {
            // Expecting string, return the raw response body.
            return (T) respBody;
        } else {
            throw new ScimApiException(
                    "Content type \"" + contentType + "\" is not supported for type: " + returnType,
                    response.getCode(),
                    extractHeaders(response),
                    respBody);
        }
    }

    /**
     * Serialize the given Java object into request body according to the object's
     * class and the request Content-Type.
     *
     * @param obj         The Java object
     * @param contentType The request Content-Type
     * @return The serialized request body
     * @throws ScimApiException If fail to serialize the given object
     */
    public HttpEntity serialize(Object obj, String contentType) throws ScimApiException {

        if (obj instanceof byte[]) {
            // Binary (byte array) body parameter support.
            return new ByteArrayEntity((byte[]) obj, ContentType.parse(contentType));
        } else if (obj instanceof File) {
            // File body parameter support.
            File file = (File) obj;
            return new FileEntity(file, ContentType.parse(contentType));
        } else if (isJsonMime(contentType)) {
            String content;
            if (obj != null) {
                content = (String) obj;
            } else {
                content = "";
            }
            return new ByteArrayEntity(content.getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON);
        } else {
            throw new ScimApiException("Content type \"" + contentType + "\" is not supported");
        }
    }

    /**
     * Download file from the given response.
     *
     * @param response An instance of the Response object
     * @return Downloaded file
     * @throws ScimApiException If fail to read file content from response and write to disk
     */
    public File downloadFileFromResponse(SimpleHttpResponse response) throws ScimApiException {

        try {
            File file = prepareDownloadFile(response);
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            byte[] bodyBytes = response.getBodyBytes();
            if (bodyBytes != null) {
                sink.write(bodyBytes);
            }
            sink.close();
            return file;
        } catch (IOException e) {
            throw new ScimApiException(e);
        }
    }

    /**
     * Prepare file for download
     *
     * @param response An instance of the Response object
     * @return Prepared file for the download
     * @throws IOException If fail to prepare file for download
     */
    public File prepareDownloadFile(SimpleHttpResponse response) throws IOException {

        String filename = null;
        Header contentDispositionHeader = response.getFirstHeader("Content-Disposition");
        String contentDisposition = contentDispositionHeader != null ? contentDispositionHeader.getValue() : null;
        if (contentDisposition != null && !"".equals(contentDisposition)) {
            // Get filename from the Content-Disposition header.
            Pattern pattern = Pattern.compile("filename=['\"]?([^'\"\\s]+)['\"]?");
            Matcher matcher = pattern.matcher(contentDisposition);
            if (matcher.find()) {
                filename = sanitizeFilename(matcher.group(1));
            }
        }

        String prefix = null;
        String suffix = null;
        if (filename == null) {
            prefix = "download-";
            suffix = "";
        } else {
            int pos = filename.lastIndexOf(".");
            if (pos == -1) {
                prefix = filename + "-";
            } else {
                prefix = filename.substring(0, pos) + "-";
                suffix = filename.substring(pos);
            }
            // File.createTempFile requires the prefix to be at least three characters long
            if (prefix.length() < 3)
                prefix = "download-";
        }

        if (tempFolderPath == null)
            return File.createTempFile(prefix, suffix);
        else
            return File.createTempFile(prefix, suffix, new File(tempFolderPath));
    }

    /**
     * Executes the provided HTTP request.
     *
     * @param <T>     Type
     * @param request An instance of the Call object
     * @return ScimApiResponse&lt;T&gt;
     * @throws ScimApiException If fail to execute the call
     */
    public <T> ScimApiResponse<T> execute(HttpUriRequest request) throws ScimApiException {

        return execute(request, null);
    }

    /**
     * Execute HTTP call and deserialize the HTTP response body into the given return type.
     * This method implements a retry mechanism for handling transient network failures.
     *
     * @param returnType The return type used to deserialize HTTP response body
     * @param <T>        The return type corresponding to (same with) returnType
     * @param request    HttpUriRequest
     * @return ScimApiResponse object containing response status, headers and
     * data, which is a Java object deserialized from response body and would be null
     * when returnType is null.
     * @throws ScimApiException If fail to execute the call
     */
    public <T> ScimApiResponse<T> execute(HttpUriRequest request, Type returnType) throws ScimApiException {

        return executeAsyncBlocking(request, returnType);
    }

    /**
     * Execute request asynchronously and block on result (backward compatible).
     * Internally uses NIO but blocks the calling thread until completion.
     *
     * Timeout is calculated once based on configuration and cached:
     * - (readTimeout + connectionTimeout) × (retryCount + 1) for all attempts
     * - Plus sum of all backoff delays between retries
     * - Plus 10% buffer for async overhead
     *
     * @param request    HttpUriRequest
     * @param returnType The return type
     * @param <T>        The return type
     * @return ScimApiResponse
     * @throws ScimApiException If execution fails
     */
    @SuppressWarnings("unchecked")
    private <T> ScimApiResponse<T> executeAsyncBlocking(HttpUriRequest request, Type returnType)
            throws ScimApiException {

        try {
            // Use cached timeout (calculated once, only recalculated when config changes)
            CompletableFuture<ScimApiResponse<T>> future =
                    (CompletableFuture<ScimApiResponse<T>>) (CompletableFuture<?>) executeAsync(request, returnType);
            return future.get(cachedMaxOperationTimeMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            long timeoutSec = cachedMaxOperationTimeMs / 1000;
            throw new ScimApiException("Request timed out after " + timeoutSec + " seconds", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ScimApiException("Request interrupted", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ScimApiException) {
                throw (ScimApiException) cause;
            }
            throw new ScimApiException("Request failed: " + cause.getMessage(), cause);
        }
    }

    /**
     * Calculate maximum operation time including all retries and backoff delays.
     * Formula:
     * - (readTimeout + connectionTimeout) × (retryCount + 1) for all HTTP attempts
     * - Plus sum of exponential backoff delays: 1s + 2s + 4s + 8s + ... (up to max)
     * - Plus 50% buffer for async overhead
     *
     * @return Maximum operation time in milliseconds
     */
    private long calculateMaxOperationTime() {

        SCIM2ClientConfig config = SCIM2ClientConfig.getInstance();
        int retryCount = config.getHttpRequestRetryCount();
        Timeout responseTimeout = requestConfig.getResponseTimeout();

        // Get connect timeout from config (it's set at connection level, not request level)
        long readTimeout = responseTimeout != null ? responseTimeout.toMilliseconds() : 5000;
        long connTimeout = config.getHttpConnectionTimeoutInMillis();

        // Time for all HTTP attempts (initial + retries).
        long totalAttemptTime = (readTimeout + connTimeout) * (retryCount + 1);

        // Sum of all backoff delays: 1s + 2s + 4s + 8s + 16s + 32s...
        long totalBackoffTime = 0;
        for (int i = 0; i < retryCount; i++) {
            totalBackoffTime += calculateBackoffDelay(i);
        }

        // Total time + 50% buffer for async overhead.
        long totalTime = totalAttemptTime + totalBackoffTime;
        return (long) (totalTime * 1.5);
    }

    /**
     * Execute HTTP request asynchronously using NIO (non-blocking).
     * Returns CompletableFuture immediately - does NOT block calling thread.
     *
     * @param request    HTTP request to execute
     * @param returnType Expected response type
     * @param <T>        The return type
     * @return CompletableFuture that completes when response is received
     */
    public <T> CompletableFuture<ScimApiResponse<T>> executeAsync(HttpUriRequest request, Type returnType) {

        int retryCount = SCIM2ClientConfig.getInstance().getHttpRequestRetryCount();

        if (retryCount == 0) {
            return executeSingleAsync(request, returnType);
        }

        return executeWithRetryAsync(request, returnType, 0, retryCount);
    }

    /**
     * Single async execution without retry.
     *
     * @param request    HTTP request
     * @param returnType Expected response type
     * @param <T>        The return type
     * @return CompletableFuture with response
     */
    private <T> CompletableFuture<ScimApiResponse<T>> executeSingleAsync(
            final HttpUriRequest request, final Type returnType) {

        final CompletableFuture<ScimApiResponse<T>> future = new CompletableFuture<>();

        try {
            // Convert classic request to simple request for async execution
            SimpleHttpRequest simpleRequest = convertToSimpleRequest(request);

            asyncHttpClient.execute(simpleRequest, new FutureCallback<SimpleHttpResponse>() {

                @Override
                public void completed(SimpleHttpResponse response) {
                    try {
                        T data = handleResponse(response, returnType);
                        ScimApiResponse<T> scimResponse = new ScimApiResponse<>(
                                response.getCode(),
                                extractHeaders(response),
                                data
                        );
                        future.complete(scimResponse);
                    } catch (Exception e) {
                        future.completeExceptionally(new ScimApiException("Response handling failed: " +
                                e.getMessage(), e));
                    }
                }

                @Override
                public void failed(Exception ex) {
                    future.completeExceptionally(new ScimApiException("Request failed: " + ex.getMessage(), ex));
                }

                @Override
                public void cancelled() {
                    future.completeExceptionally(new ScimApiException("Request cancelled"));
                }
            });
        } catch (IOException e) {
            future.completeExceptionally(new ScimApiException("Failed to convert request: " + e.getMessage(), e));
        }

        return future;
    }

    /**
     * Recursive async retry logic with non-blocking backoff.
     *
     * @param request    HTTP request
     * @param returnType Expected response type
     * @param attempt    Current attempt number (0-based)
     * @param maxRetries Maximum retry count
     * @param <T>        The return type
     * @return CompletableFuture with response
     */
    private <T> CompletableFuture<ScimApiResponse<T>> executeWithRetryAsync(
            final HttpUriRequest request, final Type returnType, final int attempt, final int maxRetries) {

        return executeSingleAsync(request, returnType)
                .handle((response, exception) -> {
                    if (exception != null) {
                        // Exception during request - check if retryable.
                        Throwable cause = exception instanceof CompletionException ?
                                exception.getCause() : exception;

                        if (!isRetryableException(cause) || attempt >= maxRetries) {
                            if (attempt >= maxRetries) {
                                logger.warn(String.format("Max retries (%d) reached for %s due to exception",
                                        maxRetries, getRequestUri(request)), cause);
                            }
                            CompletableFuture<ScimApiResponse<T>> failed = new CompletableFuture<>();
                            failed.completeExceptionally(cause);
                            return failed;
                        }

                        // Schedule retry - NON-BLOCKING!
                        long delay = calculateBackoffDelay(attempt);
                        logRetryAttempt(request, attempt + 1, maxRetries, cause.getMessage());

                        return scheduleRetry(() ->
                                executeWithRetryAsync(request, returnType, attempt + 1, maxRetries), delay);
                    }

                    // Success case - return response if not retryable.
                    if (!isRetryableResponse(response.getStatusCode())) {
                        return CompletableFuture.completedFuture(response);
                    }

                    // Retryable error - check if retries remaining.
                    if (attempt >= maxRetries) {
                        logger.warn(String.format("Max retries (%d) reached for %s with status %d",
                                maxRetries, getRequestUri(request), response.getStatusCode()));
                        return CompletableFuture.completedFuture(response);
                    }

                    // Schedule retry with backoff - NON-BLOCKING!
                    long delay = calculateBackoffDelay(attempt);
                    logRetryAttempt(request, attempt + 1, maxRetries,
                            String.format("Server error %d", response.getStatusCode()));

                    return scheduleRetry(() ->
                            executeWithRetryAsync(request, returnType, attempt + 1, maxRetries), delay);
                })
                .thenCompose(result -> {
                    @SuppressWarnings("unchecked")
                    CompletableFuture<ScimApiResponse<T>> typedResult =
                            (CompletableFuture<ScimApiResponse<T>>) (CompletableFuture<?>) result;
                    return typedResult;
                });
    }

    /**
     * Schedule a retry after delay using CompletableFuture's built-in delay executor (non-blocking).
     *
     * @param retryAction Action to execute after delay
     * @param delayMs     Delay in milliseconds
     * @param <T>         Return type
     * @return CompletableFuture that completes after delay
     */
    private <T> CompletableFuture<T> scheduleRetry(Supplier<CompletableFuture<T>> retryAction, long delayMs) {

        return CompletableFuture.supplyAsync(() -> null,
                CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS))
                .thenCompose(ignored -> retryAction.get());
    }

    /**
     * Check if exception is retryable.
     *
     * @param throwable Exception to check
     * @return true if retryable, false otherwise
     */
    private boolean isRetryableException(Throwable throwable) {

        return throwable instanceof ConnectException ||
                throwable instanceof SocketTimeoutException ||
                throwable instanceof IOException;
    }

    /**
     * Check if the HTTP status code indicates a retryable response.
     */
    private boolean isRetryableResponse(int statusCode) {

        return RETRYABLE_STATUS_CODES.contains(statusCode);
    }

    /**
     * Log retry attempt information.
     */
    private void logRetryAttempt(HttpUriRequest request, int currentAttempt, int retryCount, String reason) {

        if (!logger.isDebugEnabled()) {
            return;
        }

        if (currentAttempt < retryCount) {
            logger.debug(String.format("Request for API: %s failed due to %s. Retrying attempt %d of %d.",
                    getRequestUri(request), reason, currentAttempt, retryCount - 1));
        } else {
            logger.debug(String.format("Request for API: %s failed due to %s. Maximum retry attempts reached.",
                    getRequestUri(request), reason));
        }
    }

    /**
     * Calculate exponential backoff delay in milliseconds.
     * Uses formula: min(BACKOFF_INITIAL_DELAY_MS * (BACKOFF_MULTIPLIER ^ attempt), BACKOFF_MAX_DELAY_MS)
     *
     * @param attempt Current attempt number (0-based)
     * @return Delay in milliseconds
     */
    private long calculateBackoffDelay(int attempt) {

        long delay = (long) (BACKOFF_INITIAL_DELAY_MS * Math.pow(BACKOFF_MULTIPLIER, attempt));
        return Math.min(delay, BACKOFF_MAX_DELAY_MS);
    }

    /**
     * Handle the given response, return the deserialized object when the response is successful.
     *
     * @param <T>        Type
     * @param response   Response
     * @param returnType Return type
     * @return Type
     * @throws ScimApiException If the response has a unsuccessful status code or
     *                          fail to deserialize the response body
     */
    public <T> T handleResponse(SimpleHttpResponse response, Type returnType) throws ScimApiException {

        int statusCode = response.getCode();
        String bodyText = response.getBodyText();
        if (statusCode >= 200 && statusCode < 300) {
            if (returnType == null || statusCode == 204) {
                // returning null if the returnType is not defined,
                // or the status code is 204 (No Content)
                return null;
            } else {
                return deserialize(response, returnType);
            }
        } else {
            String respBody = bodyText != null ? bodyText : "";
            throw new ScimApiException(response.getReasonPhrase(), statusCode, extractHeaders(response),
                    respBody);
        }
    }

    /**
     * Build HTTP call with the given options.
     *
     * @param method       The request method, one of "GET", "HEAD", "OPTIONS", "POST", "PUT", "PATCH" and "DELETE"
     * @param queryParams  The query parameters
     * @param body         The request body object
     * @param headerParams The header parameters
     * @param formParams   The form parameters
     * @param authNames    The authentications to apply
     * @return The HTTP call
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest buildCall(String method, List<Pair> queryParams, Object body,
                                    Map<String, String> headerParams,
                                    Map<String, Object> formParams, String[] authNames) throws ScimApiException {

        return buildRequest(method, queryParams, body, headerParams, formParams, authNames);
    }

    public void setURL(String url) {

        this.url = url;
    }

    /**
     * Build an HTTP request with the given options.
     *
     * @param method       The request method, one of "GET", "HEAD", "OPTIONS", "POST", "PUT", "PATCH" and "DELETE"
     * @param queryParams  The query parameters
     * @param body         The request body object
     * @param headerParams The header parameters
     * @param formParams   The form parameters
     * @param authNames    The authentications to apply
     * @return The HTTP request
     * @throws ScimApiException If fail to serialize the request body object
     */
    public HttpUriRequest buildRequest(String method, List<Pair> queryParams, Object body,
                                       Map<String, String> headerParams,
                                       Map<String, Object> formParams, String[] authNames) throws ScimApiException {

        updateParamsForAuth(authNames, queryParams, headerParams);
        String path = this.url;
        final String requestUrl = buildUrl(path, queryParams);
        final HttpUriRequest request = createHttpRequest(method, requestUrl);
        processHeaderParams(headerParams, request);
        String contentType = headerParams.get("Content-Type");
        // ensuring a default content type
        if (contentType == null) {
            contentType = JSON_CONTENT_TYPE;
        }

        HttpEntity reqBody;
        if (request instanceof HttpUriRequestBase) {
            HttpUriRequestBase entityRequest = (HttpUriRequestBase) request;
            if (URL_ENCODED_CONTENT_TYPE.equals(contentType)) {
                reqBody = buildRequestBodyFormEncoding(formParams);
            } else if (MULTIPART_FORM_DATA_CONTENT_TYPE.equals(contentType)) {
                reqBody = buildRequestBodyMultipart(formParams);
            } else if (body == null) {
                // Use an empty request body (for POST, PUT, and PATCH).
                // No request body is allowed for DELETE.
                reqBody = new StringEntity("", ContentType.parse(contentType));
            } else {
                reqBody = serialize(body, contentType);
            }
            entityRequest.setEntity(reqBody);
        }
        return request;
    }

    private HttpUriRequest createHttpRequest(String method, String url) {

        switch (HttpMethod.valueOf(method)) {
            case GET:
                return new HttpGet(url);
            case HEAD:
                return new HttpHead(url);
            case OPTIONS:
                return new HttpOptions(url);
            case POST:
                return new HttpPost(url);
            case PUT:
                return new HttpPut(url);
            case PATCH:
                return new HttpPatch(url);
            case DELETE:
                return new HttpDelete(url);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }

    /**
     * Helper method to safely get URI from request.
     */
    private String getRequestUri(HttpUriRequest request) {
        try {
            return request.getUri().toString();
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * Convert classic HttpUriRequest to SimpleHttpRequest for async execution.
     */
    private SimpleHttpRequest convertToSimpleRequest(HttpUriRequest classicRequest) throws IOException {
        SimpleRequestBuilder builder = SimpleRequestBuilder.copy(classicRequest);

        // Copy entity if present
        if (classicRequest instanceof HttpUriRequestBase) {
            HttpUriRequestBase entityRequest = (HttpUriRequestBase) classicRequest;
            HttpEntity entity = entityRequest.getEntity();
            if (entity != null) {
                byte[] content = EntityUtils.toByteArray(entity);
                if (content != null && content.length > 0) {
                    builder.setBody(content, entity.getContentType() != null ?
                        ContentType.parse(entity.getContentType()) : null);
                }
            }
        }

        return builder.build();
    }

    /**
     * Build full URL by concatenating base path, the given sub path and query parameters.
     *
     * @param path        The sub path
     * @param queryParams The query parameters
     * @return The full URL
     */
    public String buildUrl(String path, List<Pair> queryParams) {

        final StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(path);
        if (queryParams != null && !queryParams.isEmpty()) {
            // support (constant) query string in `path`, e.g. "/posts?draft=1"
            String prefix = path.contains("?") ? "&" : "?";
            for (Pair param : queryParams) {
                if (param.getValue() != null) {
                    if (prefix != null) {
                        requestUrl.append(prefix);
                        prefix = null;
                    } else {
                        requestUrl.append("&");
                    }
                    String value = parameterToString(param.getValue());
                    requestUrl.append(escapeString(param.getName())).append("=").append(escapeString(value));
                }
            }
        }
        return requestUrl.toString();
    }

    /**
     * Set header parameters to the request builder, including default headers.
     *
     * @param headerParams Header parameters in the ofrm of Map
     * @param request      HttpUriRequest
     */
    public void processHeaderParams(Map<String, String> headerParams, HttpUriRequest request) {

        for (Entry<String, String> param : headerParams.entrySet()) {
            request.setHeader(param.getKey(), parameterToString(param.getValue()));
        }
        for (Entry<String, String> header : defaultHeaderMap.entrySet()) {
            if (!headerParams.containsKey(header.getKey())) {
                request.setHeader(header.getKey(), parameterToString(header.getValue()));
            }
        }
    }

    /**
     * Update query and header parameters based on authentication settings.
     *
     * @param authNames    The authentications to apply
     * @param queryParams  List of query parameters
     * @param headerParams Map of header parameters
     */
    public void updateParamsForAuth(String[] authNames, List<Pair> queryParams, Map<String, String> headerParams)
            throws ScimApiException {

        for (String authName : authNames) {
            Authentication auth = authentications.get(authName);
            if (auth == null) throw new ScimApiException("Authentication undefined: " + authName);
            auth.applyToParams(queryParams, headerParams);
        }
    }

    /**
     * Build a form-encoding request body with the given form parameters.
     *
     * @param formParams Form parameters in the form of Map
     * @return RequestBody
     */
    public HttpEntity buildRequestBodyFormEncoding(Map<String, Object> formParams) {

        List<NameValuePair> params = new ArrayList<>();
        for (Entry<String, Object> param : formParams.entrySet()) {
            params.add(new BasicNameValuePair(param.getKey(), parameterToString(param.getValue())));
        }

        return new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
    }

    /**
     * Build a multipart (file uploading) request body with the given form parameters,
     * which could contain text fields and file fields.
     *
     * @param formParams Form parameters in the form of Map
     * @return RequestBody
     */
    public HttpEntity buildRequestBodyMultipart(Map<String, Object> formParams) {

        MultipartEntityBuilder entityBuilder =
                MultipartEntityBuilder.create().setContentType(ContentType.MULTIPART_FORM_DATA);

        for (Entry<String, Object> param : formParams.entrySet()) {
            if (param.getValue() instanceof File) {
                File file = (File) param.getValue();
                String fileName = file.getName();
                ContentType contentType = ContentType.parse(guessContentTypeFromFile(file));
                entityBuilder.addPart(param.getKey(), new FileBody(file, contentType, fileName));
            } else {
                String paramValue = parameterToString(param.getValue());
                entityBuilder.addPart(param.getKey(), new StringBody(paramValue, ContentType.DEFAULT_TEXT));
            }
        }

        return entityBuilder.build();
    }

    /**
     * Guess Content-Type header from the given file (defaults to "application/octet-stream").
     *
     * @param file The given file
     * @return The guessed Content-Type
     */
    public String guessContentTypeFromFile(File file) {

        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (contentType == null) {
            return OCTET_STREAM_CONTENT_TYPE;
        } else {
            return contentType;
        }
    }

    /**
     * Initialize datetime format according to the current environment, e.g. Java 1.7 and Android.
     */
    private void initDatetimeFormat() {

        String formatWithTimeZone = null;
        if (JAVA_VERSION >= 1.7) {
            // The time zone format "XXX" is available since Java 1.7
            formatWithTimeZone = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        }
        if (formatWithTimeZone != null) {
            this.datetimeFormat = new SimpleDateFormat(formatWithTimeZone);
            // NOTE: Use the system's default time zone (mainly for datetime formatting).
        } else {
            // Use a common format that works across all systems.
            this.datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            // Always use the UTC time zone as we are using a constant trailing "Z" here.
            this.datetimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
    }

    /**
     * Apply SSL related settings to async HTTP client.
     * Recreates the async client with SSL configuration when SSL settings change.
     */
    private void applySslSettings() throws ScimApiException {

        try {
            TrustManager[] trustManagers = null;
            HostnameVerifier hostnameVerifier = null;
            if (!verifyingSsl) {
                TrustManager trustAll = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {

                        return null;
                    }
                };
                trustManagers = new TrustManager[]{trustAll};
                hostnameVerifier = (hostname, session) -> true;
            } else if (sslCaCert != null) {
                // Use custom CA certificate (cache to avoid reading InputStream multiple times)
                if (cachedTrustManagers == null) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Collection<? extends Certificate> certs = cf.generateCertificates(sslCaCert);
                    KeyStore caKeyStore = newEmptyKeyStore(null);
                    int index = 0;
                    for (Certificate cert : certs) {
                        caKeyStore.setCertificateEntry("ca" + index++, cert);
                    }
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                            TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(caKeyStore);
                    cachedTrustManagers = tmf.getTrustManagers();
                }
                trustManagers = cachedTrustManagers;
            }

            // Only recreate client if we have custom SSL settings
            if (keyManagers != null || trustManagers != null || hostnameVerifier != null) {
                // Close existing async client
                if (asyncHttpClient != null) {
                    try {
                        asyncHttpClient.close();
                    } catch (IOException e) {
                        logger.warn("Error closing async HTTP client during SSL reconfiguration", e);
                    }
                }

                // Create SSL context
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, new SecureRandom());

                // Create TLS strategy with SSL context and hostname verifier (for async client)
                TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                        .setSslContext(sslContext)
                        .setHostnameVerifier(hostnameVerifier)
                        .build();

                // Get configuration values from SCIM2ClientConfig
                SCIM2ClientConfig config = SCIM2ClientConfig.getInstance();

                // Recreate connection manager with TLS strategy
                if (asyncConnManager != null) {
                    asyncConnManager.close();
                }
                asyncConnManager = PoolingAsyncClientConnectionManagerBuilder.create()
                        .setTlsStrategy(tlsStrategy)
                        .setMaxConnTotal(config.getHttpConnectionPoolSize())
                        .setMaxConnPerRoute(config.getHttpConnectionPoolSize())
                        .setDefaultConnectionConfig(ConnectionConfig.custom()
                                .setSocketTimeout(Timeout.ofMilliseconds(config.getHttpReadTimeoutInMillis()))
                                .setConnectTimeout(Timeout.ofMilliseconds(config.getHttpConnectionTimeoutInMillis()))
                                .build())
                        .build();

                // Build new async client with SSL-configured connection manager
                asyncHttpClient = HttpAsyncClients.custom()
                        .setConnectionManager(asyncConnManager)
                        .setDefaultRequestConfig(requestConfig)
                        .build();

                // Start the new async client
                asyncHttpClient.start();

                if (logger.isDebugEnabled()) {
                    logger.debug("Async HTTP client recreated with custom SSL settings");
                }
            }
        } catch (GeneralSecurityException e) {
            throw new ScimApiException("Failed to apply SSL settings to async client", e);
        }
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {

        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private Map<String, List<String>> extractHeaders(SimpleHttpResponse response) {

        return Arrays.stream(response.getHeaders())
                .collect(
                Collectors.groupingBy(
                        Header::getName,
                        Collectors.mapping(Header::getValue, Collectors.toList())
                                     ));
    }

    /**
     * Close all resources properly.
     * MUST be called when client is no longer needed to prevent resource leaks.
     * This method is idempotent and can be safely called multiple times.
     *
     * @throws IOException If an error occurs during shutdown
     */
    @Override
    public void close() throws IOException {

        IOException firstException = null;

        // Close async HTTP client.
        if (asyncHttpClient != null) {
            try {
                asyncHttpClient.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("Async HTTP client closed successfully");
                }
            } catch (IOException e) {
                logger.warn("Error closing async HTTP client", e);
                firstException = e;
            } finally {
                asyncHttpClient = null;
            }
        }

        // Close connection manager to release I/O reactor threads and connection pool resources.
        if (asyncConnManager != null) {
            try {
                asyncConnManager.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("Connection manager closed successfully");
                }
            } catch (Exception e) {
                logger.warn("Error closing connection manager", e);
            } finally {
                asyncConnManager = null;
            }
        }

        // Close SSL CA certificate input stream if present.
        if (sslCaCert != null) {
            try {
                sslCaCert.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("SSL CA certificate input stream closed successfully");
                }
            } catch (IOException e) {
                logger.warn("Error closing SSL CA certificate input stream", e);
                if (firstException == null) {
                    firstException = e;
                }
            } finally {
                sslCaCert = null;
            }
        }

        // Throw the first exception if any occurred.
        if (firstException != null) {
            throw firstException;
        }
    }
}

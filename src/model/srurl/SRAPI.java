/*
 * File: SVTAPI.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.srurl;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstract class for build SR API URL's. Package private. Use implementations
 * for specific API's. No parsing against SR API is performed to test if
 * resulting url actually matching any API.
 *
 * This class is based on the builder design pattern.
 */
abstract class SRAPI {

    static final String TABLEAU_URL =
            "http://api.sr.se/api/v2/scheduledepisodes";
    static final String CHANNELS_URL = "http://api.sr.se/api/v2/channels";

    private StringBuilder url;
    private boolean haveParameters = false;

    /**
     * Initialise Builder with a url base
     * @param baseURL Base for building url upon
     */
    SRAPI(String baseURL) {
        url = new StringBuilder(baseURL);
    }

    /**
     * Disable pagination, i.e resulting XML/JSON/etc will be contained in
     * on page.
     */
    public void disablePagination() {
        appendParameter("pagination", false);
    }

    /**
     * Add filter to only include elements matching key and value. See
     * SR API for valid filter values.
     * @param key
     * @param value
     */
    public void addFilter(String key, String value) {
        appendParameter("filter", key);
        appendParameter("filtervalue", value);
    }

    /**
     * Must be called before adding any parameter. Will add appropriate
     * url character before parameter.
     */
    private void beforeSettingParameter() {
        if (!haveParameters) {
            url.append("?");
            haveParameters = true;
        } else {
            url.append("&");
        }
    }

    /**
     * Add a parameter to url. See SR API for valid keys and values
     * @param key Name of parameter
     * @param value Values of parameter, in form of an int
     */
    void appendParameter(String key, int value) {
        appendParameter(key, Integer.toString(value));
    }

    /**
     * Add a parameter to url. See SR API for valid keys and values
     * @param key Name of parameter
     * @param value Values of parameter, in form of a boolean
     */
    void appendParameter(String key, boolean value) {
        appendParameter(key, Boolean.toString(value));
    }

    /**
     * Add a parameter to url. See SR API for valid keys and values
     * @param key Name of parameter
     * @param value Values of parameter, in form of a string
     */
    void appendParameter(String key, String value) {
        beforeSettingParameter();
        url.append(key);
        url.append("=");
        url.append(value);
    }

    /**
     * Builds a url from given parameters
     * @return A build url from specified parameters
     * @throws MalformedURLException If resulting URL is invalid
     *                               (invalid characters, etc)
     */
    public URL build() throws MalformedURLException{
        return new URL(url.toString());
    }
}

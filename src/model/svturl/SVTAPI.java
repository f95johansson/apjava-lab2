/*
 * File: SVTAPI.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.svturl;

import java.net.MalformedURLException;
import java.net.URL;

abstract class SVTAPI {

    static final String TABLUE_URL = "http://api.sr.se/api/v2/scheduledepisodes";
    static final String CHANNELS_URL = "http://api.sr.se/api/v2/channels";

    private StringBuilder url;
    private boolean haveParameters = false;

    SVTAPI(String baseURL) {
        url = new StringBuilder(baseURL);
    }

    public void disablePagination() {
        appendParameter("pagination", false);
    }

    public void addFilter(String key, String value) {
        appendParameter("filter", key);
        appendParameter("filtervalue", value);
    }

    void beforeSettingParameter() {
        if (!haveParameters) {
            url.append("?");
            haveParameters = true;
        } else {
            url.append("&");
        }
    }

    void appendParameter(String key, int value) {
        appendParameter(key, Integer.toString(value));
    }

    void appendParameter(String key, boolean value) {
        appendParameter(key, Boolean.toString(value));
    }

    void appendParameter(String key, String value) {
        beforeSettingParameter();
        url.append(key);
        url.append("=");
        url.append(value);
    }


    public URL build() throws MalformedURLException{
        return new URL(url.toString());
    }
}

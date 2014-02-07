package com.dropbox.core

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.json.JSONElement

/**
 * Created by hitenpratap on 5/2/14.
 */
class AccessTokenService {

    String appKey = ConfigurationHolder.config.grails.plugins.dropBox.app_key
    String appSecret = ConfigurationHolder.config.grails.plugins.dropBox.app_secret

    def getAuthUrl() {
        URI uri = new URI("https://www.dropbox.com/1/oauth2/authorize")
        StringBuilder requestUri = new StringBuilder(uri.toString())
        requestUri.append("?client_id=")
        requestUri.append(URLEncoder.encode(app_key, "UTF-8"))
        requestUri.append("&response_type=")
        requestUri.append(URLEncoder.encode("code", "UTF-8"))
//        requestUri.append("&redirect_uri=")
//        requestUri.append(URLEncoder.encode("http://127.0.0.1:8080/DropBox/dropBox/getAccessToken","UTF-8"))
        return requestUri
    }

    def getAccessToken(String tempCode) {
        StringBuilder tokenUri = new StringBuilder("code=")
        tokenUri.append(URLEncoder.encode(tempCode, "UTF-8"))
        tokenUri.append("&grant_type=")
        tokenUri.append(URLEncoder.encode("authorization_code", "UTF-8"))
        tokenUri.append("&client_id=")
        tokenUri.append(URLEncoder.encode(app_key, "UTF-8"))
        tokenUri.append("&client_secret=")
        tokenUri.append(URLEncoder.encode(app_secret, "UTF-8"))
//        tokenUri.append("&redirect_uri=")
//        tokenUri.append(URLEncoder.encode("http://127.0.0.1:8080/DropBox/dropBox/getAccessToken","UTF-8"))
        URL url = new URL("https://api.dropbox.com/1/oauth2/token")
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + tokenUri.toString().length());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(tokenUri.toString());
            outputStreamWriter.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream())
            String response = inputStreamReader.text
            JSONElement content = JSON.parse(response)
//            println("******************access_Token="+content.access_token)
//            println("******************token_type="+content.token_type)
//            println("******************uid="+content.uid)
            return content.access_token

        } finally {
            connection?.disconnect()
        }

    }

}

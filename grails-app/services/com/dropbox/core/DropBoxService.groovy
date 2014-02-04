package com.dropbox.core

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement

/**
 * Created by hitenpratap on 2/2/14.
 */
class DropBoxService {

    def accountInfo(String accessToken) {
        StringBuilder stringBuilder = new StringBuilder("https://api.dropbox.com/1/account/info")
        stringBuilder.append("?access_token=")
        stringBuilder.append(URLEncoder.encode(accessToken, "UTF-8"))
        URL url = new URL(stringBuilder.toString())
        String response = url.text
        JSONElement contents = JSON.parse(response)
        String displayName = contents.display_name
        String email = contents.email
        return displayName + '    ' + email
    }


}

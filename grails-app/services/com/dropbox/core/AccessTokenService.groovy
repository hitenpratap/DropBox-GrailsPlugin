package com.dropbox.core

import grails.converters.JSON
import grails.plugin.dropbox.AbstractDropBoxService

import org.codehaus.groovy.grails.web.json.JSONElement
import org.springframework.beans.factory.InitializingBean

/**
 * @author hitenpratap
 */
class AccessTokenService extends AbstractDropBoxService implements InitializingBean {

    static transactional = false

    def grailsApplication

    private String appKey
    private String appSecret

    void afterPropertiesSet() {
        appKey = grailsApplication.config.grails.plugins.dropBox.app_key
        appSecret = grailsApplication.config.grails.plugins.dropBox.app_secret
    }

    String getAuthUrl() {
        String queryString = encodeParams([client_id: appKey, response_type: 'code'])
//        String redirectUri = URLEncoder.encode("http://127.0.0.1:8080/DropBox/dropBox/getAccessToken","UTF-8")
//        String queryString = encodeParams([client_id: appKey, response_type: 'code', redirect_uri: redirectUri])
        return "https://www.dropbox.com/1/oauth2/authorize?${queryString}"
    }

    String getAccessToken(String tempCode) {
        String bodyArgs = encodeParams([code: tempCode, grant_type: 'authorization_code', client_id: appKey, client_secret: appSecret])
        String response = post('/oauth2/token', bodyArgs, null)
        JSONElement content = JSON.parse(response)
//        println("******************access_Token=" + content.access_token)
//        println("******************token_type=" + content.token_type)
//        println("******************uid=" + content.uid)
        return content.access_token
    }
}

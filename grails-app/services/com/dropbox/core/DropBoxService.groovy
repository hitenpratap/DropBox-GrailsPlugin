package com.dropbox.core

import grails.converters.JSON
import grails.plugin.dropbox.AbstractDropBoxService

import org.codehaus.groovy.grails.web.json.JSONElement

/**
 * @author hitenpratap
 */
class DropBoxService extends AbstractDropBoxService {

    static transactional = false

    String accountInfo(String accessToken) {
        String response = get('/account/info', [access_token: accessToken])
        JSONElement contents = JSON.parse(response)
        String displayName = contents.display_name
        String email = contents.email
        return displayName + '    ' + email
    }

    // root should be 'dropbox', 'sandbox', or 'auto'
    String dropBoxFileUpload(String root, String destinationPath, byte[] data, String mimeType, String accessToken) {
        HttpURLConnection connection
        try {
            URL url = new URL("https://api-content.dropbox.com/1/files_put/${root}/${destinationPath}${encodeParams([access_token: accessToken])}")
            connection = createOutputConnection(url, 'PUT', mimeType, data.length)
            write connection, data
            return read(connection)
        }
        finally {
            connection.disconnect()
        }
    }

    String getObjectMetaData(String root, String path, String accessToken) {
        return get("/metadata/${root}/${path}", [access_token: accessToken])
    }

    String createNewFolder(String root, String path, String accessToken) {
        String bodyArgs = encodeParams([root: root, path: path])
        return post('/fileops/create_folder', bodyArgs, accessToken)
    }

    String copyOps(String root, String fromPath, String toPath, String accessToken) {
        String bodyArgs = encodeParams([root: root, from_path: fromPath, to_path: toPath])
        return post('/fileops/copy', bodyArgs, accessToken)
    }

    String deleteFileOps(String root, String path, String accessToken) {
        String bodyArgs = encodeParams([root: root, path: path])
        return post('/fileops/delete', bodyArgs, accessToken)
    }

    String moveOps(String root, String fromPath, String toPath, String accessToken) {
        String bodyArgs = encodeParams([root: root, from_path: fromPath, to_path: toPath])
        return post('/fileops/move', bodyArgs, accessToken)
    }

    /* creates link for sharing a file or returns a share link if existing. */
    String shares(String path, Map params) {
        return get("/shares/auto/${path}", params)
    }
}

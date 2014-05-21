package com.dropbox.core

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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

    def dropBoxFileUpload(String root, String destinationPath,String sourcePath, String accessToken) {
        Path pathFile = Paths.get(sourcePath)
        byte[] data = Files.readAllBytes(pathFile)
        URL url = new URL("https://api-content.dropbox.com/1/files_put/${root}/${destinationPath}?access_token=${accessToken}")
        HttpURLConnection connection
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.setDoOutput(true)
            connection.setRequestMethod("PUT")
            connection.setRequestProperty("Content-Type", "mime/type");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length))
            OutputStream outputStream = connection.getOutputStream()
            outputStream.write(data)
            outputStream.flush()
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream())
            String response = inputStreamReader.text
            return response
        }
        finally {
            connection.disconnect()
        }
    }

    def getObjectMetaData(String root,String path,String accessToken){
        StringBuilder stringBuilder = new StringBuilder("https://api.dropbox.com/1/metadata/${root}/${path}")
        stringBuilder.append("?access_token=")
        stringBuilder.append(URLEncoder.encode(accessToken, "UTF-8"))
        URL url = new URL(stringBuilder.toString())
        String response = url.text
        return response
    }


}

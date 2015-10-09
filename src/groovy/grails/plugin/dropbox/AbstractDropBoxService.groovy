package grails.plugin.dropbox

abstract class AbstractDropBoxService {

    static final String URL_ROOT = 'https://api.dropbox.com/1'

    protected String get(String uri, Map params) {
        return new URL("${URL_ROOT}${uri}?${encodeParams(params)}").text
    }

    protected post(String uri, String body, String accessToken) {
        URL url = new URL("${URL_ROOT}${uri}?${encodeParams([access_token: accessToken])}")
        HttpURLConnection connection
        try {
            connection = createOutputConnection(url, 'POST', 'application/x-www-form-urlencoded', body.length())
            write connection, body
            return read(connection)
        }
        finally {
            connection?.disconnect()
        }
    }

    protected HttpURLConnection createOutputConnection(URL url, String method, String contentType, long contentLength) {
        def connection = url.openConnection()
        connection.doOutput = true
        connection.requestMethod = method
        connection.setRequestProperty 'Content-Type', contentType
        connection.setRequestProperty 'Content-Length', String.valueOf(contentLength)
        connection
    }

    protected void write(HttpURLConnection connection, data) {
        OutputStream outputStream = connection.outputStream
        def out
        if (data instanceof CharSequence) {
            out = new OutputStreamWriter(outputStream)
        }
        else {
            out = outputStream
        }
        out.write data
        out.flush()
    }

    protected String read(HttpURLConnection connection) {
        return new InputStreamReader(connection.inputStream).text
    }

    protected String encodeParams(Map params) {
        String delimiter = ''
        def tokenUri = new StringBuilder()
        params.each { String key, String value ->
            tokenUri.append delimiter
            tokenUri.append key
            tokenUri.append '='
            tokenUri.append URLEncoder.encode(value, 'UTF-8')
            delimiter = '&'
        }
        return tokenUri
    }
}

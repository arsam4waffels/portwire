package com.portwire;

import java.util.Map;

public class HttpRequest {
    /**
     * <h5>Client data retention</h5>
     * <p>A container is created for all the connection
     * protocol attributes to store the information sent
     * from the client.</p>
     */
    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;

    /**
     * <h5>Class constructor</h5>
     * <p>Four variables are received. These are requests sent
     * from the client.</p>
     * @param method Requested method
     * @param path Requested address
     * @param version Client version
     * @param headers Requested topic
     */
    public HttpRequest(String method,
                       String path,
                       String version,
                       Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
    }

    // Methods for encapsulation and returning data
    public String getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }
    public String getVersion() {
        return version;
    }

    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    // Convert to text string
    @Override
    public String toString() {
        return method + " " + path + " " + version;
    }
}

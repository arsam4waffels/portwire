package com.portwire;

import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;

    public HttpRequest(String method,
                       String path,
                       String version,
                       Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
    }

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

    @Override
    public String toString() {
        return method + " " + path + " " + version;
    }
}

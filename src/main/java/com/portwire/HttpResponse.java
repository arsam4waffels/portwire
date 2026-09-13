package com.portwire;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final int statusCode;
    private final String contentType;
    private final String body;

    public HttpResponse(int statusCode,
                        String contentType,
                        String body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
    }

    /**
     * <h5>Factory methods</h5>
     * <p>Instead of writing this garbage:</p>
     * <pre>
     *     new HttpResponse(200, "text/html; charset=UTF-8", body)
     * </pre>
     * <p>We can simply write:</p>
     * <pre>
     *     HttpResponse.ok(body)
     * </pre>
     */
    public static HttpResponse ok(String body) {
        return new HttpResponse(200, "text/html; charset=UTF-8", body);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(404, "text/html; charset=UTF-8",
                "<h1>404 - Not Found</h1>");
    }

    public static HttpResponse badRequest() {
        return new HttpResponse(400, "text/plain; charset=UTF-8",
                "400 - Bad Request");
    }

    /**
     * <h5>HTTP Headers</h5>
     * <ol>
     *     <li><b>HTTP/1.1:</b> Client version.</li>
     *     <li><b>statusCode:</b> Connection status code.</li>
     *     <li><b>Content-Type:</b> Type of requested information.</li>
     *     <li><b>Content-Length:</b> Size of requested information.</li>
     *     <li><b>Connection: close:</b> The connection was closed..</li>
     * </ol>
     */
    public byte[] toBytes() {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        // I finally understood it.
        String headers =
                "HTTP/1.1 " + statusCode + " " + getReasonPhrase() + "\r\n"
                        + "Content-Type: " + contentType + "\r\n"
                        + "Content-Length: " + bodyBytes.length + "\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";

        byte[] headerBytes = headers.getBytes(StandardCharsets.UTF_8);

        byte[] response = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(
                headerBytes,
                0,
                response,
                0,
                headerBytes.length
        );
        System.arraycopy(
                bodyBytes,
                0,
                response,
                headerBytes.length,
                bodyBytes.length
        );

        return response;
    }

    private String getReasonPhrase() {
        return switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            default  -> "Unknown";
        };
    }
}

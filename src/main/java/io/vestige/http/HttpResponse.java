package io.vestige.http;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a raw HTTP response from the API.
 */
public final class HttpResponse {

    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

    public HttpResponse(int statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers != null ? headers : Collections.emptyMap();
    }

    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
    public Map<String, String> getHeaders() { return headers; }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
}

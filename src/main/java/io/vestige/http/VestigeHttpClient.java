package io.vestige.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vestige.config.VestigeConfig;
import io.vestige.exceptions.*;
import io.vestige.models.ErrorResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Internal HTTP client that handles connections, retries, and error mapping.
 * Thread-safe: instances can be shared across threads.
 */
public final class VestigeHttpClient {

    private static final Logger LOG = Logger.getLogger(VestigeHttpClient.class.getName());
    private static final String USER_AGENT = "Vestige-Java/1.0.0";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private final VestigeConfig config;
    private final ObjectMapper objectMapper;

    public VestigeHttpClient(VestigeConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public <T> T request(HttpMethod method, String path, Object requestBody, Class<T> responseType) {
        String jsonBody = null;
        if (requestBody != null) {
            try {
                jsonBody = objectMapper.writeValueAsString(requestBody);
            } catch (Exception e) {
                throw new VestigeException("Failed to serialize request body", e);
            }
        }

        HttpResponse response = executeWithRetries(method, path, jsonBody);

        if (!response.isSuccessful()) {
            handleErrorResponse(response);
        }

        try {
            return objectMapper.readValue(response.getBody(), responseType);
        } catch (Exception e) {
            throw new VestigeException("Failed to deserialize response", e);
        }
    }

    private HttpResponse executeWithRetries(HttpMethod method, String path, String jsonBody) {
        int maxAttempts = config.getMaxRetries() + 1;
        VestigeException lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                HttpResponse response = execute(method, path, jsonBody);

                if (isRetryable(response.getStatusCode()) && attempt < maxAttempts) {
                    long backoff = calculateBackoff(attempt);
                    debug("Retryable status %d on attempt %d/%d, backing off %d ms",
                            response.getStatusCode(), attempt, maxAttempts, backoff);
                    sleep(backoff);
                    continue;
                }
                return response;
            } catch (NetworkException e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    long backoff = calculateBackoff(attempt);
                    debug("Network error on attempt %d/%d: %s — backing off %d ms",
                            attempt, maxAttempts, e.getMessage(), backoff);
                    sleep(backoff);
                } else {
                    throw e;
                }
            }
        }

        throw lastException != null ? lastException
                : new VestigeException("Request failed after " + maxAttempts + " attempts");
    }

    private HttpResponse execute(HttpMethod method, String path, String jsonBody) {
        String url = config.getBaseUrl() + path;
        debug("%s %s", method, url);

        HttpURLConnection conn = null;
        try {
            URL endpoint = new URL(url);
            if (config.getProxy() != null) {
                conn = (HttpURLConnection) endpoint.openConnection(config.getProxy());
            } else {
                conn = (HttpURLConnection) endpoint.openConnection();
            }

            int timeoutMs = (int) config.getTimeout().toMillis();
            conn.setConnectTimeout(timeoutMs);
            conn.setReadTimeout(timeoutMs);
            conn.setRequestMethod(method.name());
            conn.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
            conn.setRequestProperty("Accept", CONTENT_TYPE_JSON);
            conn.setRequestProperty("User-Agent", USER_AGENT);

            if (config.getApiKey() != null && !config.getApiKey().isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
            }

            if (jsonBody != null && (method == HttpMethod.POST || method == HttpMethod.PUT)) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                }
            }

            int statusCode = conn.getResponseCode();
            String responseBody = readStream(statusCode >= 400 ? conn.getErrorStream() : conn.getInputStream());

            Map<String, String> headers = new HashMap<>();
            for (Map.Entry<String, java.util.List<String>> entry : conn.getHeaderFields().entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
                    headers.put(entry.getKey().toLowerCase(), entry.getValue().get(0));
                }
            }

            debug("Response: %d %s", statusCode, truncate(responseBody, 200));
            return new HttpResponse(statusCode, responseBody, headers);

        } catch (java.net.SocketTimeoutException e) {
            throw new NetworkException("Request timed out: " + url, e);
        } catch (IOException e) {
            throw new NetworkException("Network error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void handleErrorResponse(HttpResponse response) {
        String message;
        String errorType = null;

        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
            if (errorResponse.getError() != null) {
                message = errorResponse.getError().getMessage();
                errorType = errorResponse.getError().getType();
            } else {
                message = response.getBody();
            }
        } catch (Exception e) {
            message = response.getBody() != null ? response.getBody() : "Unknown error";
        }

        int status = response.getStatusCode();

        if (status == 401 || status == 403) {
            throw new AuthenticationException(message, status);
        }
        if (status == 429) {
            long retryAfter = parseRetryAfter(response.getHeaders().get("retry-after"));
            throw new RateLimitException(message, retryAfter);
        }
        if (status >= 400 && status < 500) {
            throw new ClientException(message, status, errorType);
        }
        if (status >= 500) {
            throw new ServerException(message, status, errorType);
        }

        throw new VestigeException("Unexpected HTTP status: " + status + " — " + message, status, errorType);
    }

    private static boolean isRetryable(int statusCode) {
        return statusCode == 429 || statusCode >= 500;
    }

    private static long calculateBackoff(int attempt) {
        return Math.min((long) (500 * Math.pow(2, attempt - 1)), 30_000);
    }

    private static long parseRetryAfter(String headerValue) {
        if (headerValue == null) return 0;
        try {
            return (long) (Double.parseDouble(headerValue) * 1000);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        if (inputStream == null) return "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private void debug(String format, Object... args) {
        if (config.isDebugMode()) {
            LOG.log(Level.INFO, "[Vestige] " + String.format(format, args));
        }
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "null";
        return s.length() <= maxLen ? s : s.substring(0, maxLen) + "...";
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VestigeException("Request interrupted", e);
        }
    }
}

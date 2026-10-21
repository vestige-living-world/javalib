package io.vestige.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vestige.config.VestigeConfig;
import io.vestige.http.VestigeHttpClient;
import io.vestige.resources.ChatResource;

import java.time.Duration;

/**
 * Main entry point for the Vestige SDK.
 *
 * <pre>{@code
 * VestigeClient client = VestigeClient.builder()
 *     .build();
 *
 * ChatResponse response = client.chat().sendMessage("Hello!");
 * System.out.println(response.getContent());
 * }</pre>
 *
 * This class is thread-safe. A single instance can be shared across multiple threads.
 */
public final class VestigeClient {

    private final VestigeConfig config;
    private final VestigeHttpClient httpClient;

    private volatile ChatResource chatResource;

    private VestigeClient(VestigeConfig config) {
        this.config = config;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.httpClient = new VestigeHttpClient(config, objectMapper);
    }

    /**
     * Access the Chat resource.
     */
    public ChatResource chat() {
        if (chatResource == null) {
            synchronized (this) {
                if (chatResource == null) {
                    chatResource = new ChatResource(httpClient);
                }
            }
        }
        return chatResource;
    }

    /**
     * Returns the configuration used by this client.
     */
    public VestigeConfig getConfig() {
        return config;
    }

    /**
     * Create a new builder for constructing a {@link VestigeClient}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link VestigeClient}.
     */
    public static final class Builder {
        private final VestigeConfig.Builder configBuilder = VestigeConfig.builder();

        private Builder() {
        }

        public Builder apiKey(String apiKey) {
            configBuilder.apiKey(apiKey);
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        public Builder timeout(Duration timeout) {
            configBuilder.timeout(timeout);
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            configBuilder.maxRetries(maxRetries);
            return this;
        }

        public Builder proxy(String host, int port) {
            configBuilder.proxy(host, port);
            return this;
        }

        public Builder debugMode(boolean debugMode) {
            configBuilder.debugMode(debugMode);
            return this;
        }

        public VestigeClient build() {
            return new VestigeClient(configBuilder.build());
        }
    }
}

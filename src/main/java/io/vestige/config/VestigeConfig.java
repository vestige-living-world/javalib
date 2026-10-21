package io.vestige.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

/**
 * Immutable configuration for the Vestige SDK client.
 * Use {@link VestigeConfig.Builder} to construct instances.
 */
public final class VestigeConfig {

    private static final String DEFAULT_BASE_URL = "https://api.mysticcraft.io";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final int DEFAULT_MAX_RETRIES = 3;

    private final String apiKey;
    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final Proxy proxy;
    private final boolean debugMode;

    private VestigeConfig(Builder builder) {
        this.apiKey = builder.apiKey;
        this.baseUrl = builder.baseUrl != null ? builder.baseUrl : DEFAULT_BASE_URL;
        this.timeout = builder.timeout != null ? builder.timeout : DEFAULT_TIMEOUT;
        this.maxRetries = builder.maxRetries > 0 ? builder.maxRetries : DEFAULT_MAX_RETRIES;
        this.proxy = builder.proxy;
        this.debugMode = builder.debugMode;
    }

    public String getApiKey() { return apiKey; }
    public String getBaseUrl() { return baseUrl; }
    public Duration getTimeout() { return timeout; }
    public int getMaxRetries() { return maxRetries; }
    public Proxy getProxy() { return proxy; }
    public boolean isDebugMode() { return debugMode; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String apiKey;
        private String baseUrl;
        private Duration timeout;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private Proxy proxy;
        private boolean debugMode;

        private Builder() {
        }

        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }
        public Builder maxRetries(int maxRetries) { this.maxRetries = maxRetries; return this; }

        public Builder proxy(String host, int port) {
            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            return this;
        }

        public Builder proxy(Proxy proxy) { this.proxy = proxy; return this; }
        public Builder debugMode(boolean debugMode) { this.debugMode = debugMode; return this; }

        public VestigeConfig build() {
            return new VestigeConfig(this);
        }
    }
}

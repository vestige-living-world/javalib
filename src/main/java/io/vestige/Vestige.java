package io.vestige;

import io.vestige.client.VestigeClient;

/**
 * Convenience facade for quickly creating a {@link VestigeClient}.
 *
 * <pre>{@code
 * import io.vestige.*;
 *
 * VestigeClient client = Vestige.client();
 * }</pre>
 */
public final class Vestige {

    private Vestige() {
    }

    /**
     * Create a client with default settings (no auth required).
     */
    public static VestigeClient client() {
        return VestigeClient.builder().build();
    }

    /**
     * Create a client with a custom base URL.
     */
    public static VestigeClient client(String baseUrl) {
        return VestigeClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Returns a new builder for advanced configuration.
     */
    public static VestigeClient.Builder builder() {
        return VestigeClient.builder();
    }
}

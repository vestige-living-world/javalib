package io.mysticcraft.vestige;

/** @deprecated Use {@link io.vestige.internal.VestigeLoader} */
@Deprecated
public final class Vestige {
    private Vestige() {}

    @Deprecated
    public static String getLastResponse() {
        return io.vestige.internal.VestigeLoader.getLastResponse();
    }

    @Deprecated
    public static void ready() {
        io.vestige.internal.VestigeLoader.ready();
    }
}

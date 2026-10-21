package io.mysticcraft.mystvale;

/** @deprecated Use {@link io.mysticcraft.vestige.Vestige} */
@Deprecated
public final class MystVale {

    private MystVale() {
    }

    @Deprecated
    public static String getLastResponse() {
        return io.mysticcraft.vestige.Vestige.getLastResponse();
    }

    @Deprecated
    public static void ready() {
        io.mysticcraft.vestige.Vestige.ready();
    }
}

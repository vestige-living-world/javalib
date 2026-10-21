import io.vestige.internal.VestigeLoader;

public class TestMystVale {
    public static void main(String[] args) {
        System.out.println("=== Testing Vestige ===");
        VestigeLoader.ready();
        System.out.println("Response: " + VestigeLoader.getLastResponse());
        System.out.println("=== Done ===");
    }
}

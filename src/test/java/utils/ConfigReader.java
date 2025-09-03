package utils;

import java.io.InputStream;
import java.util.Properties;

// Loads config.properties from classpath
public class ConfigReader {
    private static final Properties props = new Properties(); // Shared properties store

    static {
        try (InputStream is =
                     Thread.currentThread().getContextClassLoader()
                             .getResourceAsStream("config.properties")) { // Read resource
            if (is == null) throw new IllegalStateException("config.properties not found on classpath"); // Missing file
            props.load(is); // Parse properties
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e); // Bootstrap failure
        }
    }

    public static String get(String key) { return props.getProperty(key); } // Fetch by key
}

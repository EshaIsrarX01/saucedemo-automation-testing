package com.esha.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads settings from config.properties. A -Dkey=value passed on the command line wins,
 * so CI can run: mvn test -Dheadless=true
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String override = System.getProperty(key);
        return override != null ? override : PROPS.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key).trim());
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key).trim());
    }
}

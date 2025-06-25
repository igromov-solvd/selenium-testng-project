package com.solvd.selenium.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for loading and accessing test configuration properties
 */
public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "config.properties";

    private static ConfigManager instance;
    private final Properties properties;

    // Private constructor for singleton pattern
    private ConfigManager() {
        this.properties = new Properties();
        loadConfiguration();
    }

    /**
     * Get singleton instance of ConfigManager
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * Load configuration from config.properties file
     */
    private void loadConfiguration() {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Configuration file '" + CONFIG_FILE + "' not found in classpath");
            }
            properties.load(input);
            logger.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file: " + e.getMessage(), e);
        }
    }

    /**
     * Get configuration property value
     * 
     * @param key Property key
     * @return Property value
     * @throws RuntimeException if property is not found or empty
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Configuration property '" + key + "' is not defined");
        }
        return value.trim();
    }

    /**
     * Get configuration property as integer
     * 
     * @param key Property key
     * @return Property value as integer
     * @throws RuntimeException if property is not found or not a valid integer
     */
    public int getIntProperty(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Configuration property '" + key + "' must be a valid integer", e);
        }
    }

    /**
     * Get configuration property as boolean
     * 
     * @param key Property key
     * @return Property value as boolean
     * @throws RuntimeException if property is not found or not 'true'/'false'
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key).toLowerCase();
        if (!value.equals("true") && !value.equals("false")) {
            throw new RuntimeException("Configuration property '" + key + "' must be 'true' or 'false'");
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Check if property exists and is not empty
     * 
     * @param key Property key
     * @return true if property exists and is not empty
     */
    public boolean hasProperty(String key) {
        String value = properties.getProperty(key);
        return value != null && !value.trim().isEmpty();
    }

    // Specific getter methods for commonly used properties

    public int getExplicitWaitSeconds() {
        return getIntProperty("explicit.wait.seconds");
    }

    public int getImplicitWaitSeconds() {
        return getIntProperty("implicit.wait.seconds");
    }

    public int getPageLoadTimeoutSeconds() {
        return getIntProperty("page.load.timeout.seconds");
    }

    public String getScreenshotDirectory() {
        return getProperty("screenshot.directory");
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getSeleniumHubUrl() {
        return getProperty("selenium.hub.url");
    }

    public String getDefaultBrowser() {
        return getProperty("default.browser");
    }

    public boolean isRemoteExecutionEnabled() {
        return getBooleanProperty("remote.execution.enabled");
    }

    public int getWindowWidth() {
        return getIntProperty("window.width");
    }

    public int getWindowHeight() {
        return getIntProperty("window.height");
    }

    public boolean isScreenshotOnFailureEnabled() {
        return getBooleanProperty("screenshot.on.failure");
    }

    /**
     * Get all properties (for debugging purposes)
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }

    /**
     * Print all configuration properties (for debugging)
     */
    public void printConfiguration() {
        logger.info("Current configuration:");
        properties.forEach((key, value) -> logger.info("  {} = {}", key, value));
    }
}
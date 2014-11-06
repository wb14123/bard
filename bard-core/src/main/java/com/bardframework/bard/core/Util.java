package com.bardframework.bard.core;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {
    private static final Logger logger = LogManager.getLogger("com.bardframework.bard");
    private static CompositeConfiguration config;

    /**
     * Get the Bard config object. The properties is set in "bard.properties".
     *
     * @return The config object.
     */
    public static CompositeConfiguration getConfig() {
        if (config == null) {
            config = new CompositeConfiguration();
            String configFile = "bard.properties";

            if (Util.class.getClassLoader().getResource(configFile) == null) {
                return config;
            }

            try {
                config.addConfiguration(new PropertiesConfiguration("bard.properties"));
            } catch (ConfigurationException e) {
                logger.error("Load Bard configuration \"bard.properties\" error: {}", e);
            }
        }
        return config;
    }

    /**
     * Get the Bard logger. The logger uses log4j2 and its name is "com.bardframework.bard"
     *
     * @return The Bard logger.
     */
    public static Logger getLogger() {
        return logger;
    }
}

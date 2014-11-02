package com.bardframework.bard.core;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {
    private static final Logger logger = LogManager.getLogger("org.binwang.bard");
    private static CompositeConfiguration config;

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

    public static Logger getLogger() {
        return logger;
    }
}

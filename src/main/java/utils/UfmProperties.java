package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.NotFoundException;


public class UfmProperties {
    private static Logger logger = LogManager.getLogger(UfmProperties.class.getSimpleName());
    private static final String Easwaaq_TestPropertiesFileName = "easwaaq_test.properties";
    private static final String Semeta_TestPropertiesFileName = "semena_test.properties";
    private static File easwaaq_testPropertiesFile = null;
    private static File semena_testPropertiesFile = null;

    private static void checkEaswaaq_TestPropertiesFileLocated() {
        if (easwaaq_testPropertiesFile == null) {
            URL mainPropFileURL = UfmProperties.class.getClassLoader().getResource(Easwaaq_TestPropertiesFileName);
            if (mainPropFileURL == null) {
                String errorStr = "Cannot get resource: " + getFullPathForResource(Easwaaq_TestPropertiesFileName);
                logger.error(errorStr);
                throw new NotFoundException(errorStr);
            }
            try {
                easwaaq_testPropertiesFile = new File(mainPropFileURL.getFile());
            } catch (Exception e) {
                String errorStr = "Can't load " + getFullPathForResource(Easwaaq_TestPropertiesFileName) + " config file.";
                logger.error(errorStr);
                e.printStackTrace();
                throw new InvalidArgumentException(errorStr);
            }
            logger.debug("Easwaaq_test config file found: " + easwaaq_testPropertiesFile.getAbsolutePath());
        }
    }
    private static void checkSemenaPropertiesFileLocated() {
        if (semena_testPropertiesFile == null) {
            URL corePropFileURL = UfmProperties.class.getClassLoader().getResource(Semeta_TestPropertiesFileName);
            if (corePropFileURL == null) {
                String errorStr = "Cannot get resource: " + getFullPathForResource(Semeta_TestPropertiesFileName);
                logger.error(errorStr);
                throw new NotFoundException(errorStr);
            }
            try {
                semena_testPropertiesFile = new File(corePropFileURL.getFile());
            } catch (Exception e) {
                String errorStr = "Can't load " + getFullPathForResource(Semeta_TestPropertiesFileName) + " config file.";
                logger.error(errorStr);
                e.printStackTrace();
                throw new InvalidArgumentException(errorStr);
            }
            logger.debug("Semena_test config file found: " + semena_testPropertiesFile.getAbsolutePath());
        }
    }

    public static String getEaswaaq_TestProperty(String property) {
        checkEaswaaq_TestPropertiesFileLocated();

        java.util.Properties props = new java.util.Properties();
        try {
            FileInputStream is = new FileInputStream(easwaaq_testPropertiesFile);
            props.load(is);
            String result = props.getProperty(property);
            if (result == null)
                throw new InvalidArgumentException("");
            return result;
        } catch (Exception e) {
            String errorStr = "Can't load property '" + property + "' from " + getFullPathForResource(Easwaaq_TestPropertiesFileName) + " config file.";
            logger.error(errorStr);
            e.printStackTrace();
            throw new InvalidArgumentException(errorStr);
        }
    }

    public static String getSemena_TestProperty(String property) {
        checkSemenaPropertiesFileLocated();

        java.util.Properties props = new java.util.Properties();
        try {
            FileInputStream is = new FileInputStream(semena_testPropertiesFile);
            props.load(is);
            String result = props.getProperty(property);
            if (result == null)
                throw new InvalidArgumentException("");
            return result;
        } catch (Exception e) {
            String errorStr = "Can't load property '" + property + "' from " + getFullPathForResource(Semeta_TestPropertiesFileName) + " config file.";
            logger.error(errorStr);
            e.printStackTrace();
            throw new InvalidArgumentException(errorStr);
        }
    }

    public static String getFullPathForResource(String resource) {
        return (UfmProperties.class.getClassLoader().getResource(".") + resource);
    }
}

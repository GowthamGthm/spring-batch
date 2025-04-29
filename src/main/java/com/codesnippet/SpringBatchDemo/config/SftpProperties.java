package com.codesnippet.SpringBatchDemo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@PropertySource("classpath:${CONFIG_ENV}/file.properties")
public class SftpProperties {

    @Value("${CONFIG_ENV}")
    private String env;

    public final Properties properties = new Properties();

    @PostConstruct
    public void loadProperties() {
        String path = env + "/file.properties";
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String jobName, String key) {
        return properties.getProperty(jobName + "." + key);
    }

    public Properties getProperties() {
        return properties;
    }

}
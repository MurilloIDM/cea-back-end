package com.cea.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class ConfigProperties {

    private final Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }
}

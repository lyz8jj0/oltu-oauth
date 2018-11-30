package com.lxy.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    public static String webVersion;

    @Value("${webVersion}")
    public void setWebVersion(String webVersion) {
        Config.webVersion = webVersion;
    }


}

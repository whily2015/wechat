package com.hyde.wmis.util;

import org.springframework.core.env.Environment;

import javax.annotation.Resource;

public class CacheUtil {

    @Resource
    private static Environment env;

    public static String getSysConfig(String key) {
        return env.getProperty(key);
    }
}

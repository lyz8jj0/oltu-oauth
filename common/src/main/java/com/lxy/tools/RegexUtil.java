package com.lxy.tools;

import java.util.regex.Pattern;

/**
 * 请求地址验证工具类
 */
public class RegexUtil {

    /**
     * 验证重定向的url是否合法
     *
     * @param redirectURI 重定向URI
     * @return true/false
     */
    public static boolean isUrl(String redirectURI) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, redirectURI);
    }

}

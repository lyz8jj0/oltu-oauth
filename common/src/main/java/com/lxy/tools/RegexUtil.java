package com.lxy.tools;

import java.util.regex.Pattern;

/**
 * 请求地址验证工具类
 */
public class RegexUtil {

    /**
     * 验证重定向的url是否合法
     * (在你用字符串String设置完一条正则表达式之后，通过Pattern.compile转化为正则表达式。
     * 之后再用pattern.matcher(xx).matches()的方法，得到是否匹配的布尔值。)
     *
     * @param redirectURI 重定向URI
     * @return true/false
     */
    public static boolean isUrl(String redirectURI) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(redirectURI).matches();
    }
}

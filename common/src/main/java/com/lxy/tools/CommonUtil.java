package com.lxy.tools;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 公共工具类
 */
public class CommonUtil {

    /**
     * 获取授权码(code)时 验证clientID 是否正确
     *
     * @param oauthRequest OAuth请求
     * @return boolean
     */
    public static boolean validateOAuth2ClientId(OAuthAuthzRequest oauthRequest) {
        //客户端clientId
        ArrayList clientIdList = new ArrayList();
        clientIdList.add("fbed1d1b4b1449daa4bc49397cbe2350");
        clientIdList.add("a85b033590714fafb20db1d11aed5497");
        clientIdList.add("d23e06a97e2d4887b504d2c6fdf42c0b");
        return clientIdList.contains(oauthRequest.getClientId());
    }

    /**
     * 获取授权令牌(token)时 验证clientID 是否正确
     *
     * @param oauthRequest OAuth请求
     * @return boolean
     */
    public static boolean validateOAuth2ClientId(OAuthTokenRequest oauthRequest) {
        //客户端clientId
        ArrayList clientIdList = new ArrayList();
        clientIdList.add("fbed1d1b4b1449daa4bc49397cbe2350");
        clientIdList.add("a85b033590714fafb20db1d11aed5497");
        clientIdList.add("d23e06a97e2d4887b504d2c6fdf42c0b");
        return clientIdList.contains(oauthRequest.getClientId());
    }

    /**
     * 验证clientSecret 是否正确
     *
     * @param oauthRequest OAuth请求
     * @return
     */
    public static boolean validateOAuth2ClientSecret(OAuthTokenRequest oauthRequest) {
        //客户端AppSecret
        ArrayList clientSecretList = new ArrayList();
        clientSecretList.add("fbed1d1b4b1449daa4bc49397cbe2350");
        clientSecretList.add("a85b033590714fafb20db1d11aed5497");
        clientSecretList.add("d23e06a97e2d4887b504d2c6fdf42c0b");
        return clientSecretList.contains(oauthRequest.getClientSecret());
    }

    //设置code过期时间(10分钟)
    public static String codeExpires() {
        Date dt = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MINUTE, 10);
        return Long.toString(rightNow.getTimeInMillis());
    }

    //设置token过期时间(24小时)
    public static String tokenExpires() {
        Date dt = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.HOUR, 24);//token 过期时间设为24小时
        return Long.toString(rightNow.getTimeInMillis());

    }
}

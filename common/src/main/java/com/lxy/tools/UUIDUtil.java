package com.lxy.tools;

import java.util.UUID;

/**
 * uuid 用于主键id
 */
public class UUIDUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        String uuid = UUIDUtil.getUUID();
        System.out.println(uuid);
    }
}
package com.lxy.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5Util() {
    }


    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    //转换字节数组为16进制字符串
    private static String byteToStirng(byte[] bByte) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            stringBuffer.append(byteToArrayString(bByte[i]));
        }
        return stringBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            //返回实现指定摘要算法的 MessageDigest 对象.用于MD5加密的
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            //md5.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToStirng(md5.digest(strObj.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultString;
    }

}

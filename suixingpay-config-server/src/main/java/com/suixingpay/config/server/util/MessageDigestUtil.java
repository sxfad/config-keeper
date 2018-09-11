package com.suixingpay.config.server.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:09:21
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:09:21
 */
public class MessageDigestUtil {

    public static String getMessageDigest(byte[] buffer, String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance(key);
            digest.reset();
            digest.update(buffer);
            return bytes2Hex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    public static String getMD5(byte[] buffer) {
        return getMessageDigest(buffer, "MD5");
    }

    public static String getMD5(String str) {
        try {
            return getMD5(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String getSHA1(byte[] buffer) {
        return getMessageDigest(buffer, "SHA-1");
    }

    public static String getSHA1(String str) {
        try {
            return getSHA1(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSHA256(byte[] buffer) {
        return getMessageDigest(buffer, "SHA-256");
    }

    public static String getSHA256(String str) {
        try {
            return getSHA256(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
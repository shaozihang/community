package com.tree.community.util;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5Utils {
    /**
     * md5加密
     * @param str 明文
     * @param key 密钥
     */
    public static String md5(String str,String key) {
        return new Md5Hash(str,key).toString();
    }
}

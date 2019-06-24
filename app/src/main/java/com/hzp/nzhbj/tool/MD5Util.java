package com.hzp.nzhbj.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * created by hzp on 2019/6/24 14:52
 * 作者：codehan
 * 描述：md5加密工具类
 */
public class MD5Util {
    /**
     * MD5加密算法
     * @param message
     * @return
     */
    public static String Md5(String message){
        StringBuilder sb = new StringBuilder();
        try {
            //algorithm: 加密的方式:MD5
            MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
            //1.将数据转化成byte数组，并对byte数组进行第一次加密,返回的就是加密过的byte数组
            byte[] digest = messageDigest.digest( message.getBytes() );
            for (int i = 0; i < digest.length; i++) {
                //2.将加密过的byte数组的元素和255进行与运算
                //-128 - 127
                int result = digest[i] & 0xff;
                //因为得到int类型的值，可能会比较大，将int类型的值转化成十六进制的字符串
                String hexString = Integer.toHexString( result );
                if(hexString.length()<2){
                    sb.append( 0 );
                }
                sb.append( hexString );
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            //找不到算法的异常
            e.printStackTrace();
        }
        return null;
    }
}

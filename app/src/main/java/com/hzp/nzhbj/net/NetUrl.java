package com.hzp.nzhbj.net;

/**
 * created by hzp on 2019/6/17 23:39
 * 作者：codehan
 * 描述：请求服务器的路径
 */
public class NetUrl {
    //服务器ip
//    public static String SERVERURL = "http://10.0.2.2:8080/zhbj";
//    public static String SERVERURL = "http://192.168.2.176:8080/zhbj";
    public static String SERVERURL = "http://192.168.43.41:8080/zhbj";
    //新闻中心
    /*http://192.168.43.41:8080/zhbj/categories.json*/
    public static final String NEWSCENTERURL=SERVERURL+"/categories.json";
    //组图
    public static final String PHOTOURL=SERVERURL+"/photos/photos_1.json";
}


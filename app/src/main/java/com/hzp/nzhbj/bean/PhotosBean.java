package com.hzp.nzhbj.bean;

import java.util.List;

/**
 * created by hzp on 2019/6/24 14:16
 * 作者：codehan
 * 描述：
 */
public class PhotosBean {
    public String retcode;
    public Photos data;
    public class Photos {

        public String countcommenturl;
        public String more;
        //显示图片对应的集
        public List<PhotosItem> news;

        public String title;
        public List topic;
    }

    public class PhotosItem {

        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String largeimage;
        //图片链接地址
        public String listimage;
        public String pubdate;
        public String smallimage;
        //图片的配套文
        public String title;
        public String type;
        public String url;

    }

}

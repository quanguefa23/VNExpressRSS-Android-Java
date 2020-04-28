package com.example.recyclerview;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RSSContract {
    public static final String RSS_BASE_URL = "https://vnexpress.net/rss";
    public static final Map<String, String> URLS;
    public static final Map<String, String> TITLES;

    static {
        URLS = new HashMap<>();
        URLS.put("trangchu", "https://vnexpress.net/rss/tin-moi-nhat.rss");
        URLS.put("thoisu", "https://vnexpress.net/rss/thoi-su.rss");
        URLS.put("thegioi", "https://vnexpress.net/rss/the-gioi.rss");
        URLS.put("thethao", "https://vnexpress.net/rss/the-thao.rss");
        URLS.put("giaoduc", "https://vnexpress.net/rss/giao-duc.rss");
        URLS.put("giaitri", "https://vnexpress.net/rss/giai-tri.rss");

        TITLES = new HashMap<>();
        TITLES.put("trangchu", "Trang chủ");
        TITLES.put("thoisu", "Thời sự");
        TITLES.put("thegioi", "Thế giới");
        TITLES.put("thethao", "Thể thao");
        TITLES.put("giaoduc", "Giáo dục");
        TITLES.put("giaitri", "Giải trí");
    }
}
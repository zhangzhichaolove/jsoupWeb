package com.chao.jsoup.bean;

import java.util.List;

/**
 * Created by 003 on 2018/3/30.
 */
public class Image {
    private java.util.List<String> big;
    private java.util.List<String> download_url;

    public java.util.List<String> getBig() {
        return big;
    }

    public void setBig(List<String> big) {
        this.big = big;
    }

    public List<String> getDownload_url() {
        return download_url;
    }

    public void setDownload_url(List<String> download_url) {
        this.download_url = download_url;
    }
}

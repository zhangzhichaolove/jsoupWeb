package com.chao.jsoup.bean;

import java.util.List;

/**
 * Created by 003 on 2018/3/30.
 */
public class Gif {
    private java.util.List<String> images;
    private java.util.List<String> gif_thumbnail;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getGif_thumbnail() {
        return gif_thumbnail;
    }

    public void setGif_thumbnail(List<String> gif_thumbnail) {
        this.gif_thumbnail = gif_thumbnail;
    }
}

package com.chao.jsoup.model;

import java.util.List;

/**
 * Created by Chao on 2017/10/13.
 */
public class BuDeJieModel {
    private BuDeJieInfo info;
    private List<BuDeJieContent> list;


    public BuDeJieInfo getInfo() {
        return info;
    }

    public void setInfo(BuDeJieInfo info) {
        this.info = info;
    }

    public List<BuDeJieContent> getList() {
        return list;
    }

    public void setList(List<BuDeJieContent> list) {
        this.list = list;
    }
}

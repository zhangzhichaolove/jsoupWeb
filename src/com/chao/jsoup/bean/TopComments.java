package com.chao.jsoup.bean;

/**
 * Created by 003 on 2018/3/30.
 */
public class TopComments {

    private String cmt_type;
    private String content;
    private U u;

    public String getCmt_type() {
        return cmt_type;
    }

    public void setCmt_type(String cmt_type) {
        this.cmt_type = cmt_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public U getU() {
        return u;
    }

    public void setU(U u) {
        this.u = u;
    }
}

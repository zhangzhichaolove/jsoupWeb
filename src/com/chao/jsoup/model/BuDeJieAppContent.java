package com.chao.jsoup.model;

import java.util.Date;

/**
 * Created by Chao on 2017/10/13.
 */
public class BuDeJieAppContent {


    private transient Integer id;
    private String type;//gif
    private String text;//出租车司机不说都是老司机吗？
    private String username = "";//风雨同舟
    private String uid = "";//风雨同舟
    private String header = "";//http://wimg.spriteapp.cn/profile/large/2017/12/14/5a3243aa2a150_mini.jpg
    private Integer comment;//305
    private String top_commentsVoiceuri;
    private String top_commentsContent;//text
    private String top_commentsHeader;//http://qzapp.qlogo.cn/qzapp/100336987/1A279B2621FC54E35217FDB6C43C9291/100
    private String top_commentsName;//InstMR
    private String top_comments_t_Voiceuri;//text
    private String top_comments_t_Content;//text
    private String top_comments_t_Header;//http://qzapp.qlogo.cn/qzapp/100336987/1A279B2621FC54E35217FDB6C43C9291/100
    private String top_comments_t_Name;//InstMR
    private Date passtime;//2017-10-13 05:18:42
    private Integer soureid;//26555457
    private Integer up = 0;//498
    private Integer down = 0;//91
    private Integer forward = 0;//8
    private String image;//http://wimg.spriteapp.cn/ugc/2018/01/19/5a613b061ad44_1.jpg
    private String gif;//http://wimg.spriteapp.cn/ugc/2017/10/10/59dc2b9570590.gif
    private String thumbnail;//http://wimg.spriteapp.cn/picture/2017/1223/27032640_319.jpg
    private String video;//http://wvideo.spriteapp.cn/video/2017/1223/86debd6ae78d11e7ad0c842b2b4c75ab_wpd.mp4

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public Integer getForward() {
        return forward;
    }

    public void setForward(Integer forward) {
        this.forward = forward;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTop_commentsContent() {
        return top_commentsContent;
    }

    public void setTop_commentsContent(String top_commentsContent) {
        this.top_commentsContent = top_commentsContent;
    }

    public String getTop_comments_t_Content() {
        return top_comments_t_Content;
    }

    public void setTop_comments_t_Content(String top_comments_t_Content) {
        this.top_comments_t_Content = top_comments_t_Content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getTop_commentsHeader() {
        return top_commentsHeader;
    }

    public void setTop_commentsHeader(String top_commentsHeader) {
        this.top_commentsHeader = top_commentsHeader;
    }

    public String getTop_commentsName() {
        return top_commentsName;
    }

    public void setTop_commentsName(String top_commentsName) {
        this.top_commentsName = top_commentsName;
    }

    public String getTop_commentsVoiceuri() {
        return top_commentsVoiceuri;
    }

    public void setTop_commentsVoiceuri(String top_commentsVoiceuri) {
        this.top_commentsVoiceuri = top_commentsVoiceuri;
    }

    public String getTop_comments_t_Voiceuri() {
        return top_comments_t_Voiceuri;
    }

    public void setTop_comments_t_Voiceuri(String top_comments_t_Voiceuri) {
        this.top_comments_t_Voiceuri = top_comments_t_Voiceuri;
    }

    public String getTop_comments_t_Header() {
        return top_comments_t_Header;
    }

    public void setTop_comments_t_Header(String top_comments_t_Header) {
        this.top_comments_t_Header = top_comments_t_Header;
    }

    public String getTop_comments_t_Name() {
        return top_comments_t_Name;
    }

    public void setTop_comments_t_Name(String top_comments_t_Name) {
        this.top_comments_t_Name = top_comments_t_Name;
    }

    public Date getPasstime() {
        return passtime;
    }

    public void setPasstime(Date passtime) {
        this.passtime = passtime;
    }

    public Integer getSoureid() {
        return soureid;
    }

    public void setSoureid(Integer soureid) {
        this.soureid = soureid;
    }
}

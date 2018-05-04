package com.chao.jsoup.request;

import com.chao.jsoup.bean.BuDeJieAppBean;
import com.chao.jsoup.bean.BuDeJieAppList;
import com.chao.jsoup.model.BuDeJieAppContent;
import com.chao.jsoup.model.RequestCount;
import com.chao.jsoup.util.*;
import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 003 on 2018/3/30.
 */
public class UpdateBaiSiBuDeJieAppData {
    private static UpdateBaiSiBuDeJieAppData instance;
    private Long addCount = 0L;//新增统计
    private int continuityRepeat = 0;//连续重复统计
    private int maxContinuityRepeat = 50;//最大连续重复限制
    private Gson gson = GsonUtils.getGson();
    private String random = "http://d.api.budejie.com/topic/recommend/budejie-android-6.9.2/0-20.json";
    private String normal = "http://c.api.budejie.com/topic/list/jingxuan/1/budejie-android-6.9.2/0-20.json";

    public static UpdateBaiSiBuDeJieAppData getInstance() {
        if (instance == null) {
            synchronized (UpdateBaiSiBuDeJieAppData.class) {
                if (instance == null) {
                    instance = new UpdateBaiSiBuDeJieAppData();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        HibernateUtils.openSession();
        getInstance().start();
    }

    public void start(Integer maxCount) {
        maxContinuityRepeat = maxCount;
        start();
    }

    public void start() {
        continuityRepeat = 0;
        ExecutorServiceUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    addCount = 0L;
                    System.out.println("UpdateBaiSiBuDeJieAppData执行，当前时间" + TimeUtils.getFormatter().format(Calendar.getInstance().getTime()));
                    try {
                        saveBaiSiBuDeJieApi(random);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("UpdateBaiSiBuDeJieAppData执行结束，当前时间" + TimeUtils.getFormatter().format(Calendar.getInstance().getTime()) + "此次新增数据：" + addCount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("-------------UpdateBaiSiBuDeJieAppData任务运行出现异常--------------");
                }
            }
        });
    }


    public void saveBaiSiBuDeJieApi(String url) {
        //System.out.println("当前加载页码：" + page);
        //String json = HttpTool.doGet(url);
        String json = null;
        try {
            json = OKHttpUtils.getWeb(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isNullOrEmpty(json)) {
            return;
        }
        //System.out.println(json);
        if (!JsonRegexUtil.isJson(json)) {
            System.out.println("JSON格式异常" + json);
            return;
        }
        Session session = HibernateUtils.openSession();
        BuDeJieAppBean model = gson.fromJson(json, BuDeJieAppBean.class);
        if (model != null) {
            int identical = 0;//完全相同计数
            for (int i = 0; model.getList() != null && i < model.getList().size(); i++) {
                BuDeJieAppList bean = model.getList().get(i);
                BuDeJieAppContent content = new BuDeJieAppContent();
                //System.out.println(bean.getText());
                content.setType(bean.getType());
                content.setText(bean.getText());
                if (bean.getU() != null/*StringUtils.isNullOrEmpty()*/) {
                    content.setUsername(bean.getU().getName());
                    content.setHeader(bean.getU().getHeader().get(0));
                    content.setUid(bean.getU().getUid());
                }
                content.setComment(Integer.valueOf(bean.getComment()));
                if (bean.getTop_comments() != null && bean.getTop_comments().size() > 0) {
                    content.setTop_commentsContent(bean.getTop_comments().get(0).getContent());
                    content.setTop_commentsVoiceuri(bean.getTop_comments().get(0).getU().getVoiceuri());
                    content.setTop_commentsName(bean.getTop_comments().get(0).getU().getName());
                    content.setTop_commentsHeader(bean.getTop_comments().get(0).getU().getHeader().get(0));
                }
                content.setPasstime(bean.getPasstime());
                content.setSoureid(bean.getId());
                content.setUp(Integer.valueOf(bean.getUp()));
                content.setDown(bean.getDown());
                content.setForward(bean.getForward());
                switch (bean.getType()) {
                    case "text":
                        break;
                    case "gif":
                        content.setGif(bean.getGif().getImages().get(0));
                        content.setThumbnail(bean.getGif().getGif_thumbnail().get(0));
                        break;
                    case "image":
                        content.setImage(bean.getImage().getBig().get(0));
                        content.setThumbnail(bean.getImage().getBig().get(0));
                        break;
                    case "video":
                        content.setVideo(bean.getVideo().getVideo().get(0));
                        content.setThumbnail(bean.getVideo().getThumbnail().get(0));
                        break;
                }
                Criteria criteria = session.createCriteria(BuDeJieAppContent.class);
                criteria.add(Restrictions.eq("text", content.getText()));
                BuDeJieAppContent buDeJieContent = (BuDeJieAppContent) criteria.uniqueResult();
                if (buDeJieContent == null) {//没有重复
                    Transaction transaction = session.beginTransaction();
                    //content.setId(null);
                    session.save(content);
                    transaction.commit();
                    addCount = addCount + 1;
                    continuityRepeat = 0;
                } else {
                    identical = identical + 1;//统计重复数量
                }
            }
            if (model.getList() == null || identical >= model.getList().size()) {//此次未新增任何内容，完全重复。
                continuityRepeat = continuityRepeat + 1;
            }
            session.close();
            if (continuityRepeat >= maxContinuityRepeat) {//此次未新增任何内容，完全重复。
                System.out.println("重复数据超过限制，今日任务停止！此次新增数据：" + addCount);
                saveCount();
                return;
            } else {
                if (url.startsWith("http://c.api.budejie.com")) {//如果已经切换到正常接口则直接递归
                    int last = url.lastIndexOf("/") + 1;
                    url = url.replaceAll(url.substring(last), model.getInfo().getNp() + "-20.json");
                    saveBaiSiBuDeJieApi(url);
                    return;
                }
                if (continuityRepeat >= maxContinuityRepeat / 2) {
                    saveBaiSiBuDeJieApi(normal);
                } else {
                    saveBaiSiBuDeJieApi(url);
                }
            }
        } else {
            saveCount();
            System.out.println("接口数据异常，停止任务！此次新增数据：" + addCount);
        }
    }

    private void saveCount() {
        continuityRepeat = 0;
        maxContinuityRepeat = 50;
        RequestCount budejie = TableUtils.findCreateTable("budejie_app");
        Session session = HibernateUtils.openSession();
        budejie.setDataCount(TableUtils.findTableCount("budejie_app"));
        budejie.setLastCount(addCount);
        budejie.setLastUpdateTime(new Date());
        Transaction transaction = session.beginTransaction();
        session.update(budejie);
        transaction.commit();
        session.close();
    }

}

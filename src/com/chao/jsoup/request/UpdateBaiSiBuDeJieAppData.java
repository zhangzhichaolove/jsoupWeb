package com.chao.jsoup.request;

import com.chao.jsoup.HttpTool;
import com.chao.jsoup.bean.BuDeJieAppBean;
import com.chao.jsoup.bean.BuDeJieAppList;
import com.chao.jsoup.model.BuDeJieAppContent;
import com.chao.jsoup.model.RequestCount;
import com.chao.jsoup.util.ExecutorServiceUtils;
import com.chao.jsoup.util.GsonUtils;
import com.chao.jsoup.util.HibernateUtils;
import com.chao.jsoup.util.TableUtils;
import com.google.gson.Gson;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.Date;

/**
 * Created by 003 on 2018/3/30.
 */
public class UpdateBaiSiBuDeJieAppData {

    private static Long addCount = 0L;//新增统计
    private static int continuityRepeat = 0;//连续重复统计
    private static int maxContinuityRepeat = 20;//最大连续重复限制
    private static Gson gson = GsonUtils.getGson();
    public static boolean startRun = false;

    public static void main(String[] args) {
        HibernateUtils.openSession();
        startRun = true;
        //saveSatin(1);
        saveBaiSiBuDeJieApi();
    }

    public static void start(Integer maxCount) {
        maxContinuityRepeat = maxCount;
        start();
    }

    public static void start() {
        if (!startRun) {
            startRun = true;
            ExecutorServiceUtils.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    //saveSatin(1);
                    addCount = 0L;
                    saveBaiSiBuDeJieApi();
                }
            });
        }
    }

    public static void stop() {
        startRun = false;
    }


    public static void saveBaiSiBuDeJieApi() {
        //System.out.println("当前加载页码：" + page);
        String json = HttpTool.doGet("http://d.api.budejie.com/topic/recommend/budejie-android-6.9.2/0-20.json");
        //System.out.println(json);
        BuDeJieAppBean model = gson.fromJson(json, BuDeJieAppBean.class);
        if (model != null) {
            int identical = 0;//完全相同计数
            for (int i = 0; i < model.getList().size(); i++) {
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
                } else if (bean.getTop_comments() != null && bean.getTop_comments().size() > 1) {
                    content.setTop_comments_t_Content(bean.getTop_comments().get(1).getContent());
                    content.setTop_comments_t_Voiceuri(bean.getTop_comments().get(1).getU().getVoiceuri());
                    content.setTop_comments_t_Name(bean.getTop_comments().get(1).getU().getName());
                    content.setTop_comments_t_Header(bean.getTop_comments().get(1).getU().getHeader().get(0));
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
                Session session = HibernateUtils.openSession();
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
                session.close();
            }
            if (identical >= model.getList().size()) {//此次未新增任何内容，完全重复。
                continuityRepeat = continuityRepeat + 1;
            }
            if (continuityRepeat >= maxContinuityRepeat) {//此次未新增任何内容，完全重复。
                System.out.println("重复数据超过限制，今日任务停止！此次新增数据：" + addCount);
                startRun = false;
                saveCount();
                return;
            }
            if (startRun) {
                saveBaiSiBuDeJieApi();
            } else {
                System.out.println("服务停止！此次新增数据：" + addCount);
                saveCount();
                return;
            }
        } else {
            startRun = false;
            saveCount();
            System.out.println("接口数据异常，停止任务！此次新增数据：" + addCount);
        }
    }

    private static void saveCount() {
        maxContinuityRepeat = 20;
        RequestCount budejie = TableUtils.findCreateTable("budejie");
        Session session = HibernateUtils.openSession();
        budejie.setDataCount(TableUtils.findTableCount("budejie"));
        budejie.setLastCount(addCount);
        budejie.setLastUpdateTime(new Date());
        Transaction transaction = session.beginTransaction();
        session.update(budejie);
        transaction.commit();
        session.close();
    }

}

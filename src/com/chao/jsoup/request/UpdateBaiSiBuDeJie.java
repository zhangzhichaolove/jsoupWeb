package com.chao.jsoup.request;

import com.chao.jsoup.HttpTool;
import com.chao.jsoup.model.BuDeJieContent;
import com.chao.jsoup.model.BuDeJieModel;
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
 * Created by Chao on 2017/10/11.
 */
public class UpdateBaiSiBuDeJie {
    private static Long addCount = 0L;//新增统计
    private static int continuityRepeat = 0;//连续重复统计
    private static int maxContinuityRepeat = 20;//最大连续重复限制
    private static Gson gson = GsonUtils.getGson();
    public static boolean startRun = false;

    public static void main(String[] args) {
        HibernateUtils.openSession();
        startRun = true;
        //saveSatin(1);
        saveBaiSiBuDeJieApi(1, null);
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
                    saveBaiSiBuDeJieApi(1, null);
                }
            });
        }
    }

    public static void stop() {
        startRun = false;
    }


    public static void saveBaiSiBuDeJieApi(int page, String maxtime) {
        //System.out.println("当前加载页码：" + page);
        String json = HttpTool.doGet("http://api.budejie.com/api/api_open.php?a=list&c=data&type=1&page=" + page + "&maxtime=" + maxtime);
        //System.out.println(json);
        BuDeJieModel model = gson.fromJson(json, BuDeJieModel.class);
        if (model != null) {
            maxtime = model.getInfo().getMaxtime();
            int identical = 0;//完全相同计数
            for (int i = 0; i < model.getList().size(); i++) {
                BuDeJieContent content = model.getList().get(i);
                //System.out.println(content.getText());
                Session session = HibernateUtils.openSession();
                Criteria criteria = session.createCriteria(BuDeJieContent.class);
                criteria.add(Restrictions.eq("text", content.getText()));
                BuDeJieContent buDeJieContent = (BuDeJieContent) criteria.uniqueResult();
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
            page = page + 1;
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
                saveBaiSiBuDeJieApi(page, maxtime);
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

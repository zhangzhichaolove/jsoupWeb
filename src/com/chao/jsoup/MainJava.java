package com.chao.jsoup;

import com.chao.jsoup.model.BuDeJieContent;
import com.chao.jsoup.model.BuDeJieModel;
import com.chao.jsoup.model.SatinModel;
import com.chao.jsoup.util.ExecutorServiceUtils;
import com.chao.jsoup.util.HibernateUtils;
import com.google.gson.Gson;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Chao on 2017/10/11.
 */
public class MainJava {
    private static Gson gson = new Gson();
    public static boolean startRun = false;

    public static void main(String[] args) {
        HibernateUtils.openSession();
        //saveSatin(1);
        //saveBaiSiBuDeJieApi(1, null);
    }

    public static void start() {
        startRun = true;
        HibernateUtils.openSession();
        ExecutorServiceUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                saveSatin(1);
            }
        });
    }

    public static void stop() {
        startRun = false;
    }

    public static void saveSatin(int page) {
        try {
            String htmlStr = HttpTool.doGet("http://www.budejie.com/text/" + page);
            // 将获取的网页 HTML 源代码转化为 Document
            Document doc = Jsoup.parse(htmlStr);
            //Elements list = doc.getElementsByClass("j-list-user");
            Elements elements = doc.getElementsByClass("j-r-list-c-desc");
            //doc.getElementsByClass("j-r-list-c-desc").get(0).getElementsByTag("a").get(0).getAllElements().get(0).text();
            int identical = 0;
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                String text = element.getElementsByTag("a").get(0).getAllElements().get(0).text();
                Session session = HibernateUtils.openSession();
                Criteria criteria = session.createCriteria(SatinModel.class);
                criteria.add(Restrictions.eq("content", text));
                SatinModel stain = (SatinModel) criteria.uniqueResult();
                if (stain == null) {
                    Transaction transaction = session.beginTransaction();
                    SatinModel model = new SatinModel();
                    model.setId(null);
                    model.setContent(text);
                    session.save(model);
                    transaction.commit();
                } else {
                    identical = identical + 1;
                }
                session.close();
                System.out.println(text);
                if (identical >= elements.size()) {
                    System.out.println("重复数据超过限制，爬取其他接口。");
                    saveBaiSiBuDeJieApi(1, null);
                }
            }
            Elements title = doc.getElementsByTag("title");
            System.out.println(title.text());
            if (startRun) {
                saveSatin(page + 1);
            } else {
                System.out.println("服务停止!");
            }
        } catch (CommonException e) {
            e.printStackTrace();
            System.out.println("请求失败了");
            saveBaiSiBuDeJieApi(1, null);
        }
    }


    public static void saveBaiSiBuDeJieApi(int page, String maxtime) {
        System.out.println("当前加载页码：" + page);
        String json = HttpTool.doGet("http://api.budejie.com/api/api_open.php?a=list&c=data&type=1&page=" + page + "&maxtime=" + maxtime);
        BuDeJieModel model = gson.fromJson(json, BuDeJieModel.class);
        if (model != null) {
            maxtime = model.getInfo().getMaxtime();
            int identical = 0;
            for (int i = 0; i < model.getList().size(); i++) {
                BuDeJieContent content = model.getList().get(i);
                System.out.println(content.getText());
                Session session = HibernateUtils.openSession();
                Criteria criteria = session.createCriteria(BuDeJieContent.class);
                criteria.add(Restrictions.eq("text", content.getText()));
                BuDeJieContent buDeJieContent = (BuDeJieContent) criteria.uniqueResult();
                if (buDeJieContent == null) {//没有重复
                    Transaction transaction = session.beginTransaction();
                    //content.setId(null);
                    session.save(content);
                    transaction.commit();
                } else {
                    identical = identical + 1;
                }
                session.close();
            }
            page = page + 1;
            if (identical >= model.getList().size()) {
                System.out.println("重复数据超过限制，爬取其他接口。");
                saveSatin(1);
            }
            if (startRun) {
                saveBaiSiBuDeJieApi(page, maxtime);
            } else {
                System.out.println("服务停止!");
            }
        } else {
            System.out.println("保存完毕!");
            saveSatin(1);
        }

    }

}

package com.chao.jsoup.util;

import com.chao.jsoup.model.RequestCount;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Chao on 2018/3/28.
 */
public class TableUtils {

    public static void main(String[] args) {
        System.out.println(TableUtils.findTableCount("budejie"));
    }

    //NameIsExists
    public static RequestCount findCreateTable(String tableName) {
        Session session = HibernateUtils.openSession();
        Criteria criteria = session.createCriteria(RequestCount.class);
        criteria.add(Restrictions.eq("tableName", tableName));
        RequestCount requestCount = (RequestCount) criteria.uniqueResult();
        if (requestCount == null) {//没有就创建
            requestCount = new RequestCount();
            requestCount.setDataCount(0L);
            requestCount.setLastCount(0L);
            requestCount.setLastUpdateTime(new Date());
            requestCount.setTableName(tableName);
            Transaction transaction = session.beginTransaction();
            session.save(requestCount);
            transaction.commit();
        } //存在就获取
        session.close();
        return requestCount;
    }

    public static Long findTableCount(String tableName) {
        String sql = "select count(*) from " + tableName;
        Session session = HibernateUtils.openSession();
        BigInteger count = new BigInteger("0");
        try {
            Query query = session.createSQLQuery(sql);
            count = (BigInteger) query.uniqueResult();
        } catch (Exception e) {
            return 0L;
        }
        session.close();
        return count.longValue();
    }

}

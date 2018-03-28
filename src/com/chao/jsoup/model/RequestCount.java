package com.chao.jsoup.model;

import java.util.Date;

/**
 * Created by Chao on 2018/3/28.
 */
public class RequestCount {

    private transient Integer id;
    private String tableName;
    private Long lastCount;
    private Long dataCount;
    private Date lastUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getLastCount() {
        return lastCount;
    }

    public void setLastCount(Long lastCount) {
        this.lastCount = lastCount;
    }

    public Long getDataCount() {
        return dataCount;
    }

    public void setDataCount(Long dataCount) {
        this.dataCount = dataCount;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}

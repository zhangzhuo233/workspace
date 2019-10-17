package com.bd.sparkproject.domain;

/**
 * @program: smp-spark-project
 * @description: 按session随机抽取出的session
 * @author: Mr.zhang
 * @create: 2019-10-17 22:48
 **/
public class SessionRandomExtract {
    private long taskid;
    private String sessionid;
    private String startTime;
    private String searchKeywords;
    private String clickCategoryIds;

    public long getTaskid() {
        return taskid;
    }

    public void setTaskid(long taskid) {
        this.taskid = taskid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public String getClickCategoryIds() {
        return clickCategoryIds;
    }

    public void setClickCategoryIds(String clickCategoryIds) {
        this.clickCategoryIds = clickCategoryIds;
    }
}

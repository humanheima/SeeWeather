package com.humanheima.litepaldemo;

import org.litepal.crud.DataSupport;

public class Comment extends DataSupport {

    private int id;

    private String content;
    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
    // 自动生成get、set方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
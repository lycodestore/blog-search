package com.ly.blogsearch.domain;

import java.util.List;

public class QueryResult {
    private Integer total;

    private String relation;

    private List<Article> articles;

    public QueryResult(Integer total, String relation, List<Article> articles) {
        this.total = total;
        this.relation = relation;
        this.articles = articles;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}

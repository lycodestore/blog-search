package com.ly.blogsearch.service;

import com.ly.blogsearch.domain.Article;
import com.ly.blogsearch.domain.QueryResult;

import java.io.IOException;

public interface BlogService {
    public Article getArticleById(String id) throws IOException;
    public QueryResult getArticles(String query, Integer from, Integer size) throws IOException;
}

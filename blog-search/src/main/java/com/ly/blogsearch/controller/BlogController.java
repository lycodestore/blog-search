package com.ly.blogsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.ly.blogsearch.domain.Article;
import com.ly.blogsearch.domain.QueryResult;
import com.ly.blogsearch.domain.Response;
import com.ly.blogsearch.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/article/{id}")
    public Response getArticleById(@PathVariable("id") String id) {
        Response response = new Response();
        try {
            Article article = blogService.getArticleById(id);
            response.setCode(20000);
            response.setMessage("success");
            response.setResult(article);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(50000);
            response.setMessage(e.getMessage());
        } finally {
            return response;
        }
    }

    @PostMapping("/search")
    public Response searchArticles(@RequestBody JSONObject body) {
        String query = body.getString("query");
        Integer from = body.getInteger("from") == null ? 0 : body.getInteger("from");
        Integer size = body.getInteger("size") == null ? 10 : body.getInteger("size");

        Response response = new Response();

        try {
            QueryResult queryResult = blogService.getArticles(query, from, size);
            response.setCode(20000);
            response.setMessage("success");
            response.setResult(queryResult);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(50000);
            response.setMessage(e.getMessage());
        } finally {
            return response;
        }
    }

    @GetMapping("/suggest")
    public Response querySuggest(@PathParam("text") String text) {
        Response response = new Response();

        try {
            List<String> suggestions = blogService.getSuggestion(text);
            response.setCode(20000);
            response.setMessage("success");
            response.setResult(suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(50000);
            response.setMessage(e.getMessage());
        } finally {
            return response;
        }

    }
}

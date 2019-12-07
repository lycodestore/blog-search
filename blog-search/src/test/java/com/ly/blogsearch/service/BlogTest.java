package com.ly.blogsearch.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogTest {
    @Autowired
    private BlogService blogService;

    @Test
    public void testGet() throws IOException {
        System.out.println(blogService.getArticleById("1190000021124690"));
    }

    @Test
    public void testQuery() throws IOException {
        System.out.println(blogService.getArticles("大数据", 0, 10));
    }
}

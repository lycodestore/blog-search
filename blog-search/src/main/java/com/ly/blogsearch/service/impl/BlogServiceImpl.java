package com.ly.blogsearch.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.blogsearch.domain.Article;
import com.ly.blogsearch.domain.QueryResult;
import com.ly.blogsearch.service.BlogService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.*;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {
    @Resource
    private RestHighLevelClient client;

    private static final String BLOG_INDEX = "article";

    @Override
    public Article getArticleById(String id) throws IOException {
        GetRequest getRequest = new GetRequest(BLOG_INDEX, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resource = getResponse.getSource();
        Article article = new Article();
        article.setTitle(resource.get("title").toString());
        article.setSummary(resource.get("summary").toString());
        article.setImage(resource.get("image").toString());
        article.setViewCount(Integer.parseInt(resource.get("viewCount").toString()));

        return article;
    }

    @Override
    public QueryResult getArticles(String query, Integer from, Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(BLOG_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder mustQuery = new MatchQueryBuilder("title", query);
        QueryBuilder shouldQuery = new MatchQueryBuilder("summary", query);
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(mustQuery).should(shouldQuery));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(
                new HighlightBuilder.Field("title")
                        .highlighterType("plain")
                        .preTags("<span class='high-light'>")
                        .postTags("</span>"));
        highlightBuilder.field(
                new HighlightBuilder.Field("summary")
                        .highlighterType("plain")
                        .preTags("<span class='high-light'>")
                        .postTags("</span>"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        JSONObject rawResult = JSONObject.parseObject(searchResponse.toString());
        Integer total = rawResult.getJSONObject("hits").getJSONObject("total").getInteger("value");
        String relation = rawResult.getJSONObject("hits").getJSONObject("total").getString("relation");
        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        JSONArray hits = rawResult.getJSONObject("hits").getJSONArray("hits");
        for (int i = 0; i < hits.size(); i ++) {
            JSONObject hit = hits.getJSONObject(i);
            article.setId(hit.getString("_id"));
            JSONObject source = hit.getJSONObject("_source");
            article.setTitle(source.getString("title"));
            article.setSummary(source.getString("summary"));
            article.setImage(source.getString("image"));
            article.setViewCount(source.getInteger("viewCount"));
            JSONObject highlight = hit.getJSONObject("highlight");
            for (String key: highlight.keySet()) {
                if (key.equals("title")) {
                    article.setTitle(highlight.getJSONArray("title").getString(0));
                }
                if (key.equals("summary")) {
                    article.setSummary(highlight.getJSONArray("summary").getString(0));
                }
            }
            articles.add(article);
            article = new Article();
        }
        QueryResult queryResult = new QueryResult(total, relation, articles);


        return queryResult;
    }

    @Override
    public List<String> getSuggestion(String text) throws IOException {
        SearchRequest searchRequest = new SearchRequest(BLOG_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CompletionSuggestionBuilder completionSuggestion =
                SuggestBuilders.completionSuggestion("title_completion").text(text);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("completion-suggest", completionSuggestion);
        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<String> suggestions = new ArrayList<>();
        CompletionSuggestion suggest = searchResponse.getSuggest().getSuggestion("completion-suggest");
        for (CompletionSuggestion.Entry entry: suggest.getEntries()) {
            for (CompletionSuggestion.Entry.Option option: entry) {
                String suggestText = option.getText().string();
                suggestions.add(suggestText);
            }
        }

        return suggestions;
    }

}

## 创建索引

```
PUT /article
{
  "settings": {
    "analysis": {
      "analyzer": {
        "cn_analyzer": {
          "type": "ik_max_word"
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "analyzer": "cn_analyzer"
      },
      "summary": {
        "type": "text",
        "analyzer": "cn_analyzer"
      },
      "image": {
        "type": "keyword"
      },
      "viewCount": {
        "type": "integer"
      },
      "title_completion": {
        "type": "completion",
        "analyzer": "simple"
      }
    }
  }
}
```

## 搜索博客文章  
```
POST /article/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "新手"
          }
        }
      ],
      "should": [
        {
          "match": {
            "summary": "新手"
          }
        }
      ]
    }
  },
  "highlight": {
    "fields": {
      "title": {
        "type": "plain",
        "pre_tags": ["<span class='high-light'>"],
        "post_tags": ["</span>"]
      },
      "summary": {
        "type": "plain",
        "pre_tags": ["<span class='high-light'>"],
        "post_tags": ["</span>"]
      }
    }
  }
}
```

## 查询建议
```
POST /article/_search
{
  "suggest": {
    "my-suggestion": {
      "text": "数",
      "completion": {
        "field": "title_completion"
      }
    }
  }
}
```
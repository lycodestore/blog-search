# blog-search
blog-search是一款基于ElasticSearch打造的中文博客网站搜索引擎，对中文搜索友好，提供搜索提示和关键字高亮等功能

## 快速体验  

### 1、安装软件    

>ElasticSearch 7.2.0   

>ik中文分词器

### 2、新建索引  
在kibana中执行如下语句新建索引或通过postman等工具新建相同的索引
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

### 3、导入数据  
下载 docs/articles.jl文件到本地，然后通过如下命令批量导入数据到索引中
```
curl -X POST "localhost:9200/_bulk" -H 'Content-Type: application/json' --data-binary @file
```
Mac或者linux用户将file修改问articles.jl文件在本地的文件目录加文件名后直接执行即可，windows用户需要先安装curl工具  

### 4、启动项目  
使用IDEA或者eclipse等IDE启动blog-search文件夹中的项目   
然后打开浏览器访问 [http://localhost:8080](http://localhost:8080)   

> 首页  

![首页](./docs/home-page.png)

> 搜索建议    

![搜索提示](./docs/tips.png)  

> 搜索结果，关键词高亮显示   

![关键词高亮](./docs/result.png)
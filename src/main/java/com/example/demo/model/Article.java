package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "article")
@Data
public class Article {

    @Id
    private String articleId;

    private String articleName;

    public Article(String articleName){
        this.articleName=articleName;
    }

}

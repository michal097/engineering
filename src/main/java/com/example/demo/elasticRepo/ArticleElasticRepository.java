package com.example.demo.elasticRepo;

import com.example.demo.model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleElasticRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findAll();
}
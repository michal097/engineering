package com.example.demo.mongoRepo;

import com.example.demo.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleMongoRepository extends MongoRepository<Article, String> {

}

package com.example.demo.controller;

import com.example.demo.elasticRepo.ArticleElasticRepository;
import com.example.demo.elasticRepo.AuthorRepoElastic;
import com.example.demo.model.Article;
import com.example.demo.mongoRepo.ArticleMongoRepository;
import com.example.demo.model.Author;
import com.example.demo.mongoRepo.AuthorRepo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin
public class ArticleController {

    private final ArticleMongoRepository articleMongoRepository;
    private final ArticleElasticRepository articleElasticRepository;
    private final AuthorRepo authorRepo;
    private final AuthorRepoElastic authorRepoElastic;
    @Autowired
    public ArticleController(ArticleMongoRepository articleMongoRepository,AuthorRepoElastic authorRepoElastic, ArticleElasticRepository articleElasticRepository, AuthorRepo authorRepo){
        this.articleMongoRepository=articleMongoRepository;
        this.articleElasticRepository=articleElasticRepository;
        this.authorRepo=authorRepo;
        this.authorRepoElastic=authorRepoElastic;
    }

    @GetMapping("mongo")
    public List<Article> getAsMongo(){
        var a = Stream.of(new Article("MMMM"), new Article("BBBBB")).map(Article.class::cast).collect(toList());
        return articleMongoRepository.saveAll(a);
    }

    @GetMapping("elastic")
    public List<Article> getElastic(){
     return articleElasticRepository.findAll();
    }

    @GetMapping("dropElastic")
    public String check(){
        articleElasticRepository.deleteAll();
        return "Check this yo";
    }
    @GetMapping("author")
    public Author author(){
       return authorRepo.save(new Author("MIKeL0"));

    }

    @GetMapping("elasticAuthor")
    public Iterable<Author> authors(){
        return authorRepoElastic.findAll();
    }
//    @GetMapping("saveAndRead")
//    public List<Article> articles(){
//        return (List<Article>) articleElasticRepository.saveAll(Stream.of(new Article("MIK"), new Article("RYK")).collect(Collectors.toList()));
//    }
}

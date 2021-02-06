package com.example.demo.elasticRepo;

import com.example.demo.model.Author;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AuthorRepoElastic extends ElasticsearchRepository<Author,String> {
}

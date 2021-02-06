package com.example.demo.elasticRepo;

import com.example.demo.model.Client;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ClientElasticRepo extends ElasticsearchRepository<Client, String> {
}

package com.example.demo.elasticRepo;

import com.example.demo.model.Client;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ClientRepoElastic extends ElasticsearchRepository<Client, String> {
    List<Client> findAll();
}

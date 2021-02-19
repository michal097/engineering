package com.example.demo.elasticRepo;

import com.example.demo.model.Client;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ClientRepoElastic extends ElasticsearchRepository<Client, String> {
    List<Client> findAll();

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\",\"surname\",\"nip\"], \"fuzziness\": 10,\"prefix_length\": 1}}")
    List<Client> findClientsBySearchPhrase(String phrase);
}

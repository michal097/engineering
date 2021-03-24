package com.example.demo.elasticRepo;


import com.example.demo.model.ExternalClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ExtClientRepoElastic extends ElasticsearchRepository<ExternalClient, String> {

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\",\"surname\"], \"fuzziness\": 10,\"prefix_length\": 1}}")
    List<ExternalClient> findExternalClientsBySearchPhrase(String phrase, Pageable pageable);

    List<ExternalClient> findAll();

    Optional<ExternalClient> findByNip(String nip);
}

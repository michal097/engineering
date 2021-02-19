package com.example.demo.elasticRepo;

import com.example.demo.model.Invoice;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface InvoiceRepoElastic extends ElasticsearchRepository<Invoice, String> {
    List<Invoice> findAll();

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"invName\",\"invSurname\"], \"fuzziness\": 10,\"prefix_length\": 1}}")
    List<Invoice> findNIPWithFuzziness(String phrase);
}

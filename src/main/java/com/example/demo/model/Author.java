package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "author")
public class Author {
    @Id
    private String authorId;

    private String name;

    public Author(String name){
        this.setName(name);
    }
}

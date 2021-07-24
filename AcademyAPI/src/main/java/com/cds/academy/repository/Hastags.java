package com.cds.academy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cds.academy.model.Hastag;

public interface Hastags extends MongoRepository<Hastag, String>{

}

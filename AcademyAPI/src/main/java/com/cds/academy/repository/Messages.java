package com.cds.academy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cds.academy.model.Message;

public interface Messages extends MongoRepository<Message, String>{

}

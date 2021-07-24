package com.cds.academy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cds.academy.model.User;


public interface Users extends MongoRepository<User, String> {

	User findByUsername(String username);

}

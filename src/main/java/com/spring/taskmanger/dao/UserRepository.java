package com.spring.taskmanger.dao;

import org.springframework.data.repository.CrudRepository;

import com.spring.taskmanger.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}

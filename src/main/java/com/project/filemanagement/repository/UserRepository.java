package com.project.filemanagement.repository;

import org.springframework.data.repository.CrudRepository;

import com.project.filemanagement.dao.UserDao;

public interface UserRepository extends CrudRepository<UserDao, Integer> {
	UserDao findByUsername(String username);
}
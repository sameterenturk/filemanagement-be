package com.project.filemanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceTest {

	private Map<String, User> users = new HashMap<>();

	@PostConstruct
	public void initialize() {
		users.put("samet", new User("samet", "samet123456", new ArrayList<>()));

	}

	public User getUserByUsername(String username) {
		return users.get(username);
	}
}
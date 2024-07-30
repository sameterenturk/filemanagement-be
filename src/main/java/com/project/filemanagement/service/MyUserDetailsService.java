package com.project.filemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserServiceTest userServiceTest;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userServiceTest.getUserByUsername(username);
	}
}
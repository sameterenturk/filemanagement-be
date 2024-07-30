package com.project.filemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.filemanagement.request.AuthRequest;
import com.project.filemanagement.service.MyUserDetailsService;
import com.project.filemanagement.util.JwtUtil;

@RestController
public class AuthRestController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@PostMapping("/createToken")
	public String createToken(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new Exception("Incorret username or password", ex);
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);

		return jwt;
	}
}
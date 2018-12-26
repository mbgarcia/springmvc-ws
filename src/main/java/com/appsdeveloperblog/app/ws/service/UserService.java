package com.appsdeveloperblog.app.ws.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.UserNotFoundException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.Utils;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public UserEntity createUser(UserEntity user) {
		
		if (repository.existsByEmail(user.getEmail())) throw new RuntimeException("User already exists");
		
		user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
		user.setUserId(utils.generateUserId(20));
		
		UserEntity storedUser = repository.save(user);
		
		return storedUser;
	}
	
	public UserEntity updateUser(String publicId, UserEntity user) {
		UserEntity entity = findUserByPublicId(publicId);
		
		entity.setLastName(user.getLastName());
		entity.setFirstName(user.getFirstName());
		
		UserEntity storedUser = repository.save(entity);
		
		return storedUser;
	}
	public UserEntity findUserByPublicId(String publicId) {
		UserEntity user = repository.findByUserId(publicId);
		
		if (user == null)
			throw new UserNotFoundException(publicId);
		
		return user;
	}	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = repository.findByEmail(email);
		
		if (user == null)
			throw new UsernameNotFoundException(email);
		
		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}
	
	public UserEntity findUserByEmail(String email){
		UserEntity user = repository.findByEmail(email);
		
		if (user == null)
			throw new UsernameNotFoundException(email);
		
		return user;
	}
}

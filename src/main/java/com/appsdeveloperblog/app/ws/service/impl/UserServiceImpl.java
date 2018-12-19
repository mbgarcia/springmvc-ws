package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDto createUser(UserDto user) {
		
		if (repository.existsByEmail(user.getEmail())) throw new RuntimeException("User already exists");
		
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(user, userEntity);
		userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
		userEntity.setUserId(utils.generateUserId(20));
		
		UserEntity storedUser = repository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUser, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = repository.findByEmail(email);
		
		if (user == null)
			throw new UsernameNotFoundException(email);
		
		return new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
	}
}

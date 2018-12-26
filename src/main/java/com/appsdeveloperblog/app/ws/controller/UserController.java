package com.appsdeveloperblog.app.ws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(path="/{publicId}")
	public UserRest getUser(@PathVariable String publicId){
		UserRest returnValue = new UserRest();
		
		UserEntity dto = userService.findUserByPublicId(publicId);
		
		BeanUtils.copyProperties(dto, returnValue);
		
		return returnValue;
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){
		UserRest returnValue = new UserRest();
		
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userDetails, user);
		
		UserEntity createdUser = userService.createUser(user);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping
	public String updateUser(){
		return "update user was called";
	}
	
	@DeleteMapping
	public String deleteUser(){
		return "delete user was called";
	}
	
}

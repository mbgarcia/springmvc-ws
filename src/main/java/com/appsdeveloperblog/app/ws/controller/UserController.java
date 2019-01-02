package com.appsdeveloperblog.app.ws.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.request.UserUpdateRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserControllerResponse;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(path="/{publicId}"
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserControllerResponse getUser(@PathVariable String publicId){
		UserControllerResponse returnValue = new UserControllerResponse();
		
		UserEntity dto = userService.findUserByPublicId(publicId);
		
		BeanUtils.copyProperties(dto, returnValue);
		
		return returnValue;
	}

	@PostMapping(
			 consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserControllerResponse createUser(@RequestBody UserDetailsRequestModel userDetails){
		UserControllerResponse returnValue = new UserControllerResponse();
		
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userDetails, user);
		
		UserEntity createdUser = userService.createUser(user);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping(path="/{publicId}"
			,consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}			
			)
	public UserControllerResponse updateUser(@PathVariable String publicId, @RequestBody UserUpdateRequestModel userDetails){
		UserControllerResponse returnValue = new UserControllerResponse();
		
		UserEntity createdUser = userService.updateUser(publicId, userDetails);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{publicId}")
	public void deleteUser(@PathVariable String publicId){
		userService.deleteUser(publicId);
	}
	
	@GetMapping(produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserControllerResponse> getUsers(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int limit){
		List<UserControllerResponse> list = new ArrayList<UserControllerResponse>();
		
		List<UserEntity> users = userService.getUsers(page, limit);
		
		users.stream().forEach(user -> {
			UserControllerResponse item = new UserControllerResponse();
			BeanUtils.copyProperties(user, item);
			list.add(item);
		});
		
		return list;
	}
}
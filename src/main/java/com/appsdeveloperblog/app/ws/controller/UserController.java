package com.appsdeveloperblog.app.ws.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
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
import com.appsdeveloperblog.app.ws.ui.model.request.UserRequestDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserUpdateRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserResponseDto;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(path="/{publicId}"
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponseDto getUser(@PathVariable String publicId){
		UserResponseDto returnValue = new UserResponseDto();
		
		UserEntity dto = userService.findUserByPublicId(publicId);
		
		BeanUtils.copyProperties(dto, returnValue);
		
		return returnValue;
	}

	@PostMapping(
			 consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponseDto createUser(@RequestBody UserRequestDto userDetails){
		UserResponseDto returnValue = new UserResponseDto();
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity user = modelMapper.map(userDetails, UserEntity.class);
		
		UserEntity createdUser = userService.createUser(user);
		returnValue = modelMapper.map(createdUser, UserResponseDto.class);
		
		return returnValue;
	}
	
	@PutMapping(path="/{publicId}"
			,consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}			
			)
	public UserResponseDto updateUser(@PathVariable String publicId, @RequestBody UserUpdateRequestModel userDetails){
		UserResponseDto returnValue = new UserResponseDto();
		
		UserEntity createdUser = userService.updateUser(publicId, userDetails);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{publicId}")
	public void deleteUser(@PathVariable String publicId){
		userService.deleteUser(publicId);
	}
	
	@GetMapping(produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserResponseDto> getUsers(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int limit){
		List<UserResponseDto> list = new ArrayList<UserResponseDto>();
		
		List<UserEntity> users = userService.getUsers(page, limit);
		
		users.stream().forEach(user -> {
			UserResponseDto item = new UserResponseDto();
			BeanUtils.copyProperties(user, item);
			list.add(item);
		});
		
		return list;
	}
}
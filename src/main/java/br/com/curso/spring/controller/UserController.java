package br.com.curso.spring.controller;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

import br.com.curso.spring.io.entity.AddressEntity;
import br.com.curso.spring.io.entity.UserEntity;
import br.com.curso.spring.request.AddressDto;
import br.com.curso.spring.request.UserControllerRequest;
import br.com.curso.spring.request.UserControllerUpdateRequest;
import br.com.curso.spring.response.UserResponseDto;
import br.com.curso.spring.service.AddressService;
import br.com.curso.spring.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
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
	public UserResponseDto createUser(@RequestBody UserControllerRequest userDetails){
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
	public UserResponseDto updateUser(@PathVariable String publicId, @RequestBody UserControllerUpdateRequest userDetails){
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
		List<UserEntity> users = userService.getUsers(page, limit);
		
		Type listType = new TypeToken<List<UserResponseDto>>() {}.getType();
				
		return new ModelMapper().map(users, listType);
	}
	
	@GetMapping(path="/{publicId}/addresses"
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<AddressDto> getUserAddresses(@PathVariable String publicId){
		List<AddressEntity> list = addressService.listUserAddresses(publicId);
		
		Type listType = new TypeToken<List<AddressDto>>() {}.getType();
		
		return new ModelMapper().map(list, listType);
	}
	
}
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
import br.com.curso.spring.request.UserControllerPostRequest;
import br.com.curso.spring.request.UserControllerPutRequest;
import br.com.curso.spring.response.UserControlerResponse;
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
	public UserControlerResponse getUser(@PathVariable String publicId){
		UserEntity dto = userService.findUserByPublicId(publicId);
		
		return new ModelMapper().map(dto, UserControlerResponse.class);
	}

	@PostMapping(
			 consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserControlerResponse createUser(@RequestBody UserControllerPostRequest userDetails){
		ModelMapper modelMapper = new ModelMapper();
		UserEntity user = modelMapper.map(userDetails, UserEntity.class);
		
		UserEntity createdUser = userService.createUser(user);
		
		return modelMapper.map(createdUser, UserControlerResponse.class);
	}
	
	@PutMapping(path="/{publicId}"
			,consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
			,produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}			
			)
	public UserControlerResponse updateUser(@PathVariable String publicId, @RequestBody UserControllerPutRequest userDetails){
		UserControlerResponse returnValue = new UserControlerResponse();
		
		UserEntity createdUser = userService.updateUser(publicId, userDetails);
		BeanUtils.copyProperties(createdUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{publicId}")
	public void deleteUser(@PathVariable String publicId){
		userService.deleteUser(publicId);
	}
	
	@GetMapping(produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserControlerResponse> getUsers(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int limit){
		List<UserEntity> users = userService.getUsers(page, limit);
		
		Type listType = new TypeToken<List<UserControlerResponse>>() {}.getType();
				
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
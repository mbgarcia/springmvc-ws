package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	UserService userService;
	
	public List<AddressEntity> listUserAddresses(String publicId){
		UserEntity user = userService.findUserByPublicId(publicId);
		
		return addressRepository.findByUser(user);
	}
}

package br.com.curso.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.curso.spring.io.entity.AddressEntity;
import br.com.curso.spring.io.entity.UserEntity;
import br.com.curso.spring.repository.AddressRepository;

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

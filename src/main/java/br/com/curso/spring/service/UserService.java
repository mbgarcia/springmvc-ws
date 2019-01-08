package br.com.curso.spring.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.curso.spring.exception.UserNotFoundException;
import br.com.curso.spring.io.entity.UserEntity;
import br.com.curso.spring.repository.UserRepository;
import br.com.curso.spring.request.UserControllerPutRequest;
import br.com.curso.spring.shared.Utils;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository repository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public UserEntity createUser(UserEntity user) {

		if (repository.existsByEmail(user.getEmail()))
			throw new RuntimeException("User already exists");

		user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
		user.setUserId(utils.generateUserId(20));
		user.getAddresses().stream().forEach(ad -> {ad.setUser(user);});

		UserEntity storedUser = repository.save(user);

		return storedUser;
	}

	public UserEntity updateUser(String publicId, UserControllerPutRequest user) {
		UserEntity entity = findUserByPublicId(publicId);

		entity.setLastName(user.getLastName());
		entity.setFirstName(user.getFirstName());

		UserEntity storedUser = repository.save(entity);

		return storedUser;
	}

	public void deleteUser(String publicId) {
		UserEntity entity = findUserByPublicId(publicId);

		entity.setDeleted(true);
		entity.setDeletedAt(LocalDateTime.now());

		repository.save(entity);
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

	public UserEntity findUserByEmail(String email) {
		UserEntity user = repository.findByEmail(email);

		if (user == null)
			throw new UsernameNotFoundException(email);

		return user;
	}

	public List<UserEntity> getUsers(int page, int limit) {
		if (page > 0)
			page -= 1;

		Pageable pageable = PageRequest.of(page, limit);

		return repository.findAll(pageable).getContent();
	}
}
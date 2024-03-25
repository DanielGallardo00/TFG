package com.woola.woola.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woola.woola.model.User;
import com.woola.woola.repository.UserRepository;

@Service
public class UserService {
    @Autowired
	private UserRepository users;

	public void save(User user) {
		users.save(user);
	}

	public Optional<User> findByEmail(String mail) {
		return users.findByEmail(mail);
	}

	public Optional<User> findByUsername(String username) {
	 	return users.findByUsername(username);
	}

	public List<User> findAll() {
		return users.findAll();
	}

	public Optional<User> findById(long id) {
		Optional<User> findById = users.findById(id);
		return findById;
	}

	public boolean existEmail(String email) {
	 	Optional<User> user = findByEmail(email);
	 	return user.isPresent();
	}

	public boolean existUsername(String username) {
		Optional<User> user = findByUsername(username);
		return user.isPresent();
   }

	public void deleteById(long id){
		users.deleteById(id);
	}
}

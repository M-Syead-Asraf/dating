package com.mydating.dating.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mydating.dating.entity.User;
import com.mydating.dating.repository.UserRepository;
import com.mydating.dating.util.UserGender;

@Repository
public class UserDao {
	
	@Autowired
	UserRepository userRepository;

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public List<User> findAllMaleUsers() {
		return userRepository.findByGender(UserGender.MALE);
	}

	public List<User> findAllFemaleUsers() {
		return userRepository.findByGender(UserGender.FEMALE);
	}

	public Optional<User> findUserById(int id) {
		return userRepository.findById(id);
	}
	
}

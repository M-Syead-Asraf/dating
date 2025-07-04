package com.mydating.dating.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mydating.dating.dao.UserDao;
import com.mydating.dating.dto.MatchingUser;
import com.mydating.dating.entity.User;
import com.mydating.dating.util.UserGender;
import com.mydating.dating.util.UserSorting;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;

	public ResponseEntity<?> saveUser(User user) {
		User savedUser = userDao.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	public ResponseEntity<?> findAllMaleUsers() {
		List<User> maleUsers = userDao.findAllMaleUsers();
		if(maleUsers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Male Users Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(maleUsers);
	}

	public ResponseEntity<?> findAllFemaleUsers() {
		List<User> femaleUsers = userDao.findAllFemaleUsers();
		if(femaleUsers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Female Users Found");
		}
		return ResponseEntity.status(HttpStatus.OK).body(femaleUsers);
	}

	public ResponseEntity<?> findBestMatch(int id, int top) {
		Optional<User> optional = userDao.findUserById(id);
		if(optional.isEmpty()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
		User user = optional.get();
		List<User> users = user.getGender().equals(UserGender.MALE) ? userDao.findAllFemaleUsers() : userDao.findAllMaleUsers();
		List<MatchingUser> matchingUsers = new ArrayList<>();
		for(User u : users) {
			MatchingUser mu = new MatchingUser();
			mu.setId(u.getId());
			mu.setName(u.getName());
			mu.setEmail(u.getEmail());
			mu.setPhone(u.getPhone());
			mu.setAge(u.getAge());
			mu.setIntrests(u.getIntrests());
			mu.setGender(u.getGender());
			mu.setAgeDiff(Math.abs(user.getAge() - u.getAge()));
			List<String> intrests1 = user.getIntrests();
			List<String> intrests2 = u.getIntrests();
			int mic = 0;
			for(String s : intrests1) 
				if(intrests2.contains(s))
					mic++;
			mu.setIntrestCount(mic);
			matchingUsers.add(mu);
		}
		UserSorting us = new UserSorting();
		matchingUsers.sort(us);
		ArrayList<MatchingUser> result = new ArrayList<>();
		for(MatchingUser u : matchingUsers) {
			if(top==0)
				break;
			result.add(u);
			top--;
		}
		return  ResponseEntity.status(HttpStatus.OK).body(result);
	}

	public ResponseEntity<?> searchByName(String letters) {
		List<User> users = userDao.searchByName("%"+letters+"%");
		if(users.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Users Found with letters: "+letters);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	public ResponseEntity<?> searchByEmail(String letters) {
		List<User> users = userDao.searchByEmail("%"+letters+"%");
		if(users.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Users Found with letters: "+letters+" in thier mail");
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
}

package com.smart.config;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> getUsers(){
		
		return userRepository.findAll();
	}
	
//	public boolean isEmailExists(String email)
//	{
//		return userRepository.existByEmail(email);
//	}
	
//	public User createUser(User user)
//	{
//		  user.setId(UUID.randomUUID());
//			return userRepository.save(user);
//	}

}

package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.smart.entities.User;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {
               
	          
}

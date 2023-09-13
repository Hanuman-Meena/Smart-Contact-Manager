package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.smart.entities.Contact;
import com.smart.entities.User;

@Component
public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	//currentPage == page;
	//contact per page == 10;
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUserId(@Param("userId") int userId, Pageable pageable);
	
	public List<Contact> findByNameContainingAndUser(String name,User user);  //Search functionality

	
}

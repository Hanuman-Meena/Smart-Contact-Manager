package com.smart.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CONTACT")
public class Contact {

	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int cId;
	   private String name;
	   private String nickName;
	   private String phone;
	   private String email;	   
	   private String work;
	   @Column(length = 50000)
	   private String description;
	   private String image;
	   
	   @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	   private User user;
	   
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public User getUser() {
		return user;
	}
    public void setUser(User user) {
		this.user = user;
	}
	
	   
	   
	    
}

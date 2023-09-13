package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="CONTACT")
@Getter
@Setter
public class Contact {

	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int cId;
	   private String name;
	   private String nickName;
	   private String phone;
	   private String email;	   
	   private String work;
	   @Column(length = 1000)
	   private String description;
	   
	   
	   private String image;
	   
	   @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	   @JsonIgnore
	   private User user;
	   
	public Contact() {
		super();
		
	}
	
	
	    @Override
	    public String toString() {
	        return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", phone=" + phone + ", email=" + email
	                + ", work=" + work + ", description=" + description + "]";
	    }
	
	   
	   
	    
}

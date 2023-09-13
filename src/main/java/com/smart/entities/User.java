package com.smart.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="USER")
@Component
@Getter
@Setter
public class User{
           
           @GeneratedValue(strategy = GenerationType.AUTO)
	       @Id
	       private int id;
           
           @NotBlank(message = "Name field can't be empty")
           @Size(min = 1, max = 15, message = "Name must be between 1-15 characters!!")
	       private String name;
           
	       @Column(unique = true)
	       @Email(regexp = "^[A-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	       @NotEmpty
	       private String email;
	       
	      // @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}[]:;<>,.?/~_+-=|\\]).{8,32}$")
	       private String password;
	       
	       private String role;
	       
	       @Column(length = 500)
	       private String description;
	       
	       private boolean enabled;
	       
	       private String imageUrl;
	       
	       @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	       private List<Contact> contacts = new ArrayList<>();
	       
		public User() {
			super();
			
		}

		
		@Override
	    public String toString() {
	        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role + ", description=" + description + ", enabled=" + enabled + ", imageUrl=" + imageUrl + "]";
	    }
}

	       		       


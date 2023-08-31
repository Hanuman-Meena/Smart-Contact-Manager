package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	       
	       @Autowired
	       private UserRepository userRepository;
            
	       @GetMapping("/home")
	       public String home(Model model)
	       {
	    	   model.addAttribute("title","Home- Smart Contact Manager");
	    	   
	    	   return "home";
	       }
	       
	       @GetMapping("/")
	       public String homePage(Model model)
	       {
	    	   model.addAttribute("title","Home- Smart Contact Manager");
	    	   
	    	   return "home";
	       }
	        
	       @GetMapping("/about")
	       public String about(Model model)
	       {
	    	   model.addAttribute("title","About- Smart Contact Manager");
	    	   
	    	   return "about";
	       }
	       
	       @GetMapping("/signup")
	       public String signUp(Model model)
	       {
	    	   model.addAttribute("title","Register- Smart Contact Manager");
	    	   model.addAttribute("user", new User());
	    	   
	    	   return "signup";
	       }
	       
	       //This handler is for registering user
           @PostMapping("/do_signup")
	       public String registerUser(@ModelAttribute("user") User user, @RequestParam(value = "agreement",defaultValue = "false")
	       boolean agreement, Model model, HttpSession session)
	       {
        	  try {
        		  if(!agreement)
           	   {
           		   System.out.println("you have not agreed the terms and conditions");
           		   throw new Exception("you have not agreed the terms and conditions");
           	   }
           	   
           	   user.setRole("USER_ROLE");
           	   user.setEnabled(true);
           	   
           	   User result = this.userRepository.save(user);
           	   model.addAttribute("user",new User());
           	   
           	session.setAttribute("message","Registered successfully!!");
           	   
   	    	   System.out.println("User"+' '+ user);
   	    	   System.out.println("Agreement"+ ' '+ agreement);
   	    	   
   	    	 return "signup";     
   	    	  
			} catch (Exception e) {
				
				e.printStackTrace();
				model.addAttribute("user",user);
				session.setAttribute("message", "something went wrong");
				return "signup";
			}
        	  
	       }
	       
}

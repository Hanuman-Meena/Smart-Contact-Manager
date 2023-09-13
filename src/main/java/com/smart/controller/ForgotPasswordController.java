package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	//email ID form open handler
	
    @GetMapping("/forgot-password")
    public String openEmailForm(Model model)
    {
 	  
        model.addAttribute("title", "Forgot Password - Smart Contact Manager");
 	   
 	   return "enter-email";
    }
    
    
    //send OTP handler
    
    @PostMapping("/send-otp")
    public String sendOtp(Model model,@RequestParam("email") String email, HttpSession session) throws MessagingException
    {       
            
    	    User user = userRepository.getUserByUserName(email);
    	    
    	    if(user != null)
    	    {
    	    	    Random random = new Random();
    		    	int otp = random.nextInt(1000, 9999);
    		    	
    		    	
    		    	String subject = "Reset Smart Contact Manager Password";
    		    	String  message = " "
    		    	                  + "<div>"
    		    			          + "<h3>"
    		    	                  + "Your OTP to reset password is: "
    		    	                  + "<b>"+otp
    		    	                  + "</b>"
    		    			          + "</h3>"
    		    			          + "</div>";
    		    	
    		    	String to = email;
    		    	
     		    	boolean flag = this.emailService.sendEmail(subject,message,to);
    		    			    	
    		    	if(flag)
    		    	{  
    		    		session.setAttribute("message", new Message("Check your email id", "alert-danger"));
    		    		
    		    		session.setAttribute("sentOTP", otp);
    		    		session.setAttribute("email", email);
    		    		
    		    		return "verify-otp";
    		    	}
    		    	else {
    		    		
    		    		return "enter-email";
    		    	}	
    	    }
    	    else
    	    {   
    	    	session.setAttribute("message", new Message("You're not registered with us!!", "alert-danger"));
    	    	return "enter-email";
    	    }
    	  
    	   
    	
    }
    
    // verify OTP handler
    
    @PostMapping("/verify-otp")
    public String verifyOtp(Model model, @RequestParam("otp") int otp, HttpSession session)
    {
    	
    	int sentOTP = (int)session.getAttribute("sentOTP");
    	
    	if(sentOTP == otp)
    	{
    		 model.addAttribute("title", "Reset Password - Smart Contact Manager");
    			 
   			 return "reset-password";
    		 
    	}
    	else
    	{
    		session.setAttribute("message", new Message("Incorrect OTP", "alert-danger"));
    		
    		return "verify-otp";
    	}
 	  
    }
    
    // reset-password handler
    
    @PostMapping("/reset-password")
    public String resetPassword(Model model,@RequestParam("newPassword") String newPassword, HttpSession session)
    {
    	
    	String email = (String)session.getAttribute("email");
    	
    	User user = userRepository.getUserByUserName(email);
    	
    	try {
    		
    		user.setPassword(passwordEncoder.encode(newPassword));
        	userRepository.save(user);
        	
        	session.setAttribute("message", new Message("Password changed successfully!!", "alert-success"));
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
  	
    	return "/login";
    }
    
    
    

}

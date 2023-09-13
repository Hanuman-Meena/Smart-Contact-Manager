package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.smart.dao.UserRepository;


@Configuration
@EnableWebSecurity
public class mySecurityConfig {
	
	
	@Autowired
    private UserDetailsService getUserDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Bean
	public UserDetailsService getUserDetailService()
	{
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
   
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf-> csrf.disable())
             .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/**").permitAll())
             .formLogin(formLogin -> formLogin
            	        .loginPage("/login") 
            	        .loginProcessingUrl("/login")
            	        .defaultSuccessUrl("/user/index")
            	        .failureUrl("/login?error=true"));
            
        
        return http.build();
    }
  
	    
		
     @Bean     	 
	 public DaoAuthenticationProvider daoAuthenticationProvider()
	 {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		
		auth.setUserDetailsService(this.getUserDetailService());
		auth.setPasswordEncoder(passwordEncoder());
		
		
		return auth;	
	 }
     
     	
     

     
     
	
}

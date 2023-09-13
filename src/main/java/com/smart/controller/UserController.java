package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName(); // using principal interface to get the userName(email)

		System.out.println("UserName:" + userName); // printing the user name(email) to the console

		// getting the user data using userName(email)

		User user = userRepository.getUserByUserName(userName); // using userRepo's method to access the user data

		String pageTitle = user.getName() + " - Dashboard";

		System.out.println("User: " + user); // printing the user data to the console by using userName(email)

		model.addAttribute("user", user); // adding the user data to the model
	}

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		String userName = principal.getName();

		User user = userRepository.getUserByUserName(userName);

		String pageTitle = user.getName() + " - Dashboard";

		model.addAttribute("title", pageTitle);

		return "normal/user_dashboard";
	}

	// add-contact handler

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add-contact-form";
	}

	// handler for processing contacts

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			String name = principal.getName();

			User user = userRepository.getUserByUserName(name);

			// processing and uploading files

			if (file.isEmpty()) {
				// message in case file is empty

				System.out.println("File is empty");
				
				contact.setImage("contact.png");
				
				
			} else {
				// upload the file to folder and update the name in the contact

				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/Images").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image uploaded successfully");

				// print success message

				session.setAttribute("message", new Message("Your contact is added!!", "alert-success"));

			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("Contact details:" + contact);

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();

			// print error message

			session.setAttribute("message", new Message("Something went wrong!!", "alert-danger"));
		}

		return "normal/add-contact-form";
	}

	// handler for view-contact
	// how many contacts per page ? == 10;
	// current page index ==0;

	@GetMapping("/view-contacts/{page}")
	public String viewContacts(@PathVariable("page")Integer page ,Model model, Principal principal) {
		
		model.addAttribute("title", "View Contacts");

		String name = principal.getName();

		User user = userRepository.getUserByUserName(name);

		int userId = user.getId();
		
		//current page == page;
		// size = number of contacts per page;
		
		Pageable pageable =PageRequest.of(page, 5);

		Page<Contact> contacts = contactRepository.findContactsByUserId(userId,pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		// printing user contacts

		System.out.println("Contacts:" + contacts);

		return "normal/view-contacts";
	}
	
	//creating user profile in 

	@GetMapping("/{cId}/contact")
	public String userProfile(@PathVariable("cId")Integer cId, Model model, Principal principal)
	{
		
		
		System.out.println("ID: "+ cId);
		
		Optional<Contact> optionalContact = contactRepository.findById(cId);
		
		Contact contact  = optionalContact.get();
		
		//getting user
		
		String name = principal.getName();
		
		User user = userRepository.getUserByUserName(name);
		
		if(user.getId()== contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName()+" - Profile");
		}		
		
		return "normal/contact-profile";
	}
	
	//user profile handler
	
	@GetMapping("/profile")
	public String userProfile(Model model, Principal principal)
	{
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		model.addAttribute("title", user.getName()+" - Profile");
		return "normal/user-profile";
	}
	
	//delete contact handler
	
	@GetMapping("/delete-contact/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Principal principal, HttpSession session)
	{
		Optional<Contact> optionalContact = contactRepository.findById(cId);
		
		Contact contact = optionalContact.get();
		
		// Security check for the user
		
		String name = principal.getName();
		
		User user = userRepository.getUserByUserName(name);
				
	 if(user.getId()==contact.getUser().getId())
	 {
		 
		contact.setUser(null);
		this.contactRepository.deleteById(cId);
		session.setAttribute("message", new Message("Contact Deleted Successfully!!", "alert-success"));
		
	 }
	
		return "redirect:/user/view-contacts/0";
	}
	
	//Update contact handler
	
	@PostMapping("/update-contact/{cId}")
	public String updateContact(@PathVariable("cId")Integer cId, Model model)
	{
		
		Contact contact = contactRepository.findById(cId).get();
		
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Update - Contact");
		
		return "normal/update-contact";
	}
	
	//process-update handler
	
	@PostMapping("/process-update")
	public String updateProcess(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, 
			Model model, Principal principal)
	{
		try {
			
			 //checking if new image is selected then we need to update the image in the database as well
			// fetching old contact details
			
			Contact oldContactDetails = contactRepository.findById(contact.getCId()).get();
			
			if(!file.isEmpty())
			{       
				//Deleting the old  photo
				
				File deleteFile = new ClassPathResource("static/Images").getFile();
				File file1 = new File(deleteFile, oldContactDetails.getImage());
				file1.delete();
				
				
				//updating the new photo
				
					File saveFile = new ClassPathResource("static/Images").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator+ file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					contact.setImage(file.getOriginalFilename());
					
			}
			else
			{
				contact.setImage(oldContactDetails.getImage());  //when the photo is not updated
			}
			String name = principal.getName();
			
			User user = userRepository.getUserByUserName(name);
			
			contact.setUser(user);
			
			contactRepository.save(contact);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getCId()+"/contact";
	}
	
	// Setting handler
	
	@GetMapping("/setting")
	public String openSetting(Model model)
	{
		model.addAttribute("title", "Setting - Smart Contact Manager");
		
		return "normal/setting";
	}
	
	//change-password handler
	
	@PostMapping("/change-password")
	public String changePassword(Model model, Principal principal,
			@RequestParam("oldPassword")String oldPassword, 
			@RequestParam("newPassword") String newPassword,
			 HttpSession session)
	{
		User user = userRepository.getUserByUserName(principal.getName());
		
		
		
		try {
			if(this.passwordEncoder.matches(oldPassword, user.getPassword()))
			{
				user.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(user);
				
			}
			else
			{
     			session.setAttribute("message", new Message("Incorrect Password!!","alert-danger"));
     			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    model.addAttribute("title", "Setting - Smart Contact Manager"); 
	    
		
		return "redirect:/user/index";
	}

}

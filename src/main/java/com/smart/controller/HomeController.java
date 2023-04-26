package com.smart.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","Home - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@RequestMapping("/signing")
	public String login(Model model) {
		model.addAttribute("title","Login");
		return "login";
	}
	
	//this handler for registering user
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement",defaultValue = "false") boolean agreement,
			Model model,HttpSession session) {
		try {
			if(!agreement) {
				System.out.print("You have not agreed the terms and conditions ");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if(result1.hasErrors()) {
				System.out.print("ERROR"+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			String hasCode= passwordEncoder.encode(user.getPassword());
			user.setPassword(hasCode);
			
			System.out.println("Agreement "+agreement);
			System.out.println("User "+user);
			
			User result = this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registerd !!", "alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something Went Wrong !!"+e.getMessage(), "alert-danger"));
			return "signup";
		}
		
	}
}

package com.smart.controller;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	//method for adding common data
	@ModelAttribute
	public void addCommonData(Model m,Principal principal){
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		m.addAttribute("user",user);
	}

	// dashboard home
	@RequestMapping(value = "/index")
	public String dashboard(Model model) {
		model.addAttribute("title","User DashBoard");
		return "normal/user_dashboard";
	}

	//open add form handler
	@GetMapping("/add_contact")
	public String openAddContactForm(Model model){
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact-form";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
								 @RequestParam("profileImage") MultipartFile file,
								 Principal principal, HttpSession http){
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			//processing and uploading file
			if(file.isEmpty()){
				System.out.println("File is Empty");
			}else{
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);

			//success message send to user
			http.setAttribute("message",new Message("Contact added to database","success"));
		}catch (Exception e){
			System.out.println("error"+e.getMessage());
			e.printStackTrace();
			//Failed message send to user
			http.setAttribute("message",new Message("Something went wrong","danger"));
		}


		return "normal/add_contact-form";
	}
	@GetMapping("/show-contacts")
	public String showContacts(Model model,Principal principal){
		model.addAttribute("title","Show user contacts");
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		List<Contact> contacts= contactRepository.findContactByUser(user.getId());
		model.addAttribute("contacts",contacts);
		return "normal/show_contacts";
	}

	//delete contact handler
	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id") Integer cid,Model model,Principal principal,HttpSession http){
		Optional<Contact> contactOptional =this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		String name =principal.getName();
		User user = userRepository.getUserByUserName(name);
		if(user.getId()==contact.getUser().getId()) {
			user.getContacts().remove(contact);
			this.userRepository.save(user);
			http.setAttribute("message",new Message("Your detail deleted successfully","success"));
		}else{
			http.setAttribute("message",new Message("Sorry you are not authorized to delete this detail's","danger"));
		}
		return "redirect:/user/show-contacts";
	}

	//update user contacts
	@PostMapping("/update-contact/{id}")
	public String updateContacts(@PathVariable("id")Integer cId,Model model){
		model.addAttribute("title","Update Contacts");
		Contact contact=this.contactRepository.findById(cId).get();
		model.addAttribute("contact",contact);
		return "normal/update_contact";
	}

	@PostMapping("/process-update")
	public String Update(@ModelAttribute Contact contact,
								 @RequestParam("profileImage") MultipartFile file,Model model,
								 Principal principal, HttpSession http){
		try {
			Contact oldContactDetails=this.contactRepository.findById(contact.getId()).get();

			//processing and uploading file
			if(!file.isEmpty()){
				//delete old file
				File deleteFile = new ClassPathResource("static/image").getFile();
				File file1 = new File(deleteFile,oldContactDetails.getImage());
				file1.delete();

				//update new file
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}else{
				contact.setImage(oldContactDetails.getImage());

			}
			User user = userRepository.getUserByUserName( principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			//success message send to user
			http.setAttribute("message",new Message("Contact updated to database","success"));
		}catch (Exception e){
			System.out.println("error"+e.getMessage());
			e.printStackTrace();
			//Failed message send to user
			http.setAttribute("message",new Message("Something went wrong","danger"));
		}


		return "redirect:/user/show-contacts";
	}
}

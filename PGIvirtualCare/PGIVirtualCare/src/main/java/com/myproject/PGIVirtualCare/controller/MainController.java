package com.myproject.PGIvirtualCare.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIvirtualCare.Model.Enquiry;
import com.myproject.PGIvirtualCare.Model.Users;
import com.myproject.PGIvirtualCare.Model.Users.UserRole;
import com.myproject.PGIvirtualCare.Model.Users.UserStatus;
import com.myproject.PGIvirtualCare.Respository.EnquiryRepository;
import com.myproject.PGIvirtualCare.Respository.UserRepository;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	@Autowired
	private EnquiryRepository enquiryRepo;

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/")
	public String showIndex() {
		return "index";
	}

	
	@GetMapping("/AboutUs")
	public String showAboutUs() {
		return "aboutus";
	}

	
	@GetMapping("/ContactUs")
	public String showContactUs(Model model) {
		Enquiry enquiry = new Enquiry();
		model.addAttribute("enquiry", enquiry);
		return "ContactUs";

	}
	
	
	@GetMapping("/Registration")
	public String showRegistration(Model model) {
		Users userDto = new Users();
		model.addAttribute("userDto", userDto);
		return "registration";
	}

	
	@PostMapping("/Registration")
	public String Registration(@ModelAttribute("userDto") Users newUser, RedirectAttributes attributes) {
		try {
			newUser.setRole(UserRole.PATIENT);
			newUser.setStatus(UserStatus.PENDING);
			newUser.setRegDate(LocalDateTime.now());
			
			userRepo.save(newUser);
			attributes.addFlashAttribute("msg", "Regitration Successfull !");
			return "redirect:/Registration";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getMessage());
			return "redirect:/Registration";
		}
	}
	//--------------Admin login.------------------
	@GetMapping("/AdminLogin")
	public String ShowAdminLogin() {
		return "AdminLogin";
	}
	@PostMapping("/AdminLogin")
	public String AdminLogin(HttpServletRequest  request, RedirectAttributes attributes,HttpSession session) 
	{
		try {
			String username = request.getParameter("email");
			String password = request.getParameter("password");
			if(!userRepo.existsByEmail(username))
			{
				attributes.addFlashAttribute("msg", "User doesn't  exist!");
				return "redirect:/AdminLogin";
			}
			Users admin = userRepo.findByEmail(username);
			if(password.equals(admin.getPassword()) && admin.getRole().equals(UserRole.ADMIN)) {
			    session.setAttribute("loggedInAdmin", admin);
				return "redirect:/Admin/AdminDashboard";
			}else {
				attributes.addFlashAttribute("msg","Invalid user or Password");
			}
			
			return "redirect:/AdminLogin";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg","Error : "+e.getMessage());
			return "redirect:/AdminLogin";
		}
	}
	
	//----------DoctorLogin-------------
	
	@GetMapping("/DoctorLogin")
	public String ShowDoctorLogin() {
		return "DoctorLogin";
	}
	
	
	@PostMapping("/DoctorLogin")
	public String DoctorLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "user doesn't exist");
				return "redirect:/DoctorLogin";
			}

			Users doctor = userRepo.findByEmail(email);

			if (password.equals(doctor.getPassword()) && doctor.getRole().equals(UserRole.DOCTOR)) {
				if (doctor.getStatus().equals(UserStatus.PENDING)) {
					attributes.addFlashAttribute("msg", "Registration Pending, wait for Admin Approval");
				} else if (doctor.getStatus().equals(UserStatus.DISABLE)) {
					attributes.addFlashAttribute("msg", "Login Disabbled 🚫, Please Contact Administration");

				} else {
					session.setAttribute("loggedInDoctor", doctor);
					return "redirect:/Doctor/DoctorDashboard";
				}

			} else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
			}

			return "redirect:/DoctorLogin";
		} catch (Exception e) {
			return "redirect:/DoctorLogin";
		}
	}
	
	
	
	//-------------Patient Login-----------
	
	@GetMapping("/PatientLogin")
	public String ShowPatientLogin() {
		return "PatientLogin";
	}
	
	@PostMapping("/PatientLogin")
	public String PatientLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "user doesn't exist");
				return "redirect:/PatientLogin";
			}

			Users patient = userRepo.findByEmail(email);

			if (password.equals(patient.getPassword()) && patient.getRole().equals(UserRole.PATIENT)) {
				if (patient.getStatus().equals(UserStatus.PENDING)) {
					attributes.addFlashAttribute("msg", "Registration Pending, wait for Admin Approval");
				} else if (patient.getStatus().equals(UserStatus.DISABLE)) {
					attributes.addFlashAttribute("msg", "Login Disabbled 🚫, Please Contact Administration");

				} else {
					session.setAttribute("loggedInPatient", patient);
					return "redirect:/Patient/PatientDashboard";
				}

			} else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");
			}

			return "redirect:/PatientLogin";
		} catch (Exception e) {
			return "redirect:/PatientLogin";
		}
	}
	
	
	//-----------service-----------------
	
    @GetMapping("/Service")
	public String showService() {
		return "service";
	}
    
    
//------------contact---------------------
	@PostMapping("/ContactUs")
	public String SubmitEnquiry(@ModelAttribute("enquiry") Enquiry enquiry) {
		try {
			enquiry.setEnquiryDate(LocalDateTime.now());
			enquiryRepo.save(enquiry);
			return "redirect:/ContactUs";
		} catch (Exception e) {
			return "redirect:/ContactUs";
		}
	}

}

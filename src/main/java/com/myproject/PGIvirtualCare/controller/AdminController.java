package com.myproject.PGIvirtualCare.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIvirtualCare.API.SendAutoEmail;
import com.myproject.PGIvirtualCare.Model.Enquiry;
import com.myproject.PGIvirtualCare.Model.Users;
import com.myproject.PGIvirtualCare.Model.Users.UserRole;
import com.myproject.PGIvirtualCare.Model.Users.UserStatus;
import com.myproject.PGIvirtualCare.Respository.EnquiryRepository;
import com.myproject.PGIvirtualCare.Respository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class AdminController {
	
	@Autowired
	private HttpSession session;
	
    @Autowired
	private UserRepository userRepo;
    
    @Autowired
    private SendAutoEmail sendAutoEmail;
    
    @Autowired
    private EnquiryRepository EnquiryRepo;
	
	@GetMapping("/AdminDashboard")
	public String showAdminDashboard() 
	{
		if(session.getAttribute("loggedInAdmin") == null)
		{
			return "redirect:/AdminLogin";
		}
		return "Admin/AdminDashboard";
	}
	  
	
	
	
	//----------ViewAppointments-------------
	
	@GetMapping("/ViewAppointments")
	public String showViewAppointments() 
	{
		if(session.getAttribute("loggedInAdmin") == null)
		{
			return "redirect:/AdminLogin";
		}
		return "Admin/ViewAppointments";
	}
	
	//------------ManagePrescriptions----------
	
	@GetMapping("/ManagePrescriptions")
	public String showManagePrescriptions() 
	{
		if(session.getAttribute("loggedInAdmin") == null)
		{
			return "redirect:/AdminLogin";
		}
		return "Admin/ManagePrescriptions";
	}
	
	//---------------ViewDoctors--------------
	
	@GetMapping("/ViewDoctors")
	public String showViewDoctors(Model model) 
	{
		if(session.getAttribute("loggedInAdmin") == null)
		{
			return "redirect:/AdminLogin";
		}
		List<Users> doctorList = userRepo.findAllByRole(UserRole.DOCTOR);
		model.addAttribute("doctorList", doctorList);
		return "Admin/ViewDoctors";
	}
	
	//----------Delete doctor-------------------
	
	@GetMapping("/Deletedoctor")
	public String Deletedoctor(@RequestParam("id") long id) 
	{
		userRepo.deleteById(id);
		return "redirect:/Admin/ViewDoctors";
	}
	
	
	//-----------ManageMedicalReports-----------
	
	@GetMapping("/ManageMedicalReports")
	public String showManageMedicalReports() 
	{
		if(session.getAttribute("loggedInAdmin") == null)
		{
			return "redirect:/AdminLogin";
		}
		return "Admin/ManageMedicalReports";
	}
	
	
	
	
	//-----------manage patient-------------------
	
	
	@GetMapping("/ManagePatients")
	public String showManagePatient(Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
		}
		
		List<Users> patientList= userRepo.findAllByRole(UserRole.PATIENT);
		model.addAttribute("patientList", patientList.reversed());
		return "Admin/ManagePatient";
		
	}
	
	@GetMapping("/PatientStatus")
	public String updatePatientStatus(@RequestParam("id") long id,RedirectAttributes attributes) {
		try {
			
			Users patient = userRepo.findById(id).get();
			if(patient.getStatus().equals(UserStatus.PENDING)) {
				patient.setStatus(UserStatus.APPROVED);
				userRepo.save(patient);
				
				sendAutoEmail.SendConfirmationEmail(patient);
			}
			else if(patient.getStatus().equals(UserStatus.APPROVED))
			{
				patient.setStatus(UserStatus.DISABLE);
				userRepo.save(patient);
			}
			else {
				
				patient.setStatus(UserStatus.APPROVED);
				userRepo.save(patient);
			}
			
			
			
			attributes.addFlashAttribute("msg", "user status successfully updated");
			
			return "redirect:/Admin/ManagePatients";
			
		}catch (Exception e) {
			return "redirect:/Admin/ManagePatients";
		}
		
	}
	
	
	//-----------DeletePatient----------------
	
	
	@GetMapping("/DeletePatient")
	public String DeletePatient(@RequestParam("id") long id) 
	{
		userRepo.deleteById(id);
		return "redirect:/Admin/ManagePatients";
	}
	
	
	
	//--------Enquiry--------------------------
	
	
	@GetMapping("/Enquiry")
	public String showEnquiry(Model model) {
		if(session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/AdminLogin";
			
		}
		List<Enquiry> EnquiryList= EnquiryRepo.findAll();
		model.addAttribute("EnquiryList", EnquiryList);
		return "Admin/Enquiry";
		
	}
	
	//----------DeleteEnquiry------------------
	
	@GetMapping("/DeleteEnquiry")
	public String DeleteEnquiry(@RequestParam("id") long id) 
	{
		userRepo.deleteById(id);
		return "redirect:/Admin/Enquiry";
	}
	
	
	
	
	
	
	 //---------Add doctor show page------------
	
	
	@GetMapping("/AddDoctors")
	public String showAddDoctors(Model model) 
	{
		if(session.getAttribute("loggedInAdmin") == null)
		{
			return "redirect:/AdminLogin";
		}
		Users doctor =new Users();
		model.addAttribute("doctor", doctor);
		return "Admin/AddDoctors";
	}
	
	@PostMapping("/AddDoctors")
	public String AddDoctor(@ModelAttribute("doctor") Users doctor,RedirectAttributes attributes) {
		try {
			
			if(userRepo.existsByEmail(doctor.getEmail())) {
				
				attributes.addFlashAttribute("msg","user Already Exists with this Email");
				return "redirect:/Admin/AddDoctors";
			}
			
			doctor.setRole(UserRole.DOCTOR);
			doctor.setStatus(UserStatus.APPROVED);
			doctor.setRegDate(LocalDateTime.now());
			userRepo.save(doctor);
			attributes.addFlashAttribute("msg","Doctor Successfully added");
			
			
			return "redirect:/Admin/AddDoctors";
		}
		catch(Exception e){
			attributes.addFlashAttribute("msg","Error :" +e.getMessage());
			return "redirect:/Admin/AddDoctors";
			
		}
	}
	
	
	//-----------change password--------------
	
		@GetMapping("/ChangePassword")
		public String showChangePassword() 
		{
			if(session.getAttribute("loggedInAdmin") == null)
			{
				return "redirect:/AdminLogin";
			}
			return "Admin/ChangePassword";
		}
		
		@PostMapping("/ChangePassword")
		public String ChangePassword(HttpServletRequest request,RedirectAttributes attributes) {
			try {
				String oldPass = request.getParameter("oldPassword");
				String newPass = request.getParameter("newPassword");
				String confirmPass = request.getParameter("confirmPassword");
				if(! newPass.equals(confirmPass)) {
					attributes.addFlashAttribute("msg", "New Password and Confirm Password are not Same");
					return "redirect:/Admin/ChangePassword";
				}
				
				Users admin = (Users) session.getAttribute("loggedInAdmin");
				
				if(oldPass.equals(admin.getPassword())) {
					 
					admin.setPassword(confirmPass);
					userRepo.save(admin);
					session.removeAttribute("loggedInAdmin");
					attributes.addFlashAttribute("msg","Password successfully Changed");
					return "redirect:/AdminLogin";
					
				}
				else
				{
					attributes.addFlashAttribute("msg","Invalid Old Password");
				}
				
				return "redirect:/Admin/ChangePassword";
			}
			catch(Exception e){
				attributes.addFlashAttribute("msg","Error : "+e.getLocalizedMessage());
				return "redirect:/Admin/ChangePassword";
				
			}
			
		}
	
	
	
	//-------------logout-------------- 
	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes)
	{
		session.removeAttribute("loggenInAdmin");
		attributes.addFlashAttribute("msg", "logged out successfully");
		return "redirect:/AdminLogin";
	}
	
	
	
	
}

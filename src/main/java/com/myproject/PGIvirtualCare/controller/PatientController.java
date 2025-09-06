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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIvirtualCare.Model.Appointment;
import com.myproject.PGIvirtualCare.Model.Appointment.AppointmentStatus;
import com.myproject.PGIvirtualCare.Model.Users;
import com.myproject.PGIvirtualCare.Model.Users.UserRole;
import com.myproject.PGIvirtualCare.Respository.AppointmentRepository;
import com.myproject.PGIvirtualCare.Respository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Patient")
public class PatientController {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AppointmentRepository appointmentRepo;

	@GetMapping("/PatientDashboard")
	public String showPatientDashboard() {

		if (session.getAttribute("loggedInPatient") == null) {
			return "redirect:/Patient";
		}
		return "Patient/PatientDashboard";
	}
	
	
	//View Profile
	@GetMapping("/ViewProfile")
	public String showViewProfile() {

		if (session.getAttribute("loggedInPatient") == null) {
			return "redirect:/Patient";
		}
		return "Patient/ViewProfile";
	}
	
	
	//--------Book Appointment

	@GetMapping("/BookAppointment")
	public String showBookAppointment(Model model, HttpSession session) {
		if (session.getAttribute("loggedInPatient") == null) {
			return "redirect:/PatientLogin";
		}

		List<Users> doctorList = userRepo.findAllByRole(UserRole.DOCTOR);
		model.addAttribute("doctorList", doctorList);
		Appointment appointment = new Appointment();
		model.addAttribute("appointment", appointment);

		return "Patient/BookAppointment";
	}

	@PostMapping("/BookAppointment")
	public String BookAppointment(@ModelAttribute("appointment") Appointment appointment,
			RedirectAttributes attributes) {
		try {

			Users patient = (Users) session.getAttribute("loggedInPatient");
			appointment.setPatient(patient);
			appointment.setDepartment(appointment.getDoctor().getSpecialization());
			appointment.setBookedAt(LocalDateTime.now());
			appointment.setStatus(AppointmentStatus.PENDING);

			appointmentRepo.save(appointment);
			attributes.addFlashAttribute("msg", "Appointment Successfully Booked!");
			return "redirect:/Patient/BookAppointment";
		} catch (Exception e) {

			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Patient/BookAppointment";
		}
	}

	// -------------logout--------------
	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes) {
		session.removeAttribute("loggedInPatient");
		attributes.addFlashAttribute("msg", "logged out successfully");
		return "redirect:/PatientLogin";
	}

	// ViewAppointment
	@GetMapping("/ViewAppointment")
	public String ShowViewAppointment(Model model) {

		if (session.getAttribute("loggedInPatient") == null) {
			return "redirect:/PatientLogin";
		}

		Users patient = (Users) session.getAttribute("loggedInPatient");

		List<Appointment> appointments = appointmentRepo.findAllByPatient(patient);
		model.addAttribute("appointments", appointments);

		return "Patient/ViewAppointment";
	}

	
	
	// -----------change password--------------

	@GetMapping("/ChangePassword")
	public String showChangePassword() {
		if (session.getAttribute("loggedInPatient") == null) {
			return "redirect:/PatientLogin";
		}
		return "Patient/ChangePassword";
	}

	@PostMapping("/ChangePassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes) {
		try {
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass = request.getParameter("confirmPassword");
			if (!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not Same");
				return "redirect:/Admin/ChangePassword";
			}

			Users patient = (Users) session.getAttribute("loggedInPatient");

			if (oldPass.equals(patient.getPassword())) {

				patient.setPassword(confirmPass);
				userRepo.save(patient);
				session.removeAttribute("loggedInPatient");
				attributes.addFlashAttribute("msg", "Password successfully Changed");
				return "redirect:/PatientLogin";

			} else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
			}

			return "redirect:/Patient/ChangePassword";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getLocalizedMessage());
			return "redirect:/Patient/ChangePassword";

		}

	}

}

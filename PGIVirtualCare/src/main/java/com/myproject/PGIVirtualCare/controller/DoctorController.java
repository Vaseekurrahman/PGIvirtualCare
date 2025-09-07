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

import com.myproject.PGIvirtualCare.Model.Appointment;
import com.myproject.PGIvirtualCare.Model.Prescription;
import com.myproject.PGIvirtualCare.Model.Users;
import com.myproject.PGIvirtualCare.Respository.AppointmentRepository;
import com.myproject.PGIvirtualCare.Respository.PrescriptionRepository;
import com.myproject.PGIvirtualCare.Respository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Doctor")
public class DoctorController {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AppointmentRepository appointmentRepo;

	@Autowired
	private PrescriptionRepository prescriptionRepo;

	@GetMapping("/DoctorDashboard")
	public String showDoctorDashboard() {

		if (session.getAttribute("loggedInDoctor") == null) {
			return "redirect:/Doctor";
		}
		return "Doctor/DoctorDashboard";
	}

// Manage Patients

	@GetMapping("/ManagePatients")
	public String showManagePatients() {

		if (session.getAttribute("loggedInDoctor") == null) {
			return "redirect:/Doctor";
		}

		return "Doctor/Managepatients";

	}

	// View Profile

	@GetMapping("ViewProfile")
	public String showViewProfile() {

		if (session.getAttribute("loggedInDoctor") == null) {
			return "redirect:/Doctor";
		}
		return "Doctor/ViewProfile";
	}

	// -------------logout--------------
	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes) {
		session.removeAttribute("loggedInDoctor");
		attributes.addFlashAttribute("msg", "logged out successfully");
		return "redirect:/DoctorLogin";
	}

	// -----------change password--------------

	@GetMapping("/ChangePassword")
	public String showChangePassword() {
		if (session.getAttribute("loggedInDoctor") == null) {
			return "redirect:/DoctorLogin";
		}
		return "Doctor/ChangePassword";
	}

	@PostMapping("/ChangePassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes) {
		try {
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass = request.getParameter("confirmPassword");
			if (!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not Same");
				return "redirect:/Doctor/ChangePassword";
			}

			Users doctor = (Users) session.getAttribute("loggedInDoctor");

			if (oldPass.equals(doctor.getPassword())) {

				doctor.setPassword(confirmPass);
				userRepo.save(doctor);
				session.removeAttribute("loggedInDoctor");
				attributes.addFlashAttribute("msg", "Password successfully Changed");
				return "redirect:/DoctorLogin";

			} else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");
			}

			return "redirect:/Doctor/ChangePassword";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getLocalizedMessage());
			return "redirect:/Doctor/ChangePassword";

		}

	}

	// View Appointments

	@GetMapping("/ViewAppointments")
	public String ShowViewAppointment(Model model) {

		if (session.getAttribute("loggedInDoctor") == null) {
			return "redirect:/DoctorLogin";
		}

		Users doctor = (Users) session.getAttribute("loggedInDoctor");

		List<Appointment> appointments = appointmentRepo.findAllByDoctor(doctor);
		model.addAttribute("appointments", appointments);

		return "Doctor/ViewAppointments";
	}

	// -----WritePrescription

	@GetMapping("/WritePrescription")
	public String showWritePrescription(@RequestParam("id") long id, Model model) {

		if (session.getAttribute("loggedInDoctor") == null) {
			return "redirect:/Doctor";
		}

		Appointment appointment = appointmentRepo.findById(id).get();
		model.addAttribute("appointment", appointment);

		Prescription prescription = new Prescription();
		model.addAttribute("prescription", prescription);

		return "Doctor/WritePrescription";
	}

	@PostMapping("/WritePrescription")
	public String WritePrescription(@ModelAttribute Prescription prescription, RedirectAttributes attributes,
			@RequestParam("appointmentId") long appointmentId) {
		try {

			Appointment appointment = appointmentRepo.findById(appointmentId).get();

			prescription.setAppointment(appointment);

			prescription.setPrescriptionDate(LocalDateTime.now());
			prescriptionRepo.save(prescription);
			attributes.addFlashAttribute("msg", "Prescription successfully submited");

			return "redirect:/Doctor/ViewAppointments";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Doctor/WritePrescription?id='+appointmentId'";
		}
	}
}

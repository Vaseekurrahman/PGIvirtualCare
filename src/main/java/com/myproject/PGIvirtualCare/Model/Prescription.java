package com.myproject.PGIvirtualCare.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Prescription {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  long id;
	
	@Column(nullable = false)
	private String medicationDetails;
	
	@Column(nullable = false,length = 500)
	private String advice;
	
	@Column(nullable = false,length = 500)
	private String diagonsis;
	
	
	
	private LocalDateTime prescriptionDate;
	
	
	
	
	@ManyToOne
	private Appointment appointment;




	public long getId() {
		return id;
	}




	public void setId(long id) {
		this.id = id;
	}




	public String getMedicationDetails() {
		return medicationDetails;
	}




	public void setMedicationDetails(String medicationDetails) {
		this.medicationDetails = medicationDetails;
	}




	public String getAdvice() {
		return advice;
	}




	public void setAdvice(String advice) {
		this.advice = advice;
	}




	public String getDiagonsis() {
		return diagonsis;
	}




	public void setDiagonsis(String diagonsis) {
		this.diagonsis = diagonsis;
	}




	public LocalDateTime getPrescriptionDate() {
		return prescriptionDate;
	}




	public void setPrescriptionDate(LocalDateTime prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}




	public Appointment getAppointment() {
		return appointment;
	}




	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	
	
}

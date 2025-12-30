package com.myproject.PGIvirtualCare.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long id;

	@ManyToOne
	private Users patient;

	@ManyToOne
	private Users doctor;
	private String department;
	private LocalDate date;
	private LocalTime time;
	private LocalDateTime bookedAt;

	@Column(length = 1000)
	private String symtomps;

	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;

	public enum AppointmentStatus {
		PENDING, APPROVED, REJECTED, RESCHEDULED
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Users getPatient() {
		return patient;
	}

	public void setPatient(Users patient) {
		this.patient = patient;
	}

	public Users getDoctor() {
		return doctor;
	}

	public void setDoctor(Users doctor) {
		this.doctor = doctor;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

	public String getSymtomps() {
		return symtomps;
	}

	public void setSymtomps(String symtomps) {
		this.symtomps = symtomps;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

}
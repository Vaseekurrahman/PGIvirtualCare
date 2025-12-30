package com.myproject.PGIvirtualCare.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.myproject.PGIvirtualCare.Model.Appointment;
import com.myproject.PGIvirtualCare.Model.Users;

import jakarta.transaction.Transactional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findAllByPatient(Users patient);

	List<Appointment> findAllByDoctor(Users doctor);

	@Transactional
	@Modifying
	List<Appointment> findAllByDoctorId(long doctorId);
}

package com.myproject.PGIvirtualCare.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIvirtualCare.Model.Appointment;
import com.myproject.PGIvirtualCare.Model.Users;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findAllByPatient(Users patient);

	List<Appointment> findAllByDoctor(Users doctor);

	
}

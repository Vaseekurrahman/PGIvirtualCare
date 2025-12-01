package com.myproject.PGIvirtualCare.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.myproject.PGIvirtualCare.Model.Prescription;

import jakarta.transaction.Transactional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Prescription p WHERE p.appointment.id = :appointmentId")
	void deleteByAppointmentId(long appointmentId);
}

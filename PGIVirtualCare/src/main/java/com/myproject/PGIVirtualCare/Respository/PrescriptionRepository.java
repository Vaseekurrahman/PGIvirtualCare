package com.myproject.PGIvirtualCare.Respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIvirtualCare.Model.Prescription;



public interface PrescriptionRepository  extends JpaRepository<Prescription, Long>{

}

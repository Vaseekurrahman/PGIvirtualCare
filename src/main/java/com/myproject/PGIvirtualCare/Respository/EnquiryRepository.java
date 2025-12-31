package com.myproject.PGIvirtualCare.Respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIvirtualCare.Model.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry,Long> {
	
	

}

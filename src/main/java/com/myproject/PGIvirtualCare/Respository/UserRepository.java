package com.myproject.PGIvirtualCare.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIvirtualCare.Model.Users;
import com.myproject.PGIvirtualCare.Model.Users.UserRole;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email);

	Users findByEmail(String email);

	List<Users> findAllByRole(UserRole patient);
}

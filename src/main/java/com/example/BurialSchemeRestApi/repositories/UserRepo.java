package com.example.BurialSchemeRestApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BurialSchemeRestApi.models.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
	User findByUsername(String username);

}

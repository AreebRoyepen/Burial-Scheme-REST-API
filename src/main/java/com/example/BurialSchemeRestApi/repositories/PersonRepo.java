package com.example.BurialSchemeRestApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BurialSchemeRestApi.models.Person;

public interface PersonRepo extends JpaRepository<Person, Long> {
	
	List<Person> findByName(String name);
	List<Person> findByNameContains(String name);


}

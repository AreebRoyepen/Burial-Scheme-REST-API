package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Dependant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependantRepo extends JpaRepository<Dependant, Long> {
	
	List<Dependant> findByName(String name);
	List<Dependant> findByNameContains(String name);

}

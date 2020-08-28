package com.example.BurialSchemeRestApi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BurialSchemeRestApi.models.Member;

public interface MemberRepo extends JpaRepository<Member, Long> {
	
	List<Member> findByName(String name);
	List<Member> findByNameContains(String name);

}

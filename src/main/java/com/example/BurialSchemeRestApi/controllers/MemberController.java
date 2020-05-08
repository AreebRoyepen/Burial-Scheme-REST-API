package com.example.BurialSchemeRestApi.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.BurialSchemeRestApi.models.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BurialSchemeRestApi.repositories.MemberRepo;

@RestController
@CrossOrigin
public class MemberController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    MemberRepo repo;
	
	@GetMapping("/members")
	public ResponseEntity<?> allMembers() {
		
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("member", repo.findAll());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	@PostMapping("/addMember")
	public ResponseEntity<?> addMember(@RequestBody Member m){
		
		List <Member> members = repo.findAll();
		Map<String, Object> map = new HashMap<String, Object> ();
		
		for(Member x : members) {
			
			if(	x.getName().equals(m.getName()) && x.getSurname().equals(m.getSurname())) {
				
				if( x.getNumber().equals(m.getNumber()) && x.getEmail().equals(m.getEmail())){
				
					map.put("message", "Member already exists");
					return ResponseEntity.status(HttpStatus.OK).body(map);
				
				}else if(x.getNumber().equals(m.getNumber()) || x.getEmail().equals(m.getEmail())) {
				
					map.put("message", "Similar Member already exists");
					return ResponseEntity.status(HttpStatus.OK).body(map);
				
				}			
			}
			
		}
		
		map.put("message", "success");
		map.put("member", repo.save(m));
		return ResponseEntity.status(HttpStatus.OK).body(map);
	
	}

	
	@GetMapping("/memberByName/{name}")
	public ResponseEntity<?> getMemberByName(@PathVariable String name){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("member", repo.findByName(name));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	
	@GetMapping("/memberLikeName/{p}")
	public ResponseEntity<?> memberLikeName(@PathVariable String p){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("person",repo.findByNameContains(p));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	@GetMapping("/memberByID/{id}")
	public ResponseEntity<?> getPersonByID(@PathVariable Long id) {
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("member",repo.findById(id).orElseThrow());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}

	@DeleteMapping("/deleteMember/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable Long id) {
		try {
			repo.deleteById(id);
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "success");
			return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "No such Member");
			logger.error("Trying to delete a member that does not exist");
			return ResponseEntity.status(HttpStatus.OK).body(m);
		}
	}
	
	@PutMapping("/updateMember/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Member p) {
		
		Map<String, Object> m = new HashMap<String, Object> ();
		
		try {
			Member member =repo.findById(id).orElseThrow();

			if (p.getEmail() != null) member.setEmail(p.getEmail());
		  	if (p.getNumber()!= null) member.setNumber(p.getNumber());
		    if (p.getName() != null) member.setName(p.getName());
		    if (p.getSurname() != null) member.setSurname(p.getSurname());
	        
		    m.put("message", "success");
		    m.put("member", repo.save(member));
		    return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			
			logger.error("Trying to update a member that does not exist");
			m.put("message", "No such member");
			return ResponseEntity.status(HttpStatus.OK).body(m);

		}
	}
}

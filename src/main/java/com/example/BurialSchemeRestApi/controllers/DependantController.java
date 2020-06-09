package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.models.Dependant;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Relationship;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.RelationshipRepo;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class DependantController {
	
	Logger logger = LoggerFactory.getLogger(DependantController.class);
	@Autowired
	MemberRepo memberRepo;
	@Autowired
	DependantRepo dependantRepo;
	@Autowired
	RelationshipRepo relationshipRepo;
	@Autowired
	private UtilClass util;
	
	@GetMapping("/dependants")
	public ResponseEntity<?> allMembers() {
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("message", "success");
		m.put("data", dependantRepo.findAll());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}


	
	@PostMapping("/addDependant")
	public ResponseEntity<?> addDependant(@RequestBody Map requestMap){

		String name = null;
		String surname = null;
		String IDNumber = null;
		Date DOB = null;
		boolean child;
		long member;
		long relationship;

		if(requestMap.get("name")!=null){
			name = requestMap.get("name").toString();
		}
		if(requestMap.get("surname") !=null){
			surname = requestMap.get("surname").toString();
		}
		if(requestMap.get("IDNumber") !=null) {
			IDNumber = requestMap.get("IDNumber").toString();
		}
		//TODO see deprecated date
		if(requestMap.get("DOB") !=null) {
			try {
				DOB = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(requestMap.get("DOB").toString()).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if(requestMap.get("child").toString().equalsIgnoreCase("true")){
			child = true;
		}else if(requestMap.get("child").toString().equalsIgnoreCase("false")){
			child = false;
		}else{
			return util.responseUtil("Invalid Request Body");
		}

		if(requestMap.get("member") ==null) {
			return util.responseUtil("Invalid Request Body");
		}else {
			member = Long.parseLong(requestMap.get("member").toString());
		}
		if(requestMap.get("relationship") ==null) {
			return util.responseUtil("Invalid Request Body");
		}else {
			relationship = Long.parseLong(requestMap.get("relationship").toString());
		}

		try{
			Member m = memberRepo.findById(member).orElseThrow();
			try{
				Relationship r = relationshipRepo.findById(relationship).orElseThrow();

				Dependant dependant = new Dependant();
				dependant.setRelationship(r);
				dependant.setMember(m);
				dependant.setDOB(DOB);
				dependant.setChild(child);
				dependant.setIDNumber(IDNumber);
				dependant.setName(name);
				dependant.setSurname(surname);


				List<Dependant> dependants = dependantRepo.findAll();
				Map<String, Object> map = new HashMap<> ();

				for(Dependant x : dependants) {

					if(	x.getName().equals(dependant.getName()) && x.getSurname().equals(dependant.getSurname())) {

						if( x.getDOB().equals(dependant.getDOB())){
							map.put("message", "Similar dependant already exists");
							return ResponseEntity.status(HttpStatus.OK).body(map);

						}else if(x.getMember().equals(dependant.getMember())) {
							map.put("message", "Dependant already added");
							return ResponseEntity.status(HttpStatus.OK).body(map);

						}
					}

				}
				dependantRepo.save(dependant);

				map.put("message", "success");
				map.put("data", dependant);
				return ResponseEntity.status(HttpStatus.OK).body(map);

			}catch (Exception ex){
				logger.error("No such relationship");
				return util.responseUtil("No such relationship");
			}


		}catch (Exception ex){
			logger.error("No such Member");
			return util.responseUtil("No such Member");
		}
	
	}

	
	@GetMapping("/dependantByName/{name}")
	public ResponseEntity<?> getDependantByName(@PathVariable String name){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data", dependantRepo.findByName(name));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	
	@GetMapping("/dependantLikeName/{p}")
	public ResponseEntity<?> dependantLikeName(@PathVariable String p){
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data", dependantRepo.findByNameContains(p));
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}
	
	@GetMapping("/dependantByID/{id}")
	public ResponseEntity<?> getDependantByID(@PathVariable Long id) {
		Map<String, Object> m = new HashMap<String, Object> ();
		m.put("message", "success");
		m.put("data", dependantRepo.findById(id).orElseThrow());
		return ResponseEntity.status(HttpStatus.OK).body(m);
	}

	@DeleteMapping("/deleteDependant/{id}")
	public ResponseEntity<?> deleteDependant(@PathVariable Long id) {
		try {
			Dependant dep = dependantRepo.findById(id).orElseThrow();
			dependantRepo.deleteById(dep.getID());
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "success");
			return ResponseEntity.status(HttpStatus.OK).body(m);
			
		}catch(Exception e) {
			Map<String, String> m = new HashMap<String, String> ();
			m.put("message", "No such Dependant");
			logger.error("Trying to delete a dependant that does not exist");
			return ResponseEntity.status(HttpStatus.OK).body(m);
		}
	}
	
	@PutMapping("/updateDependant/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map requestMap) {

		Map<String, Object> m = new HashMap<> ();

		try {
			Dependant updateDep = dependantRepo.findById(id).orElseThrow();
			try{

				String name = null;
				String surname = null;
				String IDNumber = null;
				Date DOB = null;
				long member;
				long relationship;

				if(requestMap.get("member") !=null) {
					member = Long.parseLong(requestMap.get("member").toString());
					Member mem = memberRepo.findById(member).orElseThrow();
					updateDep.setMember(mem);
				}
				if(requestMap.get("name")!=null){
					name = requestMap.get("name").toString();
					updateDep.setName(name);
				}
				if(requestMap.get("surname") !=null){
					surname = requestMap.get("surname").toString();
					updateDep.setSurname(surname);
				}
				if(requestMap.get("IDNumber") !=null) {
					IDNumber = requestMap.get("IDNumber").toString();
					updateDep.setIDNumber(IDNumber);
				}
				//TODO see deprecated date
				if(requestMap.get("DOB") !=null) {
					DOB = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(requestMap.get("DOB").toString()).getTime());
					updateDep.setDOB(DOB);
				}
				if(requestMap.get("child") != null){
					if(requestMap.get("child").toString().equalsIgnoreCase("true")){
						updateDep.setChild(true);
					}else if(requestMap.get("child").toString().equalsIgnoreCase("false")){
						updateDep.setChild(false);
					}else{
						return util.responseUtil("Invalid Request Body");
					}
				}

				try{
					if(requestMap.get("relationship") !=null) {
						relationship = Long.parseLong(requestMap.get("relationship").toString());
						Relationship r = relationshipRepo.findById(relationship).orElseThrow();
						updateDep.setRelationship(r);
					}

					m.put("message", "success");
					m.put("data", dependantRepo.save(updateDep));
					return ResponseEntity.status(HttpStatus.OK).body(m);


				}catch (Exception ex){
					logger.error("No such relationship");
					return util.responseUtil("No such relationship");
				}

			}catch (Exception ex){
				logger.error("No such Member");
				return util.responseUtil("No such Member");
			}
			
		}catch(Exception e) {
			
			logger.error("Trying to update a dependant that does not exist");
			m.put("message", "No such dependant");
			return ResponseEntity.status(HttpStatus.OK).body(m);

		}
	}
}

package com.example.BurialSchemeRestApi.controllers;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.dto.MemberRequestDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@RestController
@CrossOrigin
@RequestMapping("/v1/members")
public class MemberController {

	MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping()
	public ResponseEntity<ResponseMessageList> allMembers() {
		return new ResponseEntity<>(memberService.allMembers(), HttpStatus.OK);
	}

	@GetMapping("/dependants/{id}")
	public ResponseEntity<?> dependants(@PathVariable Long id){

		try{
			return new ResponseEntity<>(memberService.membersDependants(id),HttpStatus.OK);
		}catch (ValidationException e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping()
	public ResponseEntity<?> addMember(@RequestBody MemberRequestDTO m){
		
		try{
			return new ResponseEntity<>(memberService.addMember(m),HttpStatus.OK);
		}catch (ValidationException e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<?> getMemberByName(@PathVariable String name){
		try{
			return new ResponseEntity<>(memberService.getMemberByName(name),HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/search/{name}")
	public ResponseEntity<?> memberLikeName(@PathVariable String name){
		try{
			return new ResponseEntity<>(memberService.memberLikeName(name),HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getMemberByID(@PathVariable Long id) {
		try{
			return new ResponseEntity<>(memberService.getMemberByID(id),HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable Long id) {
		try{
			return new ResponseEntity<>(memberService.deleteMember(id),HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MemberRequestDTO p) {

		try{
			return new ResponseEntity<>(memberService.update(id, p),HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.BAD_REQUEST);
		}
	}

}

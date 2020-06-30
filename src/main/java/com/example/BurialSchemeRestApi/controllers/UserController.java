package com.example.BurialSchemeRestApi.controllers;

import javax.servlet.http.HttpServletRequest;

import com.example.BurialSchemeRestApi.api.ErrorMessage;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.jwt.JwtRefreshRequest;
import com.example.BurialSchemeRestApi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BurialSchemeRestApi.dto.UserDTO;
import com.example.BurialSchemeRestApi.jwt.JwtRequest;

@RestController
@CrossOrigin
public class UserController {

	UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

		try {
			return new ResponseEntity<>(userService.login(authenticationRequest), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDTO user) {

		try {
			return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(@RequestBody JwtRefreshRequest request) {

		try {
			return new ResponseEntity<>(userService.refresh(request), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<?> users() {

		return new ResponseEntity<>(userService.users(), HttpStatus.OK);

	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO user){

		try {
			return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/changeUserStatus")
	public ResponseEntity<?> userStatus(@RequestBody UserDTO userDTO) {

		try {
			return new ResponseEntity<>(userService.userStatus(userDTO), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(new ErrorMessage(e.getMessage(), ResponseStatus.FAILURE.name()), HttpStatus.UNAUTHORIZED);
		}

	}

}

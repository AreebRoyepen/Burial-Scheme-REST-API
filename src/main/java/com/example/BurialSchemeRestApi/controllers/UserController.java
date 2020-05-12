package com.example.BurialSchemeRestApi.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.BurialSchemeRestApi.util.UtilClass;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.BurialSchemeRestApi.dto.UserDTO;
import com.example.BurialSchemeRestApi.jwt.JwtRequest;
import com.example.BurialSchemeRestApi.jwt.JwtResponse;
import com.example.BurialSchemeRestApi.jwt.JwtTokenUtil;
import com.example.BurialSchemeRestApi.models.Role;
import com.example.BurialSchemeRestApi.models.User;
import com.example.BurialSchemeRestApi.repositories.RoleRepo;
import com.example.BurialSchemeRestApi.repositories.UserRepo;
import com.example.BurialSchemeRestApi.services.JwtUserDetailsService;

@RestController
@CrossOrigin
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UtilClass util;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		User user = userRepo.findByUsername(userDetails.getUsername());

		// check if user is active
		if (!user.isActive())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		final String token = jwtTokenUtil.generateToken(userDetails);

		long exp = expirationTime(jwtTokenUtil.getExpirationDateFromToken(token)) / 1000;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("expiration", exp);
		map.put("token", new JwtResponse(token).getToken());
		map.put("data", user);
		return ResponseEntity.ok(map);

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDTO user) {

		if(user.getName() ==null || user.getSurname()==null || user.getUsername()==null || 
				user.getPassword() ==null || user.getRole() <= 0) {
			if(user.getEmail() == null && user.getNumber() == null)
			return util.responseUtil("Invalid request body");
		}
		
		User newUser = save(user);
		if(newUser == null) {
			return util.responseUtil("No such Role");
		}
		return ResponseEntity.ok(newUser);

	}

	@GetMapping("refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		
		String authToken = request.getHeader("Authorization");
		final String token = authToken.substring(7);
		long exp = expirationTime(jwtTokenUtil.getExpirationDateFromToken(token)) / 1000;

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("expiration", exp);
			map.put("token", new JwtResponse(refreshedToken).getToken());
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<?> users() {

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("message", "success");
		m.put("data", userRepo.findAll());

		return ResponseEntity.status(HttpStatus.OK).body(m);
	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO user){

		Map<String, Object> m = new HashMap<String, Object> ();

		try {
			User theUser =userRepo.findById(id).orElseThrow();
			try {
				Role role = roleRepo.findById(user.getRole()).orElseThrow();

				if (user.getEmail() != null) theUser.setEmail(user.getEmail());
				if (user.getNumber()!= null) theUser.setNumber(user.getNumber());
				if (user.getName() != null) theUser.setName(user.getName());
				if (user.getSurname() != null) theUser.setSurname(user.getSurname());
				if (user.getUsername() != null) theUser.setUsername(user.getUsername());
				if (user.getPassword() != null) theUser.setPassword(user.getPassword());
				if (user.getSurname() != null) theUser.setSurname(user.getSurname());
				if (user.getRole() != 0) theUser.setRole(role);

				m.put("message", "success");
				m.put("data", userRepo.save(theUser));
				return ResponseEntity.status(HttpStatus.OK).body(m);

			}catch (Exception e) {
				logger.error("No such role");
				return util.responseUtil("No such Role");
			}


		}catch(Exception e) {

			logger.error("Trying to update a user that does not exist");
			m.put("message", "No such User");
			return ResponseEntity.status(HttpStatus.OK).body(m);

		}

	}

	@PostMapping("/changeUserStatus")
	public ResponseEntity<?> userStatus(@RequestBody Map<String, Object> map) {

		try {
			
			try {
				User user = userRepo.findById(Long.parseLong(map.get("user").toString())).orElseThrow();
				
				boolean active;
				if (map.get("active").toString().equalsIgnoreCase("true"))
					active = true;
				else if (map.get("active").toString().equalsIgnoreCase("false"))
					active = false;
				else
					throw new Exception("");

				user.setActive(active);
				userRepo.save(user);

				return util.responseUtil("success");
			}catch (Exception e) {
				logger.error("No such user");
				return util.responseUtil("No such user");
			}

		} catch (Exception e) {
			logger.error("Bad request body when changing user status");
			return util.responseUtil("Bad Request Body");
		}

	}

	private long expirationTime(Date date) {

		Calendar calender = Calendar.getInstance();
		calender.setTime(date);

		Calendar cal2 = Calendar.getInstance();

		return (calender.getTimeInMillis() - cal2.getTimeInMillis());

	}

	private User save(UserDTO user) {
		User newUser = new User();
		try {
			Role role = roleRepo.findById(user.getRole()).orElseThrow();

			newUser.setName(user.getName());
			newUser.setSurname(user.getSurname());
			newUser.setUsername(user.getUsername());
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			newUser.setEmail(user.getEmail());
			newUser.setNumber(user.getNumber());
			newUser.setRole(role);
			newUser.setActive(user.isActive());
			return userRepo.save(newUser);
						
		}catch (Exception e) {
			logger.error("No such role");
			return null;
		}

	}



	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			logger.error("user diabled");
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			logger.error("User credentials invalid");
			throw new Exception("INVALID_CREDENTIALS", e);
		}catch(AuthenticationException e) {
			
		}
	}
}

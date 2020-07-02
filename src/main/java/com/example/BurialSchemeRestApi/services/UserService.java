package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.api.UserResponseMessage;
import com.example.BurialSchemeRestApi.dto.UserDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.jwt.JwtRefreshRequest;
import com.example.BurialSchemeRestApi.jwt.JwtRequest;
import com.example.BurialSchemeRestApi.jwt.JwtResponse;
import com.example.BurialSchemeRestApi.jwt.JwtTokenUtil;
import com.example.BurialSchemeRestApi.models.Role;
import com.example.BurialSchemeRestApi.models.User;
import com.example.BurialSchemeRestApi.repositories.RoleRepo;
import com.example.BurialSchemeRestApi.repositories.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@Log4j2
public class UserService {

    private HashMap<String, String> refreshTokens = new HashMap<>();

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;
    private PasswordEncoder bCryptEncoder;
    private UserRepo userRepo;
    private RoleRepo roleRepo;

    public UserService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                       JwtUserDetailsService userDetailsService, PasswordEncoder bCryptEncoder, UserRepo userRepo, RoleRepo roleRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.bCryptEncoder = bCryptEncoder;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public UserResponseMessage login(JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        User user = userRepo.findByUsername(userDetails.getUsername());

        // check if user is active
        if (!user.isActive())
            throw new ValidationException("User does not exist");

        final String token = jwtTokenUtil.generateToken(userDetails, user);

        UUID refreshToken = UUID.randomUUID();

        refreshTokens.put(user.getUsername(),refreshToken.toString());

        return UserResponseMessage.builder().token(new JwtResponse(token).getToken()).refreshToken(refreshToken.toString()).build();

    }

    public Message register(UserDTO user) throws ValidationException {

        if(user.getName() ==null || user.getSurname()==null || user.getUsername()==null ||
                user.getPassword() ==null || user.getRole() <= 0) {
            if(user.getEmail() == null && user.getNumber() == null)
                throw new ValidationException("Invalid request body");
        }

        User newUser = save(user);
        if(newUser == null) {
            throw new ValidationException("No such Role");
        }
        return ResponseMessageObject.builder().data(newUser).build();

    }

    public UserResponseMessage refresh(JwtRefreshRequest request) throws ValidationException {

        String username = request.getUsername();
        String refreshToken = request.getRefreshToken();

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userRepo.findByUsername(userDetails.getUsername());

        if (refreshTokens.get(username).equals(refreshToken)) {

            String token = jwtTokenUtil.generateToken(userDetails, user);

            UUID newRefreshToken = UUID.randomUUID();
            refreshTokens.put(user.getUsername(),newRefreshToken.toString());

            return UserResponseMessage.builder().token(new JwtResponse(token).getToken()).refreshToken(newRefreshToken.toString()).build();

        }else{
            throw new ValidationException("Invalid Token");
        }
    }

    public ResponseMessageList users() {
        return ResponseMessageList.builder().message(ResponseStatus.SUCCESS.name()).data(userRepo.findAll()).build();
    }

    public ResponseMessageObject updateUser(Long id, UserDTO user) throws ValidationException {

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

                return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).data(userRepo.save(theUser)).build();

            }catch (NoSuchElementException e) {
                log.error("No such role");
                throw new ValidationException("No such Role");
            }catch(Exception ex){
                ex.printStackTrace();
                throw new ValidationException(ex.getMessage());
            }

        }catch(NoSuchElementException e) {

            log.error("Trying to update a user that does not exist");
            throw new ValidationException("No such User");

        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }

    }

    public ResponseMessageObject userStatus(UserDTO userDTO) throws ValidationException {

        try {

            try {
                User user = userRepo.findById(userDTO.getId()).orElseThrow();

                user.setActive(userDTO.isActive());
                userRepo.save(user);

                return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).build();
            }catch (NoSuchElementException e) {
                log.error("No such user");
                throw new ValidationException("No such user");
            }catch(Exception ex){
                ex.printStackTrace();
                throw new ValidationException(ex.getMessage());
            }

        } catch (Exception e) {
            log.error("Bad request body when changing user status");
            throw new ValidationException("Bad Request Body");
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
            newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
            newUser.setEmail(user.getEmail());
            newUser.setNumber(user.getNumber());
            newUser.setRole(role);
            newUser.setActive(user.isActive());
            return userRepo.save(newUser);

        }catch (Exception e) {
            log.error("No such role");
            return null;
        }

    }



    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            log.error("user diabled");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            log.error("User credentials invalid");
            throw new Exception("INVALID_CREDENTIALS", e);
        }catch(AuthenticationException e) {
            throw new Exception(e.getMessage());
        }
    }

}

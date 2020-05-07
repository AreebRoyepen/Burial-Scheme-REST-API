package com.example.BurialSchemeRestApi.dto;

public class UserDTO {

	private String name;
	private String surname;
	private String email;
	private String number;
	private String username;
	
	private String password;
	private boolean active;

	private long role;
    
    public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	} 
    	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public long getRole() {
		return role;
	}
	public void setRole(long role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", surname=" + surname + ", email=" + email + ", username="
				+ username + ", password=" + password + ", role=" + role + "]";
	}
	

}

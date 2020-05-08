package com.example.BurialSchemeRestApi.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;
	private String name;
	private String surname;
	private String number;
	private String email;
	private Date DOB;
	private Date DOE;
	private boolean hasDependants;
	private boolean claimed;
	
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date DOB) {
		this.DOB = DOB;
	}

	public Date getDOE() {
		return DOE;
	}

	public void setDOE(Date DOE) {
		this.DOE = DOE;
	}

	public boolean hasDependants() {
		return hasDependants;
	}

	public void setHasDependants(boolean hasDependants) {
		this.hasDependants = hasDependants;
	}

	public boolean hasClaimed() {
		return claimed;
	}

	public void setClaimed(boolean claimed) {
		this.claimed = claimed;
	}

	@Override
	public String toString() {
		return "Person [ID=" + ID + ", name=" + name + ", surname=" + surname + ", number=" + number + ", email="
				+ email + "]";
	}
	
	
	
	
}

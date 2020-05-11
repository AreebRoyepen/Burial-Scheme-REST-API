package com.example.BurialSchemeRestApi.models;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
	private Date DOE = new Date(System.currentTimeMillis());
	private boolean claimed = false;

	@OneToMany(mappedBy="member")
	private Set<Dependant> dependants;
	
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

	public boolean hasClaimed() {
		return claimed;
	}

	public void setClaimed(boolean claimed) {
		this.claimed = claimed;
	}

	public Set<Dependant> getDependants() {
		return dependants;
	}

	public void setDependants(Set<Dependant> dependants) {
		this.dependants = dependants;
	}

	@Override
	public String toString() {
		return "Person [ID=" + ID + ", name=" + name + ", surname=" + surname + ", number=" + number + ", email="
				+ email + "]";
	}
	
	
	
	
}

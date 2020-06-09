package com.example.BurialSchemeRestApi.models;


import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;
	private String name;
	private String surname;
	private String email;
	private String IDNumber;
	private String address;
	private String area;
	private String homeNumber;
	private String cellNumber;
	private String workNumber;
	private Date DOB;
	private Date DOE = new Date(System.currentTimeMillis());
	private boolean claimed = false;
	private boolean paidJoiningFee = false;

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String IDNumber) {
		this.IDNumber = IDNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	public String getCellNumber() {
		return cellNumber;
	}

	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
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

	public boolean isPaidJoiningFee() {
		return paidJoiningFee;
	}

	public void setPaidJoiningFee(boolean paidJoiningFee) {
		this.paidJoiningFee = paidJoiningFee;
	}

	@Override
	public String toString() {
		return "Person [ID=" + ID + ", name=" + name + ", surname=" + surname + ", email="
				+ email + "]";
	}
	
	
	
	
}

package com.example.BurialSchemeRestApi.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
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
	private String postalCode;
	private String homeNumber;
	private String cellNumber;
	private String workNumber;
	private Date DOB;
	private Date DOE = new Date(System.currentTimeMillis());
	private boolean claimed = false;
	private boolean paidJoiningFee = false;

	@OneToMany(mappedBy="member")
	private Set<Dependant> dependants;

}

package com.example.BurialSchemeRestApi.models;

import com.example.BurialSchemeRestApi.dto.DependantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	private boolean paidJoiningFee;

	@OneToMany(mappedBy="member")
	private List<Dependant> dependants;

	public List<Dependant> getDependants() {

		List list = dependants.stream().map(e ->
				DependantDTO.builder().id(e.getID()).child(e.isChild()).DOB(e.getDOB()).DOE(e.getDOE()).IDNumber(e.getIDNumber())
				.member(e.getMember().getID()).name(e.getName()).surname(e.getSurname()).relationship(e.getRelationship()).build())
				.collect(Collectors.toList());

		return list;
	}
}

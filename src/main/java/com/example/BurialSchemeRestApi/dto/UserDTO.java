package com.example.BurialSchemeRestApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

	private long id;
	private String name;
	private String surname;
	private String email;
	private String number;
	private String username;
	private String password;
	private boolean active;
	private long role;

}

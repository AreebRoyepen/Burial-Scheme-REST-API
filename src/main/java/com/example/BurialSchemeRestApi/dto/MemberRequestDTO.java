package com.example.BurialSchemeRestApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class MemberRequestDTO {

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

}

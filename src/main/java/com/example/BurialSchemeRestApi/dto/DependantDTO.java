package com.example.BurialSchemeRestApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DependantDTO {

    String name ;
    String surname ;
    String IDNumber ;
    Date DOB ;
    boolean child;
    @NonNull long member;
    @NonNull long relationship;

}

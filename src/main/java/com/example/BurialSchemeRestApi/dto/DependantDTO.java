package com.example.BurialSchemeRestApi.dto;

import com.example.BurialSchemeRestApi.models.Relationship;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DependantDTO {

    Long id ;
    String name ;
    String surname ;
    String IDNumber ;
    Date DOB ;
    Date DOE;
    boolean child;
    @NonNull long member;
    @NonNull Relationship relationship;

}

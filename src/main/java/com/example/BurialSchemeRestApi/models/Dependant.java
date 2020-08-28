package com.example.BurialSchemeRestApi.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Dependant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String name;
    private String surname;
    private String IDNumber;
    private Date DOB;
    private Date DOE = new Date(System.currentTimeMillis());
    private boolean claimed = false;
    private boolean child;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "memberID" , nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "relationshipID" , nullable = false)
    private Relationship relationship;

}

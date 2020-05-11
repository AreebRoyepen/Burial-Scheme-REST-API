package com.example.BurialSchemeRestApi.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Dependant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String name;
    private String surname;
    private Date DOB;
    private Date DOE = new Date(System.currentTimeMillis());
    private boolean claimed = false;

    @ManyToOne
    @JoinColumn(name = "memberID" , nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "relationshipID" , nullable = false)
    private Relationship relationship;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
}
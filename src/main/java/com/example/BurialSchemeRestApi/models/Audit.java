package com.example.BurialSchemeRestApi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    private boolean claim;
    private boolean premium;
    private boolean expense;
    private boolean income;
    private boolean other;

    private String username;

    private String info;

    private String beforeValue;
    private String afterValue;

    private String member;
    private String dependant;

    private Date timestamp = new Date(System.currentTimeMillis());

}

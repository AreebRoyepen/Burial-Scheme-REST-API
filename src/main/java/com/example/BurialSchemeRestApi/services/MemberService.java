package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.dto.MemberRequestDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Audit;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.repositories.AuditRepo;
import com.example.BurialSchemeRestApi.repositories.DependantRepo;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class MemberService {

    MemberRepo memberRepo;
    DependantRepo dependantRepo;
    AuditRepo auditRepo;

    public MemberService(MemberRepo memberRepo, DependantRepo dependantRepo, AuditRepo auditRepo) {
        this.memberRepo = memberRepo;
        this.dependantRepo = dependantRepo;
        this.auditRepo = auditRepo;
    }

    public ResponseMessageList allMembers() {
        return ResponseMessageList.builder().data(memberRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public ResponseMessageList membersDependants(long id) throws ValidationException {

        try{

            Member member = memberRepo.findById(id).orElseThrow();

            return ResponseMessageList.builder().data(member.getDependants()).message(ResponseStatus.SUCCESS.name()).build();

        }catch (Exception ex){
            log.error("No such Member");
            throw new ValidationException("No such Member");
        }

    }

    public Message addMember(MemberRequestDTO m) throws ValidationException {

        List<Member> members = memberRepo.findAll();

        for(Member x : members) {

            if(	x.getName().equals(m.getName()) && x.getSurname().equals(m.getSurname())) {

                if( x.getCellNumber().equals(m.getCellNumber()) && x.getEmail().equals(m.getEmail())){

                    throw new ValidationException("Member already exists");

                }else if(x.getCellNumber().equals(m.getCellNumber()) || x.getEmail().equals(m.getEmail())) {

                    throw new ValidationException("Similar Member already exists");

                }
            }

        }
        Member member = Member.builder().name(m.getName()).surname(m.getSurname()).IDNumber(m.getIDNumber()).address(m.getAddress())
                .area(m.getArea()).postalCode(m.getPostalCode()).cellNumber(m.getCellNumber()).homeNumber(m.getHomeNumber())
                .workNumber(m.getWorkNumber()).email(m.getEmail()).DOB(m.getDOB()).DOE(m.getDOE()).paidJoiningFee(m.isPaidJoiningFee()).build();
        memberRepo.save(member);
        auditRepo.save(Audit.builder()
                .other(true).info("User added member")
                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .member(member.toString())
                .build());
        return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).build();

    }

    public Message getMemberByName(String name){
        return ResponseMessageList.builder().data(memberRepo.findByName(name)).message(ResponseStatus.SUCCESS.name()).build();
    }


    public Message memberLikeName(String p){
        return ResponseMessageList.builder().data(memberRepo.findByNameContains(p)).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message getMemberByID(Long id) {
        return ResponseMessageObject.builder().data(memberRepo.findById(id).orElseThrow()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message deleteMember(Long id) throws ValidationException {
        try {
            Member m = memberRepo.findById(id).orElseThrow();
            memberRepo.deleteById(id);
            auditRepo.save(Audit.builder()
                    .other(true).info("User deleted member")
                    .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                    .member(m.toString())
                    .build());
            return ResponseMessageList.builder().message(ResponseStatus.SUCCESS.name()).build();

        }catch(NoSuchElementException e) {
            throw new ValidationException("Trying to delete a member that does not exist");
        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }
    }

    public Message update(Long id, MemberRequestDTO p) throws ValidationException {

        try {
            Member member =memberRepo.findById(id).orElseThrow();

            if (p.getName() != null) member.setName(p.getName());
            if (p.getSurname() != null) member.setSurname(p.getSurname());
            if (p.getEmail() != null) member.setEmail(p.getEmail());
            if (p.getAddress() != null) member.setAddress(p.getAddress());
            if (p.getArea() != null) member.setArea(p.getArea());
            if (p.getIDNumber() != null) member.setIDNumber(p.getIDNumber());
            if (p.getPostalCode() != null) member.setPostalCode(p.getPostalCode());
            if (p.getCellNumber()!= null) member.setCellNumber(p.getCellNumber());
            if (p.getHomeNumber()!= null) member.setHomeNumber(p.getHomeNumber());
            if (p.getWorkNumber()!= null) member.setWorkNumber(p.getWorkNumber());
            if(p.getDOB() != null) member.setDOB(p.getDOB());
            member.setPaidJoiningFee(p.isPaidJoiningFee());

            auditRepo.save(Audit.builder()
                    .other(true).info("User edited member")
                    .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                    .beforeValue(p.toString())
                    .afterValue(member.toString())
                    .build());
            return ResponseMessageObject.builder().data(memberRepo.save(member)).message(ResponseStatus.SUCCESS.name()).build();

        }catch(NoSuchElementException e) {

            log.error("Trying to update a member that does not exist");
            throw new ValidationException("No such member");

        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }
    }

}

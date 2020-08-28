package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.dto.ClaimRequestDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.*;
import com.example.BurialSchemeRestApi.repositories.*;
import com.example.BurialSchemeRestApi.util.UtilClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.NoSuchElementException;

@Service
@Log4j2
public class ClaimService {

    MemberRepo memberRepo;
    DependantRepo dependantRepo;
    ClaimRepo claimRepo;
    PremiumRepo premiumRepo;
    TransactionTypeRepo transactionTypeRepo;
    UtilClass util;
    AuditRepo auditRepo;

    public ClaimService(MemberRepo memberRepo, DependantRepo dependantRepo, ClaimRepo claimRepo, PremiumRepo premiumRepo, TransactionTypeRepo transactionTypeRepo, UtilClass util, AuditRepo auditRepo) {
        this.memberRepo = memberRepo;
        this.dependantRepo = dependantRepo;
        this.claimRepo = claimRepo;
        this.premiumRepo = premiumRepo;
        this.transactionTypeRepo = transactionTypeRepo;
        this.util = util;
        this.auditRepo = auditRepo;
    }

    public ResponseMessageList allClaims() {
        return ResponseMessageList.builder().message(ResponseStatus.SUCCESS.name()).data(claimRepo.findAll()).build();
    }

    public Message claim(ClaimRequestDTO claimRequestDTO) throws ValidationException {

        try {
            Member member =memberRepo.findById(claimRequestDTO.getID()).orElseThrow();

            if(member.isClaimed()){
                auditRepo.save(Audit.builder()
                        .claim(true).info("User tried to claim for claimant that already claimed")
                        .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                        .member(member.toString())
                        .build());

                throw new ValidationException("Cannot claim more than once");
            }else{

                try{

                    TransactionType t = transactionTypeRepo.findById(claimRequestDTO.getTransactionType()).orElseThrow();
                    Claim claim = new Claim();

                    BigDecimal total = util.getBalanceAtDate(member, new Date(System.currentTimeMillis())).setScale(2, RoundingMode.HALF_EVEN);

                    if(total.compareTo(BigDecimal.ZERO) == 0){
                        auditRepo.save(Audit.builder()
                                .claim(true).info("User tried to claim for claimant with no claims")
                                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                                .member(member.toString())
                                .build());

                        throw new ValidationException("No funds");
                    }

                    if(total.compareTo(BigDecimal.ZERO) < 0){
                        log.error("member " + member.getID()+ " has negative balance");
                        auditRepo.save(Audit.builder()
                                .claim(true).info("User tried to claim for claimant that already claimed")
                                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                                .member(member.toString())
                                .build());
                        throw new ValidationException("Negative Balance");
                    }

                    if(claimRequestDTO.getAmount().compareTo(total) > 0){
                        auditRepo.save(Audit.builder()
                                .claim(true).info("User tried to claim more than claimants premiums")
                                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                                .member(member.toString())
                                .build());
                        throw new ValidationException("Cannot claim more than total premiums paid");
                    }

                    if(member.getDependants().isEmpty() || util.allDependantsClaimed(member)){
                        claim.setAmount(total);
                        claim.setClaimDate(new Date(System.currentTimeMillis()));
                        claim.setBurialPlace(claimRequestDTO.getBurialPlace());
                        claim.setBuriedDate(claimRequestDTO.getBuriedDate());
                        claim.setDeathDate(claimRequestDTO.getDeathDate());
                        claim.setMember(member);
                        claim.setTransactionType(t);

                        member.setClaimed(true);
                        memberRepo.save(member);

                        auditRepo.save(Audit.builder()
                                .claim(true)
                                .info("Final claimant gets total: R"+total + " despite only needing R" + claimRequestDTO.getAmount())
                                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                                .member(member.toString())
                                .build());

                        return ResponseMessageObject.builder()
                                .info("This is the last claim possible so all funds are paid out: R"+total).
                                message(ResponseStatus.SUCCESS.name()).data(claimRepo.save(claim)).build();
                    }

                    claim.setAmount(claimRequestDTO.getAmount());
                    claim.setClaimDate(new Date(System.currentTimeMillis()));
                    claim.setBurialPlace(claimRequestDTO.getBurialPlace());
                    claim.setBuriedDate(claimRequestDTO.getBuriedDate());
                    claim.setDeathDate(claimRequestDTO.getDeathDate());
                    claim.setMember(member);
                    claim.setTransactionType(t);
                    
                    member.setClaimed(true);
                    memberRepo.save(member);
                    return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).data(claimRepo.save(claim)).build();

                }catch (NoSuchElementException ex){

                    log.error("No such transaction type");
                    throw new ValidationException("No such transaction type");

                }catch (Exception ex){
                    ex.printStackTrace();
                    throw new ValidationException(ex.getMessage());
                }

            }

        }catch(NoSuchElementException e) {

            log.error("Trying to claim from a member that does not exist");
            throw new ValidationException("No such member");

        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }
    }


    public ResponseMessageObject claimForDep(ClaimRequestDTO claimRequestDTO) throws ValidationException {

        try{
            Dependant dep = dependantRepo.findById(claimRequestDTO.getID()).orElseThrow();
            if(dep.isClaimed()){
                return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).info("Cannot claim more than once").build();
            }else{

                Member member = memberRepo.findById(dep.getMember().getID()).orElseThrow();
                try{

                    TransactionType t = transactionTypeRepo.findById(claimRequestDTO.getTransactionType()).orElseThrow();
                    Claim claim = new Claim();

                    BigDecimal total = util.getBalanceAtDate(member, new Date(System.currentTimeMillis())).setScale(2, RoundingMode.HALF_EVEN);

                    if(claimRequestDTO.getAmount().compareTo(total) > 0){
                        auditRepo.save(Audit.builder()
                                .claim(true).info("User tried to claim for than claimants premiums")
                                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                                .dependant(dep.toString())
                                .build());
                        throw new ValidationException("Cannot claim more than total premiums paid");
                    }

                    if((member.getDependants().size() == 1 || util.lastDependantToClaim(member)) && member.isClaimed()){
                        claim.setAmount(total);
                        claim.setClaimDate(new Date(System.currentTimeMillis()));
                        claim.setBurialPlace(claimRequestDTO.getBurialPlace());
                        claim.setBuriedDate(claimRequestDTO.getBuriedDate());
                        claim.setDeathDate((claimRequestDTO.getDeathDate()));
                        claim.setDependant(dep);
                        claim.setTransactionType(t);

                        dep.setClaimed(true);
                        dependantRepo.save(dep);

                        auditRepo.save(Audit.builder()
                                .claim(true)
                                .info("Final claimant gets total: R"+total + " despite only needing R" + claimRequestDTO.getAmount())
                                .username(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                                .dependant(dep.toString())
                                .build());

                        return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).data(claimRepo.save(claim)).
                                info("This is the last claim possible so all funds are paid out").build();
                    }

                    claim.setAmount(claimRequestDTO.getAmount());
                    claim.setClaimDate(new Date(System.currentTimeMillis()));
                    claim.setBurialPlace(claimRequestDTO.getBurialPlace());
                    claim.setBuriedDate(claimRequestDTO.getBuriedDate());
                    claim.setDeathDate(claimRequestDTO.getDeathDate());
                    claim.setDependant(dep);
                    claim.setTransactionType(t);
                    
                    dep.setClaimed(true);
                    dependantRepo.save(dep);
                    return ResponseMessageObject.builder().message(ResponseStatus.SUCCESS.name()).data(claimRepo.save(claim)).build();

                }catch (NoSuchElementException ex){

                    log.error("No such transaction type");
                    throw new ValidationException("No such transaction type");

                }catch (Exception ex){
                    ex.printStackTrace();
                    throw new ValidationException(ex.getMessage());
                }

            }

        }catch (NoSuchElementException ex){
            log.error("Trying to claim from a dependant that does not exist");
            throw new ValidationException("No such dependant");

        }catch (Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }

    }

}

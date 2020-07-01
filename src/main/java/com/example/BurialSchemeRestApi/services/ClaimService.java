package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.dto.ClaimRequestDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Dependant;
import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.*;
import com.example.BurialSchemeRestApi.util.UtilClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.NoSuchElementException;

@Service
public class ClaimService {

    Logger logger = LoggerFactory.getLogger(ClaimService.class);

    MemberRepo memberRepo;
    DependantRepo dependantRepo;
    ClaimRepo claimRepo;
    PremiumRepo premiumRepo;
    TransactionTypeRepo transactionTypeRepo;
    UtilClass util;

    public ClaimService(MemberRepo memberRepo, DependantRepo dependantRepo, ClaimRepo claimRepo, PremiumRepo premiumRepo,
                        TransactionTypeRepo transactionTypeRepo, UtilClass util){
        this.memberRepo = memberRepo;
        this.dependantRepo = dependantRepo;
        this.claimRepo = claimRepo;
        this.premiumRepo = premiumRepo;
        this.transactionTypeRepo = transactionTypeRepo;
        this.util = util;
    }

    public ResponseMessageList allClaims() {
        return ResponseMessageList.builder().message(ResponseStatus.SUCCESS.name()).data(claimRepo.findAll()).build();
    }

    public Message claim(ClaimRequestDTO claimRequestDTO) throws ValidationException {

        try {
            Member member =memberRepo.findById(claimRequestDTO.getID()).orElseThrow();

            if(member.isClaimed()){
                throw new ValidationException("Cannot claim more than once");
            }else{

                try{

                    TransactionType t = transactionTypeRepo.findById(claimRequestDTO.getTransactionType()).orElseThrow();
                    Claim claim = new Claim();

                    BigDecimal total = util.getBalanceAtDate(member, new Date(System.currentTimeMillis())).setScale(2, RoundingMode.HALF_EVEN);

                    if(total.compareTo(BigDecimal.ZERO) == 0){
                        throw new ValidationException("No funds");
                    }

                    if(total.compareTo(BigDecimal.ZERO) < 0){
                        logger.error("member " + member.getID()+ " has negative balance");
                        throw new ValidationException("Negative Balance");
                    }

                    if(claimRequestDTO.getAmount().compareTo(total) > 0){
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

                        return ResponseMessageObject.builder().info("This is the last claim possible so all funds are paid out").
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

                    logger.error("No such transaction type");
                    throw new ValidationException("No such transaction type");

                }catch (Exception ex){
                    ex.printStackTrace();
                    throw new ValidationException(ex.getMessage());
                }

            }

        }catch(NoSuchElementException e) {

            logger.error("Trying to claim from a member that does not exist");
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

                    logger.error("No such transaction type");
                    throw new ValidationException("No such transaction type");

                }catch (Exception ex){
                    ex.printStackTrace();
                    throw new ValidationException(ex.getMessage());
                }

            }

        }catch (NoSuchElementException ex){
            logger.error("Trying to claim from a dependant that does not exist");
            throw new ValidationException("No such dependant");

        }catch (Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }

    }

}

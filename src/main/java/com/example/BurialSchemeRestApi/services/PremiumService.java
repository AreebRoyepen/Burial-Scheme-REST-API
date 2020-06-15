package com.example.BurialSchemeRestApi.services;

import com.example.BurialSchemeRestApi.api.Message;
import com.example.BurialSchemeRestApi.api.ResponseMessageList;
import com.example.BurialSchemeRestApi.api.ResponseMessageObject;
import com.example.BurialSchemeRestApi.dto.PremiumDTO;
import com.example.BurialSchemeRestApi.enums.ResponseStatus;
import com.example.BurialSchemeRestApi.exception.ValidationException;
import com.example.BurialSchemeRestApi.models.Premium;
import com.example.BurialSchemeRestApi.models.TransactionType;
import com.example.BurialSchemeRestApi.repositories.MemberRepo;
import com.example.BurialSchemeRestApi.repositories.PremiumRepo;
import com.example.BurialSchemeRestApi.repositories.TransactionTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class PremiumService {

    Logger logger = LoggerFactory.getLogger(PremiumService.class);

    PremiumRepo premiumRepo;
    MemberRepo memberRepo;
    TransactionTypeRepo transactionTypeRepo;

    public PremiumService(PremiumRepo premiumRepo, MemberRepo memberRepo, TransactionTypeRepo transactionTypeRepo) {
        this.premiumRepo = premiumRepo;
        this.memberRepo = memberRepo;
        this.transactionTypeRepo = transactionTypeRepo;
    }

    public Message allPremiums() {
        return ResponseMessageList.builder().data(premiumRepo.findAll()).message(ResponseStatus.SUCCESS.name()).build();
    }

    public Message addPremium(PremiumDTO premiumDTO) throws ValidationException {

        try{

            TransactionType t = transactionTypeRepo.findById(premiumDTO.getType()).orElseThrow();

            try {
                Premium premium = new Premium();
                premium.setAmount(premiumDTO.getAmount());
                premium.setMember(memberRepo.findById(premiumDTO.getId()).orElseThrow());
                premium.setTransactionType(t);

                return ResponseMessageObject.builder().data(premiumRepo.save(premium)).message(ResponseStatus.SUCCESS.name()).build();

            }catch (NoSuchElementException ex){
                logger.error("No such Member");
                throw new ValidationException("No such Member");
            }catch(Exception ex){
                ex.printStackTrace();
                throw new ValidationException(ex.getMessage());
            }


        }catch (NoSuchElementException ex){

            logger.error("No such transaction type");
            throw new ValidationException("No such transaction type");

        }catch(Exception ex){
            ex.printStackTrace();
            throw new ValidationException(ex.getMessage());
        }


    }

}

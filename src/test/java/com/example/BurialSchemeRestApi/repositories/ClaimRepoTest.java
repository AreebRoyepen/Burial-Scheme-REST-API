package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class ClaimRepoTest {

    @Autowired
    ClaimRepo claimRepo;
    @Autowired
    MemberRepo memberRepo;

    @Test
    void findAllByMember() {


    }

    @Test
    void findByMemberAndClaimDateBefore_hasNoClaims() throws ParseException {

        List<Claim> list = claimRepo.findByMemberAndClaimDateLessThanEqual(memberRepo.findById(1L).orElseThrow(),
                new Date(new SimpleDateFormat("dd/MM/yyyy").parse("04/05/2020").getTime()));

        System.out.println(list.size());
        list.forEach(e-> System.out.println(e));

    }

    @Test
    void findByMemberAndClaimDateBefore_hasClaims() throws ParseException {

        System.out.println(memberRepo.findById(1L).toString());
        List<Claim> list = claimRepo.findByMemberAndClaimDateLessThanEqual(memberRepo.findById(1L).orElseThrow(),
                new Date(new SimpleDateFormat("dd/MM/yyyy").parse("06/05/2020").getTime()));

        System.out.println(list.size());
        list.forEach(e-> System.out.println(e));

    }

    @Test
    void findByMemberAndClaimDateAfter() throws ParseException {

        System.out.println(memberRepo.findById(1L).toString());
        List<Claim> list = claimRepo.findByMemberAndClaimDateAfter(memberRepo.findById(1L).orElseThrow(),
                new Date(new SimpleDateFormat("dd/MM/yyyy").parse("06/05/2020").getTime()));

        System.out.println(list.size());
        list.forEach(e-> System.out.println(e));

    }
}
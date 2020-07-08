package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Premium;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Disabled
class PremiumRepoTest {

    @Autowired
    PremiumRepo premiumRepo;
    @Autowired
    MemberRepo memberRepo;

    @Test
    void findByMemberAndDateBefore() throws ParseException {
        System.out.println(premiumRepo.findById(1L).toString());
        List<Premium> list = premiumRepo.findByMemberAndDateLessThanEqual(memberRepo.findById(1L).orElseThrow(),
                new Date(new SimpleDateFormat("dd/MM/yyyy").parse("06/05/2020").getTime()));

        System.out.println(list.size());
        list.forEach(e-> System.out.println(e));
    }

    @Test
    void findAllByMember() {
    }

    @Test
    void findByMemberAndDateAfter() throws ParseException {

        List<Premium> list = premiumRepo.findByMemberAndDateAfter(memberRepo.findById(1L).orElseThrow(),
                new Date(new SimpleDateFormat("dd/MM/yyyy").parse("02/04/2020").getTime()));

        System.out.println(list.size());
        list.forEach(e-> System.out.println(e));

    }

}
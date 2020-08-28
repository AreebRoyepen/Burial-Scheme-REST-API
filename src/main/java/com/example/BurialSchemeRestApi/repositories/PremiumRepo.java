package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Premium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface PremiumRepo extends JpaRepository<Premium, Long> {

    List<Premium> findByMemberAndDateLessThanEqual(Member member, Date date);
    List<Premium> findByMemberAndDateAfter(Member member, Date date);
    List<Premium> findAllByMember(Member member);

}

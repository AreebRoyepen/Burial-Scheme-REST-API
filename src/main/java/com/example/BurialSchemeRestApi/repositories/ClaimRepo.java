package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Claim;
import com.example.BurialSchemeRestApi.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface ClaimRepo extends JpaRepository<Claim, Long> {

    List<Claim> findAllByMember(Member member);

    List<Claim> findByMemberAndClaimDateLessThanEqual(Member member, Date claimDate);

    List<Claim> findByMemberAndClaimDateAfter(Member member, Date claimDate);

    List<Claim> findAllByOrderByBuriedDate();

    List<Claim> findAllByOrderByClaimDate();

    List<Claim> findAllByOrderByDeathDate();

}

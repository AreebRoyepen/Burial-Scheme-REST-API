package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Member;
import com.example.BurialSchemeRestApi.models.Premium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PremiumRepo extends JpaRepository<Premium, Long> {

    List<Premium> findByMember(Member member);
}

package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepo extends JpaRepository<Claim, Long> {
}

package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Premium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumRepo extends JpaRepository<Premium, Long> {
}

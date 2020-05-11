package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepo extends JpaRepository<Income, Long> {
}

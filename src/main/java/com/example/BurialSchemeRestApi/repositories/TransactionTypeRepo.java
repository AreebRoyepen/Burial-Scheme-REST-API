package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepo extends JpaRepository<TransactionType, Long> {
}

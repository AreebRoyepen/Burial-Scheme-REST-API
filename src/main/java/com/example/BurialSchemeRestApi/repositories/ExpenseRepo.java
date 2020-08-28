package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<Expense, Long> {
}

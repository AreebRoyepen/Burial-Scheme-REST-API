package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepo extends JpaRepository<Audit, Long> {

}

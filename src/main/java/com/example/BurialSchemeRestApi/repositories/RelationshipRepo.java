package com.example.BurialSchemeRestApi.repositories;

import com.example.BurialSchemeRestApi.models.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepo extends JpaRepository<Relationship, Long> {
}

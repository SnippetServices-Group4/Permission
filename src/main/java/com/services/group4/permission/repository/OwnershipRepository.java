package com.services.group4.permission.repository;

import com.services.group4.permission.model.Ownership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {
}

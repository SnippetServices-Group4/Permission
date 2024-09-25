package com.services.group4.permission.repository;

import com.services.group4.permission.model.Ownership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {
  Optional<Ownership> findOwnershipByUser_Username(String username);

}

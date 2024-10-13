package com.services.group4.permission.repository;

import com.services.group4.permission.model.Ownership;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {
  Optional<Ownership> findOwnershipByUserId(Long userId);

  Optional<Ownership> findOwnershipBySnippetId(Long snippetId);
}

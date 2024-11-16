package com.services.group4.permission.repository;

import com.services.group4.permission.model.Ownership;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {
  Optional<Ownership> findOwnershipByUserId(String userId);

  Optional<Ownership> findOwnershipBySnippetId(Long snippetId);

  Optional<Ownership> findOwnershipByUserIdAndSnippetId(String userId, Long snippetId);

  @Query("SELECT o.snippetId FROM Ownership o WHERE o.userId = :userId")
  Optional<List<Long>> findSnippetIdsByUserId(String userId);
}

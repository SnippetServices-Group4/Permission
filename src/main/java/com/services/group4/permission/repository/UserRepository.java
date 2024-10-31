package com.services.group4.permission.repository;

import com.services.group4.permission.model.SnippetUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SnippetUser, Long> {
  Optional<SnippetUser> findByUsername(String username);

  List<SnippetUser> findByUsernameContaining(String username);
}

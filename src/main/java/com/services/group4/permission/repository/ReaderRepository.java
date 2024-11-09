package com.services.group4.permission.repository;

import com.services.group4.permission.model.Reader;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
  Optional<Reader> findReaderByUserId(Long userId);

  Optional<Reader> findReaderBySnippetId(Long snippetId);

  Optional<Object> findReaderByUserIdAndSnippetId(Long userId, Long snippetId);

  @Query("SELECT r.snippetId FROM Reader r WHERE r.userId = :userId")
  Optional<List<Long>> findSnippetIdByUserId(Long userId);
}

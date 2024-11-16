package com.services.group4.permission.repository;

import com.services.group4.permission.model.Reader;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
  Optional<Reader> findReaderByUserId(String userId);

  Optional<Reader> findReaderBySnippetId(Long snippetId);

  Optional<Object> findReaderByUserIdAndSnippetId(String userId, Long snippetId);

  @Query("SELECT r.snippetId FROM Reader r WHERE r.userId = :userId")
  Optional<List<Long>> findSnippetIdByUserId(String userId);

  @Query("SELECT r FROM Reader r WHERE r.snippetId = :snippetId")
  Optional<List<Reader>> findReadersBySnippetId(Long snippetId);
}

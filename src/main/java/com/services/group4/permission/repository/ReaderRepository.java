package com.services.group4.permission.repository;

import com.services.group4.permission.model.Ownership;
import com.services.group4.permission.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
  Optional<Reader> findReaderByUserId(Long userId);
  Optional<Reader> findReaderBySnippetId(Long snippetId);
}

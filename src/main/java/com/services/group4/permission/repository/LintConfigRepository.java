package com.services.group4.permission.repository;

import com.services.group4.permission.model.LintConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LintConfigRepository extends JpaRepository<LintConfig, Long> {
  Optional<LintConfig> findLintConfigByUserId(Long userId);
}

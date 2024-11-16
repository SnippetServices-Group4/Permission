package com.services.group4.permission.repository;

import com.services.group4.permission.model.LintConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LintConfigRepository extends JpaRepository<LintConfig, Long> {
  Optional<LintConfig> findLintConfigByUserId(String userId);
}

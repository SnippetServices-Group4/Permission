package com.services.group4.permission.repository;

import com.services.group4.permission.model.FormatConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatConfigRepository extends JpaRepository<FormatConfig, Long> {
  Optional<FormatConfig> findFormatConfigByUserId(String userId);
}

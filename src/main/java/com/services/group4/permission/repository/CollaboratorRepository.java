// CollaboratorRepository.java
package com.services.group4.permission.repository;

import com.services.group4.permission.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
}
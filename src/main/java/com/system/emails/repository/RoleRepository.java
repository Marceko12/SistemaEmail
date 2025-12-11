package com.system.emails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.emails.model.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByNombre(String Nombre);
    
}

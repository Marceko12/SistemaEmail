package com.system.emails.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.emails.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByNombre(String nombre);   
    List<UserEntity> findDistinctByRolesNombre(String nombre);
        Optional<UserEntity> findByEmail(String email);

}
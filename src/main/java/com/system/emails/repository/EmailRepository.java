package com.system.emails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.emails.model.EmailEntity;
import java.util.List;


public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
     List<EmailEntity> findByEmisor_Email(String email);

    // Buscar por destinatario
    List<EmailEntity> findByDestinatarios_Email(String email);

    
} 

package com.system.emails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.emails.model.EmailEntity;
import java.util.List;


public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    // Listar correos enviados
    List<EmailEntity> findBySender(String sender);
    
    // listar correos recibidos 
    List<EmailEntity> findByRecipient(String recipient);

    
} 

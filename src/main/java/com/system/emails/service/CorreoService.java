package com.system.emails.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import com.system.emails.model.EmailEntity;
import com.system.emails.model.UserEntity;
import com.system.emails.model.dto.CorreoDto;
import com.system.emails.model.dto.CorreoForm;
import com.system.emails.repository.EmailRepository;
import com.system.emails.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CorreoService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final UserRepository userRepo;  // Repositorio para UserEntity

    // Lógica para envío simple de correo y guardado en BD
   public EmailEntity sendEmail(String to, String subject, String message) {
        EmailEntity email = new EmailEntity();
        email.setSubject(subject);
        email.setMessage(message);
        email.setSentAt(LocalDateTime.now());
        email.setStatus("PENDIENTE");
        // No uses setRecipient porque no existe

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(message);

            javaMailSender.send(mail);
            email.setStatus("ENVIADO");

        } catch (Exception e) {
            email.setStatus("ERROR");
        }

        return emailRepository.save(email);
    }


    // Método para guardar correo con relación a usuarios y múltiples destinatarios
    
 public void guardarCorreo(CorreoForm form) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
        throw new RuntimeException("No hay usuario autenticado");
    }

    // Spring Security devuelve NOMBRE, no email
    String nombreUsuario = auth.getName();

    // Buscar emisor por nombre
    UserEntity emisor = userRepo.findByNombre(nombreUsuario)
            .orElseThrow(() -> new RuntimeException("El usuario autenticado no existe"));

    // Buscar destinatario por email
    UserEntity destinatario = userRepo.findByEmail(form.getPara())
            .orElseThrow(() -> new RuntimeException("El destinatario no existe"));

    EmailEntity email = new EmailEntity();
    email.setEmisor(emisor);
    email.getDestinatarios().add(destinatario);
    email.setSubject(form.getAsunto());
    email.setMessage(form.getMensaje());
    email.setSentAt(LocalDateTime.now());
    email.setStatus("ENVIADO");

    emailRepository.save(email);
}









    // Otros métodos que tenías para listar correos

    public List<EmailEntity> getSentEmail(String sender){
        return emailRepository.findByEmisor_Email(sender);
    }

    public List<EmailEntity> getReceivedEmail(String recipient){
        return emailRepository.findByDestinatarios_Email(recipient);
    }

}

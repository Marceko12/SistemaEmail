package com.system.emails.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.system.emails.model.EmailEntity;
import com.system.emails.model.dto.CorreoDto;
import com.system.emails.repository.EmailRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CorreoService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailRepository repoEmail;


    // logica para el envio de correo electronico desde formulario
    public EmailEntity sendEmail(String to, String subject, String message){

        EmailEntity email = new EmailEntity();
        email.setRecipient(to);
        email.setSubject(subject);
        email.setMessage(message);
        email.setSentAt(LocalDateTime.now());
    

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

        return repoEmail.save(email);
    
    
    }






    public void enviarCorreo(CorreoDto correoDto) {
        SimpleMailMessage mensaje = new SimpleMailMessage();

        if (correoDto.getDestinatario() == null || correoDto.getDestinatario().isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacío");
        }

        mensaje.setTo(correoDto.getDestinatario());
        mensaje.setSubject(correoDto.getAsunto());
        mensaje.setText(correoDto.getMensaje());

        if (correoDto.getCc() != null && !correoDto.getCc().isEmpty()) {
            mensaje.setCc(correoDto.getCc());
        }
        if (correoDto.getBcc() != null && !correoDto.getBcc().isEmpty()) {
            mensaje.setBcc(correoDto.getBcc());
        }

        try {
            javaMailSender.send(mensaje);

            // Guardar email en BD
            EmailEntity email = new EmailEntity();
            email.setRecipient(correoDto.getDestinatario());
            email.setSubject(correoDto.getAsunto());
            email.setMessage(correoDto.getMensaje());
            email.setSentAt(LocalDateTime.now());
            email.setStatus("ENVIADO");

            // Si tienes un campo sender (quién envía), asegúrate de setearlo también:
            email.setSender("testuser@example.com"); // o el email real si tienes sesión

            repoEmail.save(email);

            System.out.println("Correo enviado y guardado");
        } catch (MailException e) {
            System.out.println("Error al enviar correo " + e.getMessage());
        }
    }



    // LOGICA PARA LISTAR CORREOS ENVADOS Y RECIBIDOS
    public List<EmailEntity> getSentEmail(String sender){
        return repoEmail.findBySender(sender);
    }


    public List<EmailEntity> getReceivedEmail(String recipient){
        return repoEmail.findByRecipient(recipient);
    }
    
    
}

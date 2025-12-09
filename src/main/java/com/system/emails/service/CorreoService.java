package com.system.emails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.system.emails.model.CorreoDto;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreo(CorreoDto correoDto){
        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(correoDto.getDestinatario());
        mensaje.setSubject(correoDto.getAsunto());
        mensaje.setText(correoDto.getMensaje());


        if(correoDto.getCc() !=null && !correoDto.getCc().isEmpty()){

            mensaje.setCc(correoDto.getCc());

        }

        if(correoDto.getBcc() !=null && !correoDto.getBcc().isEmpty()){
            mensaje.setBcc(correoDto.getBcc());
        }


        try {
            javaMailSender.send(mensaje);
            System.out.println("Correo enviado");
        } catch (MailException e) {
            System.out.println("error al enviar correo " + e.getMessage() );

        }
    }
    
    
}

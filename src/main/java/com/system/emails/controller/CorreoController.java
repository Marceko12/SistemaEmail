package com.system.emails.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.system.emails.model.dto.CorreoDto;
import com.system.emails.service.CorreoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class CorreoController {

    @Autowired
    private CorreoService correoService;

    @GetMapping("/correo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("correo", new CorreoDto());
        return "formulario-correo";
    }

    @GetMapping("/email")
    public String showEmailForm(){
        return "email-form";
    }


      @PostMapping("/email/send")
        public String sendEmail(
                @RequestParam String to,
                @RequestParam String subject,
                @RequestParam String message,
                Model model) {

            correoService.sendEmail(to, subject, message);

            model.addAttribute("msg", "Correo enviado correctamente");

            return "email-form";
    }



    @PostMapping("/correo/enviar")
    public String enviarCorreo(CorreoDto correoDto, Model model) {
        correoService.enviarCorreo(correoDto);
        model.addAttribute("mensaje", "Correo enviado correctamente");
        return "formulario-correo";
    }

    @GetMapping("/emails/sent")
    public String sentEmail(Model model, Principal principal) {
        String userEmail = (principal != null) ? principal.getName() : "testuser@example.com";
        model.addAttribute("emails", correoService.getSentEmail(userEmail));
        return "emails-sent";
    }

    @GetMapping("/emails/received")
    public String receivedEmail(Model model, Principal principal) {
        String userEmail = (principal != null) ? principal.getName() : "testuser@example.com";
        model.addAttribute("emails", correoService.getReceivedEmail(userEmail));
        return "email-received";
    }






}

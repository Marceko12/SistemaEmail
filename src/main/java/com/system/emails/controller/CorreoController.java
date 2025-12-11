package com.system.emails.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.system.emails.model.EmailEntity;
import com.system.emails.model.UserEntity;
import com.system.emails.model.dto.CorreoDto;
import com.system.emails.model.dto.CorreoForm;
import com.system.emails.repository.EmailRepository;
import com.system.emails.repository.UserRepository;
import com.system.emails.service.CorreoService;
import com.system.emails.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class CorreoController {

    @Autowired
    private CorreoService correoService;
    private final UserService usuarioService; // servicio para obtener usuarios
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailRepository correoRepo;
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



    @Autowired
private JavaMailSender mailSender;

    @PostMapping("/correo/enviar-multiples")
    public String enviarCorreoMultiples(
            @RequestParam(value = "destinatariosIds", required = false) List<Long> destinatariosIds,
            @RequestParam String asunto,
            @RequestParam String mensaje,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorEnvio", "Debes iniciar sesión para enviar correos.");
            return "redirect:/dashboard";
        }

        try {
            UserEntity emisor = userRepo.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("No se encontró el usuario logeado"));

            List<UserEntity> destinatarios = (destinatariosIds != null)
                    ? userRepo.findAllById(destinatariosIds)
                    : List.of();

            correoService.enviarCorreoMultiple(emisor, destinatarios, asunto, mensaje);

            redirectAttributes.addFlashAttribute("exitoEnvio", "Correos enviados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorEnvio", e.getMessage());
        }

        return "redirect:/dashboard";
    }
  @GetMapping("/correo/enviar")
    public String mostrarFormulario(Model model, HttpServletRequest request) {
        // Agregar token CSRF al modelo para Thymeleaf
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", token);

        // Agregar un objeto vacío para el formulario
        model.addAttribute("correoForm", new CorreoForm());

        return "correo-enviar";
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

     @GetMapping("/nuevo")
    public String mostrarFormulario(Model model, Principal principal) {
        List<UserEntity> clientes = usuarioService.findAllUsersByRole("CLIENTE");
        model.addAttribute("clientes", clientes);

        List<UserEntity> emisores = usuarioService.findAllUsersByRole("ADMIN");
        model.addAttribute("emisores", emisores);

        UserEntity adminLogueado = usuarioService.findByUsername(principal.getName());
        model.addAttribute("emisorDefault", adminLogueado);

        model.addAttribute("correoForm", new CorreoForm());

        return "correo_form";
    }

    @PostMapping("/nuevo")
    public String procesarFormulario(@ModelAttribute CorreoForm correoForm, Model model) {
        correoService.guardarCorreo(correoForm);

        model.addAttribute("mensaje", "Correo enviado correctamente");
        return "redirect:/correos/nuevo";
    }
}







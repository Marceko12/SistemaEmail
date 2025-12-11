package com.system.emails.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.system.emails.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register-admin")
    public String mostrarFormulario() {
        return "register-admin";  // nombre del archivo HTML sin extensión
    }

    @PostMapping("/register")
    public String registrarAdmin(@RequestParam String nombre,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 Model model) {
        try {
            userService.registrarAdmin(nombre, email, password);
            model.addAttribute("mensaje", "Usuario admin registrado correctamente");
            return "register-admin";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario admin: " + e.getMessage());
            return "register-admin";
        }
    }
}

    


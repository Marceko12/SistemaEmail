package com.system.emails.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.system.emails.model.UserEntity;
import com.system.emails.service.UserService;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @GetMapping("/users/clients")
    public String showClients(Model model){


        List<UserEntity> clientes = userService.findAllUsersByRole("CLIENTE");
        model.addAttribute("clientes", clientes);

        return "clientes";
    }


    @GetMapping("/users/register")
    public String showRegister(){
        return "register";
    }


    @PostMapping("/users/register")
    public String RegisterUser(@RequestParam String nombre,
        @RequestParam String email,
        @RequestParam String password,
    Model model){
        userService.newUser(nombre, email, password);
        model.addAttribute("msg", "Cliente registrado correctamente.");
        
        return "register";

    }


     @PostMapping("/create-admin")
    public String createAdmin() {
        userService.crearUsuarioAdmin("admin", "12345");
        return "Admin creado";
    }

    
    
}

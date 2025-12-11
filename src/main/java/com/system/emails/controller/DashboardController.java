package com.system.emails.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.system.emails.model.UserEntity;
import com.system.emails.model.dto.CorreoDto;
import com.system.emails.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class DashboardController {


    private final UserService userService;
    @GetMapping("/dashboard")
    public String dashboard(Model model) {


        List<UserEntity> clients = userService.findAllUsersByRole("CLIENTE");
        System.out.println("Clientes encontrados: " + clients.size());
        model.addAttribute("clientes", clients);


        return "dashboard";
    }
}

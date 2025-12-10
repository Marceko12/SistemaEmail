package com.system.emails.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.system.emails.model.dto.CorreoDto;


@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        CorreoDto c1 = new CorreoDto(
            "test1@gmail.com",
            "Correo de prueba",
            "Mensaje de ejemplo 1",
            null,
            null,
            "ENVIADO",
            LocalDateTime.now()
        );

        CorreoDto c2 = new CorreoDto(
            "test2@gmail.com",
            "Segundo correo",
            "Mensaje de ejemplo 2",
            null,
            null,
            "ERROR",
            LocalDateTime.now().minusHours(1)
        );

        List<CorreoDto> lista = Arrays.asList(c1, c2);

        model.addAttribute("correos", lista);

        return "dashboard";
    }
}

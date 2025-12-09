package com.system.emails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.system.emails.model.CorreoDto;
import com.system.emails.service.CorreoService;


@Controller
public class CorreoController {

    @Autowired
    private CorreoService correoService;

    @GetMapping("/correo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("correo", new CorreoDto());
        return "formulario-correo";
    }

    @PostMapping("/correo/enviar")
    public String enviarCorreo(CorreoDto correoDto, Model model) {
        correoService.enviarCorreo(correoDto);
        model.addAttribute("mensaje", "Correo enviado correctamente");
        return "formulario-correo";
    }

}

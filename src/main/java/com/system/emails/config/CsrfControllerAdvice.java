package com.system.emails.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class CsrfControllerAdvice {

    @ModelAttribute
    public void addCsrfToken(Model model, CsrfToken token) {
        if (token != null) {
            model.addAttribute("_csrf", token);
        }
    }
}
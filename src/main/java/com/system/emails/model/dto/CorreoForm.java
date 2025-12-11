package com.system.emails.model.dto;

import java.util.List;

import lombok.Data;
import lombok.Setter;

@Data
public class CorreoForm {

     private String para;     // email del destinatario
    private String asunto;   // asunto del correo
    private String mensaje;  // contenido del mensaje
    private String emisor;
    private List<Long> destinatariosIds;

    public CorreoForm() {



    }


    public CorreoForm(String para, String asunto, String mensaje) {
        this.para = para;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

     public CorreoForm(String emisor, String para, String asunto, String mensaje) {
        this.emisor = emisor;
        this.para = para;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

   
}
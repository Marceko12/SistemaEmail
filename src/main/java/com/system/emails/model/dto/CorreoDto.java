package com.system.emails.model.dto;

import java.time.LocalDateTime;

public class CorreoDto {

    private Long id;
    private String emisor;
    private String destinatarios; // separados por coma
    private String asunto;
    private String mensaje;
    private String estado;
    private LocalDateTime fecha;


    
    public CorreoDto() {
    }

    // Constructor usado por ChatController (3 parámetros)
    public CorreoDto(String para, String asunto, String mensaje) {
        this.destinatarios = para;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    // Constructor usado por DashboardController
    public CorreoDto(Long id, String emisor, String destinatarios, String asunto,
                     String mensaje, String estado, LocalDateTime fecha) {
        this.id = id;
        this.emisor = emisor;
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public String getEmisor() { return emisor; }
    public String getDestinatarios() { return destinatarios; }
    public String getAsunto() { return asunto; }
    public String getMensaje() { return mensaje; }
    public String getEstado() { return estado; }
    public LocalDateTime getFecha() { return fecha; }

    // Necesario para ChatController
    public String getPara() {
        return destinatarios;
    }
}

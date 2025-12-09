package com.system.emails.model;

import java.time.LocalDateTime;

public class CorreoDto {

    private String destinatario;
    private String asunto;
    private String mensaje;
    private String cc;
    private String bcc;

    private String estado;            // NUEVO
    private LocalDateTime fecha;      // NUEVO


    public CorreoDto() {
    }

    public CorreoDto(String destinatario, String asunto, String mensaje, String cc, String bcc, String estado, LocalDateTime fecha) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.cc = cc;
        this.bcc = bcc;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

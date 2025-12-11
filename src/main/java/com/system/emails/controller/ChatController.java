package com.system.emails.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.system.emails.model.dto.CorreoForm;
import com.system.emails.service.CorreoService;
import com.system.emails.service.GeminiService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private CorreoService correoService;

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostMapping("/mensaje")
    public ResponseEntity<?> procesar(@RequestBody Map<String, String> body) {

        try {
            String mensajeUsuario = body.get("mensaje");

            String prompt = """
                    Eres un asistente que interpreta texto libre para enviar correos electrónicos.

                    REGLAS IMPORTANTES:
                    - Responde SOLO con un JSON válido.
                    - NO uses comillas invertidas.
                    - NO uses bloques de código.
                    - NO uses markdown.
                    - NO añadas texto fuera del JSON.

                    Formato para enviar correo:
                    {
                    "accion": "enviar_correo",
                    "emisor": "email",
                    "para": "email",
                    "asunto": "texto",
                    "mensaje": "texto"
                    }

                    Formato cuando NO es un correo:
                    {
                    "accion": "hablar",
                    "respuesta": "texto simple"
                    }

                    Usuario: %s
                    """.formatted(mensajeUsuario);

            // ---------- Gemini ----------
            String respuestaGemini = geminiService.generarRespuesta(prompt);
            System.out.println("RAW Gemini: " + respuestaGemini);

            if (respuestaGemini == null || respuestaGemini.isBlank()) {
                return ResponseEntity.internalServerError().body("⚠️ El modelo no respondió.");
            }

            if (!respuestaGemini.trim().startsWith("{")) {
                return ResponseEntity.ok("⚠️ El servicio está saturado. Intenta de nuevo.");
            }

            JsonObject json;
            try {
                json = JsonParser.parseString(respuestaGemini).getAsJsonObject();
            } catch (Exception e) {
                return ResponseEntity.ok("⚠️ El modelo devolvió un formato inesperado.");
            }

            if (!json.has("accion")) {
                return ResponseEntity.ok("⚠️ Respuesta inesperada del modelo.");
            }

            String accion = json.get("accion").getAsString();

            // -------------------------
            // ✔ ENVIAR CORREO
            // -------------------------
            if (accion.equals("enviar_correo")) {

                if (!json.has("emisor") || !json.has("para") || !json.has("asunto") || !json.has("mensaje")) {
                    return ResponseEntity.ok("⚠️ El JSON recibido está incompleto.");
                }

                CorreoForm form = new CorreoForm(
                        json.get("emisor").getAsString(),
                        json.get("para").getAsString(),
                        json.get("asunto").getAsString(),
                        json.get("mensaje").getAsString()
                );

                correoService.guardarCorreo(form);

                return ResponseEntity.ok("📨 Correo enviado correctamente.");
            }

            // -------------------------
            // ✔ HABLAR
            // -------------------------
            if (accion.equals("hablar")) {
                return ResponseEntity.ok(json.get("respuesta").getAsString());
            }

            return ResponseEntity.ok("⚠️ Acción no reconocida.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("⚠️ Error interno: " + e.getMessage());
        }
    }
}

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
import com.system.emails.model.dto.CorreoDto;
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
                - NO uses bloques de código como ```json o ```.
                - NO uses markdown.
                - NO añadas texto antes ni después del JSON.
                - El JSON debe ser EXACTAMENTE el que se pide.

                Formato para enviar correo:
                {
                "accion": "enviar_correo",
                "para": "correo@example.com",
                "asunto": "texto asunto",
                "mensaje": "contenido del mensaje"
                }

                Formato cuando NO es un correo:
                {
                "accion": "hablar",
                "respuesta": "Aquí tu explicación simple."
                }

                Usuario: %s
                """.formatted(mensajeUsuario);


        // Llamada a Gemini
        String respuestaGemini = geminiService.generarRespuesta(prompt);

        System.out.println("RAW Gemini: " + respuestaGemini);

        // 🔥 PROTECCIÓN: si no devuelve JSON, lo detectamos y devolvemos mensaje amigable
        if (respuestaGemini == null || respuestaGemini.isBlank()) {
            return ResponseEntity.internalServerError().body("⚠️ El modelo no respondió.");
        }

        // Si devuelve HTML o texto raro → NO parsear
        if (!respuestaGemini.trim().startsWith("{")) {
            return ResponseEntity.ok("⚠️ El servicio está saturado. Intenta de nuevo.");
        }

        // Intentar parsear JSON
        JsonObject json;
        try {
            json = JsonParser.parseString(respuestaGemini).getAsJsonObject();
        } catch (Exception e) {
            return ResponseEntity.ok("⚠️ El modelo devolvió un formato inesperado. Intenta de nuevo.");
        }

        // Validar estructura esperada
        if (!json.has("accion")) {
            return ResponseEntity.ok("⚠️ Respuesta inesperada del modelo.");
        }

        String accion = json.get("accion").getAsString();

        // ✔ Acción: enviar correo
        if (accion.equals("enviar_correo")) {

            if (!json.has("para") || !json.has("asunto") || !json.has("mensaje")) {
                return ResponseEntity.ok("⚠️ El JSON recibido está incompleto.");
            }

            CorreoDto dto = new CorreoDto(
                json.get("para").getAsString(),
                json.get("asunto").getAsString(),
                json.get("mensaje").getAsString()
            );

            correoService.enviarCorreo(dto);

            return ResponseEntity.ok("📨 Correo enviado correctamente.");
        }

        // ✔ Acción: hablar
        if (accion.equals("hablar")) {
            return ResponseEntity.ok(json.get("respuesta").getAsString());
        }

        // Acciones desconocidas
        return ResponseEntity.ok("⚠️ Acción no reconocida.");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError()
            .body("⚠️ Error interno: " + e.getMessage());
    }
}

}

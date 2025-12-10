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
import com.system.emails.model.CorreoDto;
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
            Cuando el usuario escribe un texto indicando enviar un correo, responde SOLO con un JSON EXACTO con este formato, sin comillas invertidas, sin formato markdown ni ningún texto adicional:

            {
            "accion": "enviar_correo",
            "para": "correo@example.com",
            "asunto": "texto asunto",
            "mensaje": "contenido del mensaje"f
            }

            Si el texto NO es para enviar correos, responde SOLO con un JSON EXACTO, sin comillas invertidas ni formato markdown:

            {
            "accion": "hablar",
            "respuesta": "Aquí tu explicación simple."
            }

            Ejemplo:

            Usuario: Quiero enviar un correo a juan@ejemplo.com con asunto Hola y mensaje ¿Cómo estás?
            Respuesta:
            {
            "accion": "enviar_correo",
            "para": "juan@ejemplo.com",
            "asunto": "Hola",
            "mensaje": "¿Cómo estás?"
            }

            Usuario: %s

            """.formatted(mensajeUsuario);


        String respuestaGemini = geminiService.generarRespuesta(prompt);

        System.out.println("Respuesta Gemini antes de parsear: " + respuestaGemini);

        // Validar JSON antes de parsear
        try {
            JsonObject json = JsonParser.parseString(respuestaGemini).getAsJsonObject();
            String accion = json.get("accion").getAsString();

            if (accion.equals("enviar_correo")) {

                CorreoDto dto = new CorreoDto();
                dto.setDestinatario(json.get("para").getAsString());
                dto.setAsunto(json.get("asunto").getAsString());
                dto.setMensaje(json.get("mensaje").getAsString());

                correoService.enviarCorreo(dto);

                return ResponseEntity.ok("📨 Correo enviado correctamente.");
            }

            return ResponseEntity.ok(json.get("respuesta").getAsString());

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Respuesta no es JSON válido: " + respuestaGemini);
        }

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError()
                .body("Error al procesar mensaje: " + e.getMessage());
    }
}

}

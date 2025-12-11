// ===============================
// Referencias al formulario y sus campos
// ===============================
const formularioCorreoSection = document.getElementById("formulario-correo-section");
const formCorreo = document.getElementById("form-correo");
const inputPara = document.getElementById("correo-para");
const inputAsunto = document.getElementById("correo-asunto");
const inputMensaje = document.getElementById("correo-mensaje");
const btnCancelarEnvio = document.getElementById("cancelar-envio");

// Si estas funciones vienen de otro archivo, se asume que existen:
// - chatInput
// - agregarMensaje()

// ===============================
// Utilidades
// ===============================
function getCsrfToken() {
    const match = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
    return match ? decodeURIComponent(match[1]) : "";
}

function getJsonHeaders() {
    return {
        "Content-Type": "application/json",
        "X-XSRF-TOKEN": getCsrfToken(),
    };
}

// ===============================
// Enviar mensaje al chat
// ===============================
async function enviarMensaje() {
    const texto = chatInput.value.trim();
    if (!texto) return;

    agregarMensaje(texto, "user");
    chatInput.value = "";

    try {
        const response = await fetch("/chat/mensaje", {
            method: "POST",
            headers: getJsonHeaders(),
            body: JSON.stringify({ mensaje: texto }),
        });

        const dataText = await response.text();

        // Intentar parsear JSON; si no es JSON, responder directamente
        let data;
        try {
            data = JSON.parse(dataText);
        } catch {
            agregarMensaje(dataText, "bot");
            return;
        }

        // Manejo según la acción enviada por el backend
        if (data.accion === "enviar_correo") {
            // Mostrar formulario y rellenar campos
            formularioCorreoSection.style.display = "block";
            inputPara.value = data.para || "";
            inputAsunto.value = data.asunto || "";
            inputMensaje.value = data.mensaje || "";

            agregarMensaje(
                "Formulario rellenado con los datos del correo. Por favor revisa y confirma para enviar.",
                "bot"
            );

            inputPara.focus();

        } else if (data.accion === "hablar") {
            // Respuesta normal de chat (ajusta la propiedad según tu backend)
            agregarMensaje(
                data.mensaje || data.respuesta || "No se recibió contenido para mostrar.",
                "bot"
            );

        } else {
            // Acción desconocida o simple respuesta
            agregarMensaje(
                data.mensaje || dataText || "No se pudo interpretar la respuesta del servidor.",
                "bot"
            );
        }
    } catch (error) {
        console.error(error);
        agregarMensaje("⚠️ Error al conectar con el servidor.", "bot");
    }
}

// ===============================
// Envío del formulario de correo
// ===============================
if (formCorreo) {
    formCorreo.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Validación HTML5
        if (!formCorreo.checkValidity()) {
            formCorreo.classList.add("was-validated");
            return;
        }

        const correoData = {
            para: inputPara.value.trim(),
            asunto: inputAsunto.value.trim(),
            mensaje: inputMensaje.value.trim(),
        };

        try {
            const res = await fetch("/correo/enviar", {
                method: "POST",
                headers: getJsonHeaders(),
                body: JSON.stringify(correoData),
            });

            const text = await res.text();
            agregarMensaje(text, "bot");

            // Ocultar y limpiar formulario tras envío
            formularioCorreoSection.style.display = "none";
            formCorreo.classList.remove("was-validated");
            formCorreo.reset();
        } catch (err) {
            console.error(err);
            agregarMensaje("Error enviando correo: " + err.message, "bot");
        }
    });
}

// ===============================
// Botón cancelar del formulario
// ===============================
if (btnCancelarEnvio) {
    btnCancelarEnvio.addEventListener("click", () => {
        formularioCorreoSection.style.display = "none";
        formCorreo.classList.remove("was-validated");
        formCorreo.reset();
    });
}

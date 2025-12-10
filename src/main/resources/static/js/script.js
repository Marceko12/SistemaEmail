// Referencias al formulario y sus campos
const formularioCorreoSection = document.getElementById("formulario-correo-section");
const formCorreo = document.getElementById("form-correo");
const inputPara = document.getElementById("correo-para");
const inputAsunto = document.getElementById("correo-asunto");
const inputMensaje = document.getElementById("correo-mensaje");
const btnCancelarEnvio = document.getElementById("cancelar-envio");

// Modificar la función enviarMensaje para manejar JSON de enviar_correo
async function enviarMensaje() {
    const texto = chatInput.value.trim();
    if (texto === "") return;
    agregarMensaje(texto, "user");
    chatInput.value = "";

    try {
        const response = await fetch("/chat/mensaje", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ mensaje: texto }),
        });

        const dataText = await response.text();

        // Intentar parsear JSON (si falla, mostrar como texto normal)
        let data;
        try {
            data = JSON.parse(dataText);
        } catch {
            agregarMensaje(dataText, "bot");
            return;
        }

        if (data.accion === "enviar_correo") {
            // Mostrar formulario y rellenar campos
            formularioCorreoSection.style.display = "block";
            inputPara.value = data.para || "";
            inputAsunto.value = data.asunto || "";
            inputMensaje.value = data.mensaje || "";
            agregarMensaje("Formulario rellenado con los datos del correo. Por favor revisa y confirma para enviar.", "bot");

            // Enfocar el primer campo del formulario
            inputPara.focus();
        } else if (data.accion === "hablar") {
            agregarMensaje(data.respuesta, "bot");
        } else {
            agregarMensaje("Respuesta no reconocida.", "bot");
        }
    } catch {
        agregarMensaje("⚠️ Error al conectar con el servidor.", "bot");
    }
}

// Validación y envío del formulario de correo
formCorreo.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Validación básica usando HTML5
    if (!formCorreo.checkValidity()) {
        formCorreo.classList.add("was-validated");
        return;
    }

    // Datos del correo
    const correoData = {
        para: inputPara.value.trim(),
        asunto: inputAsunto.value.trim(),
        mensaje: inputMensaje.value.trim(),
    };

    try {
        const res = await fetch("/correo/enviar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(correoData),
        });

        const text = await res.text();

        agregarMensaje(text, "bot");
        // Ocultar formulario tras envío exitoso
        formularioCorreoSection.style.display = "none";
        formCorreo.classList.remove("was-validated");
        formCorreo.reset();

    } catch (err) {
        agregarMensaje("Error enviando correo: " + err.message, "bot");
    }
});

// Botón cancelar para ocultar el formulario
btnCancelarEnvio.addEventListener("click", () => {
    formularioCorreoSection.style.display = "none";
    formCorreo.classList.remove("was-validated");
    formCorreo.reset();
});

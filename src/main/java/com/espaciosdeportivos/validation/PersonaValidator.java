package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.PersonaDTO;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PersonaValidator {

    // Expresión regular para teléfono en Bolivia (móvil o fijo)
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\+591(7|6|4|3)\\d{7}$");

    // Validación del teléfono
    public void validarTelefono(String telefono) {
        if (telefono != null && !TELEFONO_PATTERN.matcher(telefono).matches()) {
            throw new BusinessException("El teléfono debe ser un número válido de Bolivia. Ejemplo: +591 7XXXXXXXX");
        }
    }

    // Validación del CI (Cédula de Identidad en Bolivia)
   /* public void validarCi(String ci) {
        if (ci == null || !ci.matches("^[0-9]{6,10}$")) {
            throw new BusinessException("El CI debe tener entre 6 y 10 dígitos numéricos.");
        }
    }*/

    // Validación de la fecha de nacimiento
    public void validarFechaNacimiento(java.time.LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isAfter(java.time.LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser una fecha futura.");
        }
    }

    // Validación de los apellidos (apellido paterno opcional, apellido materno obligatorio)
    public void validarApellidos(String aPaterno, String aMaterno) {
        // Validamos el apellido materno, que es obligatorio
        if (aMaterno == null || aMaterno.trim().isEmpty()) {
            throw new BusinessException("El apellido materno es obligatorio.");
        }

        // Validamos que el apellido paterno no exceda los 100 caracteres
        if (aPaterno != null && aPaterno.length() > 100) {
            throw new BusinessException("El apellido paterno no puede exceder los 100 caracteres.");
        }

        // Validamos que el apellido materno no exceda los 100 caracteres
        if (aMaterno.length() > 100) {
            throw new BusinessException("El apellido materno no puede exceder los 100 caracteres.");
        }
    }

    // Validación completa para el DTO PersonaDTO
    public void validarPersona(PersonaDTO personaDTO) {
        validarTelefono(personaDTO.getTelefono());
        //validarCi(personaDTO.getCi());
        validarFechaNacimiento(personaDTO.getFechaNacimiento());
        validarApellidos(personaDTO.getAPaterno(), personaDTO.getAMaterno());
    }

    // Excepción personalizada
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

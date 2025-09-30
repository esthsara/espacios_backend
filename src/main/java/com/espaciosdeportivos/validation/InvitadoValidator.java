package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.InvitadoDTO;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InvitadoValidator {

    // Expresión regular para teléfono en Bolivia (móvil o fijo)
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\+591(7|6|4|3)\\d{7}$");

    // Validación del teléfono
    public void validarTelefono(String telefono) {
        if (telefono != null && !TELEFONO_PATTERN.matcher(telefono).matches()) {
            throw new BusinessException("El teléfono debe ser un número válido de Bolivia. Ejemplo: +591 7XXXXXXXX");
        }
    }

    // Validación del CI (Cédula de Identidad en Bolivia)
    public void validarCi(String ci) {
        if (ci == null || !ci.matches("^[0-9]{6,10}$")) {
            throw new BusinessException("El CI debe tener entre 6 y 10 dígitos numéricos.");
        }
    }

    // Validación de la fecha de nacimiento
    public void validarFechaNacimiento(java.time.LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isAfter(java.time.LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser una fecha futura.");
        }
    }

    // Validación de los apellidos (apellido paterno opcional, apellido materno obligatorio)
    public void validarApellidos(String aPaterno, String aMaterno) {
        // El apellido materno es obligatorio
        if (aMaterno == null || aMaterno.trim().isEmpty()) {
            throw new BusinessException("El apellido materno es obligatorio.");
        }

        // El apellido paterno es opcional, pero si se proporciona, no puede exceder 100 caracteres
        if (aPaterno != null && aPaterno.length() > 100) {
            throw new BusinessException("El apellido paterno no puede exceder los 100 caracteres.");
        }

        // El apellido materno no puede exceder 100 caracteres
        if (aMaterno.length() > 100) {
            throw new BusinessException("El apellido materno no puede exceder los 100 caracteres.");
        }
    }

    // Validación del estado de verificación
    public void validarVerificado(Boolean verificado) {
        if (verificado == null) {
            throw new BusinessException("El estado de verificación es obligatorio.");
        }
    }

    // Validación completa para el DTO InvitadoDTO
    public void validarInvitado(InvitadoDTO invitadoDTO) {
        if (invitadoDTO.getNombre() == null || invitadoDTO.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }

        if (invitadoDTO.getFechaNacimiento() == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria.");
        }

        if (invitadoDTO.getTelefono() == null || invitadoDTO.getTelefono().trim().isEmpty()) {
            throw new BusinessException("El teléfono es obligatorio.");
        }

        if (invitadoDTO.getEmail() == null || invitadoDTO.getEmail().trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }

        /*if (invitadoDTO.getCi() == null || invitadoDTO.getCi().trim().isEmpty()) {
            throw new BusinessException("El número de CI es obligatorio.");
        }*/

        if (invitadoDTO.getVerificado() == null) {
            throw new BusinessException("El estado de verificación es obligatorio.");
        }

        // Validaciones adicionales de formato
        validarTelefono(invitadoDTO.getTelefono());
        //validarCi(invitadoDTO.getCi());
        validarFechaNacimiento(invitadoDTO.getFechaNacimiento());
        validarApellidos(invitadoDTO.getAPaterno(), invitadoDTO.getAMaterno());
        validarVerificado(invitadoDTO.getVerificado());
    }

    // Excepción personalizada
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

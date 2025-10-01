package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.AdministradorDTO;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AdministradorValidator {

    // Expresión regular para teléfono en Bolivia (móvil o fijo)
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\+591(7|6|4|3)\\d{7}$");

    // Validación del teléfono
    public void validarTelefono(String telefono) {
        if (telefono != null && !TELEFONO_PATTERN.matcher(telefono).matches()) {
            throw new BusinessException("El teléfono debe ser un número válido de Bolivia. Ejemplo: +591 7XXXXXXXX");
        }
    }

    // Validación del CI (Cédula de Identidad en Bolivia)
    /*public void validarCi(String ci) {
        if (ci == null || !ci.matches("^[0-9]{6,10}$")) {
            throw new BusinessException("El CI debe tener entre 6 y 10 dígitos numéricos.");
        }
    }*/

    // Validación de la fecha de nacimiento
    /*public void validarFechaNacimiento(java.time.LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isAfter(java.time.LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser una fecha futura.");
        }
    }*/

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

    // Validación del cargo (obligatorio)
    public void validarCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new BusinessException("El cargo es obligatorio.");
        }

        if (cargo.length() > 100) {
            throw new BusinessException("El cargo no puede exceder los 100 caracteres.");
        }
    }

    // Validación de la dirección (obligatoria)
    public void validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BusinessException("La dirección es obligatoria.");
        }

        if (direccion.length() > 255) {
            throw new BusinessException("La dirección no puede exceder los 255 caracteres.");
        }
    }

    // Validación completa para el DTO AdministradorDTO
    public void validarAdministrador(AdministradorDTO administradorDTO) {
        if (administradorDTO.getNombre() == null || administradorDTO.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }

        if (administradorDTO.getFechaNacimiento() == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria.");
        }

        if (administradorDTO.getTelefono() == null || administradorDTO.getTelefono().trim().isEmpty()) {
            throw new BusinessException("El teléfono es obligatorio.");
        }

        if (administradorDTO.getEmail() == null || administradorDTO.getEmail().trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }

        /*if (administradorDTO.getCi() == null || administradorDTO.getCi().trim().isEmpty()) {
            throw new BusinessException("El número de CI es obligatorio.");
        }*/

        if (administradorDTO.getCargo() == null || administradorDTO.getCargo().trim().isEmpty()) {
            throw new BusinessException("El cargo es obligatorio.");
        }

        if (administradorDTO.getDireccion() == null || administradorDTO.getDireccion().trim().isEmpty()) {
            throw new BusinessException("La dirección es obligatoria.");
        }

        // Validaciones adicionales de formato
        validarTelefono(administradorDTO.getTelefono());
        //validarCi(administradorDTO.getCi());
        //validarFechaNacimiento(administradorDTO.getFechaNacimiento());
        validarApellidos(administradorDTO.getAPaterno(), administradorDTO.getAMaterno());
        validarCargo(administradorDTO.getCargo());
        validarDireccion(administradorDTO.getDireccion());
    }

    // Excepción personalizada
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

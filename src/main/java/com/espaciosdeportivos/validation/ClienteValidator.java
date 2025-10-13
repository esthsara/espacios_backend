package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.ClienteDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class ClienteValidator {

    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^[0-9]{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    public void validarTelefono(String telefono) {
        if (telefono == null || !TELEFONO_PATTERN.matcher(telefono).matches()) {
            throw new BusinessException("El teléfono debe tener exactamente 8 dígitos numéricos.");
        }
    }

    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException("El email no tiene un formato válido.");
        }
    }

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (nombre.length() > 100) {
            throw new BusinessException("El nombre no puede exceder los 100 caracteres.");
        }
    }

    public void validarApellidos(String aPaterno, String aMaterno) {
        // aMaterno obligatorio
        if (aMaterno == null || aMaterno.trim().isEmpty()) {
            throw new BusinessException("El apellido materno es obligatorio.");
        }
        if (aMaterno.length() > 100) {
            throw new BusinessException("El apellido materno no puede exceder los 100 caracteres.");
        }
        // aPaterno opcional pero con máximo 100
        if (aPaterno != null && aPaterno.length() > 100) {
            throw new BusinessException("El apellido paterno no puede exceder los 100 caracteres.");
        }
    }

    public void validarFechaNacimiento(java.time.LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser futura.");
        }
    }

    public void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new BusinessException("La categoria del cliente es obligatorio.");
        }
        if (categoria.length() > 50) {
            throw new BusinessException("El estado del cliente no puede exceder los 50 caracteres.");
        }
    }

    // Validación completa para el DTO ClienteDTO
    public void validarCliente(ClienteDTO clienteDTO) {
        validarNombre(clienteDTO.getNombre());
        validarApellidos(clienteDTO.getAPaterno(), clienteDTO.getAMaterno());
        validarFechaNacimiento(clienteDTO.getFechaNacimiento());
        validarTelefono(clienteDTO.getTelefono());
        validarEmail(clienteDTO.getEmail());
        validarCategoria(clienteDTO.getCategoria());
    }

    // Excepción personalizada
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

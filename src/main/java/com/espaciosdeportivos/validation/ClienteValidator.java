package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.ClienteDTO;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ClienteValidator {

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

    // Validación del estado del cliente (ACTIVO o INACTIVO)
    public void validarEstadoCliente(String estadoCliente) {
        if (estadoCliente == null || !(estadoCliente.equals("ACTIVO") || estadoCliente.equals("INACTIVO"))) {
            throw new BusinessException("El estado del cliente debe ser 'ACTIVO' o 'INACTIVO'.");
        }
    }

    // Validación completa para el DTO ClienteDTO
    public void validarCliente(ClienteDTO clienteDTO) {
        if (clienteDTO.getNombre() == null || clienteDTO.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }

        if (clienteDTO.getFechaNacimiento() == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria.");
        }

        if (clienteDTO.getTelefono() == null || clienteDTO.getTelefono().trim().isEmpty()) {
            throw new BusinessException("El teléfono es obligatorio.");
        }

        if (clienteDTO.getEmail() == null || clienteDTO.getEmail().trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }

        /*if (clienteDTO.getCi() == null || clienteDTO.getCi().trim().isEmpty()) {
            throw new BusinessException("El número de CI es obligatorio.");
        }*/

        if (clienteDTO.getEstadoCliente() == null || clienteDTO.getEstadoCliente().trim().isEmpty()) {
            throw new BusinessException("El estado del cliente es obligatorio.");
        }

        // Validaciones adicionales de formato
        validarTelefono(clienteDTO.getTelefono());
        //validarCi(clienteDTO.getCi());
        validarFechaNacimiento(clienteDTO.getFechaNacimiento());
        validarApellidos(clienteDTO.getAPaterno(), clienteDTO.getAMaterno());
        validarEstadoCliente(clienteDTO.getEstadoCliente());
    }

    // Excepción personalizada
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

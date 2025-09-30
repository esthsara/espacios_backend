package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.regex.Pattern;

@Component
public class UsuarioControlValidator {

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

    // Validación del estado operativo (obligatorio)
    public void validarEstadoOperativo(String estadoOperativo) {
        if (estadoOperativo == null || estadoOperativo.trim().isEmpty()) {
            throw new BusinessException("El estado operativo es obligatorio.");
        }

        if (estadoOperativo.length() > 50) {
            throw new BusinessException("El estado operativo no puede exceder los 50 caracteres.");
        }
    }

    // Validación de la hora de inicio de turno
    public void validarHoraInicioTurno(LocalTime horaInicioTurno) {
        if (horaInicioTurno == null) {
            throw new BusinessException("La hora de inicio de turno es obligatoria.");
        }
    }

    // Validación de la hora de fin de turno
    public void validarHoraFinTurno(LocalTime horaFinTurno) {
        if (horaFinTurno == null) {
            throw new BusinessException("La hora de fin de turno es obligatoria.");
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

    // Validación completa para el DTO UsuarioControlDTO
    public void validarUsuarioControl(UsuarioControlDTO usuarioControlDTO) {
        if (usuarioControlDTO.getNombre() == null || usuarioControlDTO.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }

        if (usuarioControlDTO.getFechaNacimiento() == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria.");
        }

        if (usuarioControlDTO.getTelefono() == null || usuarioControlDTO.getTelefono().trim().isEmpty()) {
            throw new BusinessException("El teléfono es obligatorio.");
        }

        if (usuarioControlDTO.getEmail() == null || usuarioControlDTO.getEmail().trim().isEmpty()) {
            throw new BusinessException("El email es obligatorio.");
        }

        /*if (usuarioControlDTO.getCi() == null || usuarioControlDTO.getCi().trim().isEmpty()) {
            throw new BusinessException("El número de CI es obligatorio.");
        }*/

        if (usuarioControlDTO.getEstadoOperativo() == null || usuarioControlDTO.getEstadoOperativo().trim().isEmpty()) {
            throw new BusinessException("El estado operativo es obligatorio.");
        }

        if (usuarioControlDTO.getHoraInicioTurno() == null) {
            throw new BusinessException("La hora de inicio de turno es obligatoria.");
        }

        if (usuarioControlDTO.getHoraFinTurno() == null) {
            throw new BusinessException("La hora de fin de turno es obligatoria.");
        }

        if (usuarioControlDTO.getDireccion() == null || usuarioControlDTO.getDireccion().trim().isEmpty()) {
            throw new BusinessException("La dirección es obligatoria.");
        }

        // Validaciones adicionales de formato
        validarTelefono(usuarioControlDTO.getTelefono());
        //validarCi(usuarioControlDTO.getCi());
        validarFechaNacimiento(usuarioControlDTO.getFechaNacimiento());
        validarApellidos(usuarioControlDTO.getAPaterno(), usuarioControlDTO.getAMaterno());
        validarEstadoOperativo(usuarioControlDTO.getEstadoOperativo());
        validarHoraInicioTurno(usuarioControlDTO.getHoraInicioTurno());
        validarHoraFinTurno(usuarioControlDTO.getHoraFinTurno());
        validarDireccion(usuarioControlDTO.getDireccion());
    }

    // Excepción personalizada
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

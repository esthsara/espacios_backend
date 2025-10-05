package com.espaciosdeportivos.validation;

import com.espaciosdeportivos.dto.UsuarioControlDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

@Component
public class UsuarioControlValidator {

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
        if (aMaterno == null || aMaterno.trim().isEmpty()) {
            throw new BusinessException("El apellido materno es obligatorio.");
        }
        if (aMaterno.length() > 100) {
            throw new BusinessException("El apellido materno no puede exceder los 100 caracteres.");
        }
        if (aPaterno != null && aPaterno.length() > 100) {
            throw new BusinessException("El apellido paterno no puede exceder los 100 caracteres.");
        }
    }

    public void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser futura.");
        }
    }

    public void validarEstadoOperativo(String estadoOperativo) {
        if (estadoOperativo == null || estadoOperativo.trim().isEmpty()) {
            throw new BusinessException("El estado operativo es obligatorio.");
        }
        if (estadoOperativo.length() > 50) {
            throw new BusinessException("El estado operativo no puede exceder los 50 caracteres.");
        }
    }

    public void validarTurno(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new BusinessException("Las horas de turno son obligatorias.");
        }
        if (fin.isBefore(inicio)) {
            throw new BusinessException("La hora de fin de turno no puede ser anterior a la hora de inicio.");
        }
    }

    public void validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BusinessException("La dirección es obligatoria.");
        }
        if (direccion.length() > 200) {
            throw new BusinessException("La dirección no puede exceder los 200 caracteres.");
        }
    }

    public void validarUsuarioControl(UsuarioControlDTO dto) {
        validarNombre(dto.getNombre());
        validarApellidos(dto.getAPaterno(), dto.getAMaterno());
        validarFechaNacimiento(dto.getFechaNacimiento());
        validarTelefono(dto.getTelefono());
        validarEmail(dto.getEmail());
        validarEstadoOperativo(dto.getEstadoOperativo());
        validarTurno(dto.getHoraInicioTurno(), dto.getHoraFinTurno());
        validarDireccion(dto.getDireccion());
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

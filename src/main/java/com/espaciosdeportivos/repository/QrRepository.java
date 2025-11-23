package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrRepository extends JpaRepository<Qr, Long> {

    // Verifica si existe un código QR (ignorando mayúsculas/minúsculas)
    Boolean existsByCodigoQrIgnoreCase(String codigoQr);

    // Busca todos los QRs activos
    List<Qr> findByEstadoTrue();

    // Busca un QR por su código, solo si está activo
    Optional<Qr> findByCodigoQrIgnoreCaseAndEstadoTrue(String codigoQr);

    // Busca un QR por su ID, solo si está activo
    Optional<Qr> findByIdQrAndEstadoTrue(Long idQr);

    // Busca QRs por ID de reserva
    List<Qr> findByReserva_IdReserva(Long idReserva);

    // Busca QRs por ID de usuario de control (referencia a Persona)
    List<Qr> findByUsuarioControl_Id(Long idUsuarioControl);

    // Busca QRs por ID de invitado (referencia a Persona)
    List<Qr> findByInvitado_Id(Long idInvitado);

    // Alternativa duplicada (puedes eliminar si no se usa)
    List<Qr> findByReservaIdReserva(Long idReserva);
}

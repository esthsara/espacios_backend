package com.espaciosdeportivos.service.impl;
// com.espaciosdeportivos.service.impl.IncluyeServiceImpl.java

import com.espaciosdeportivos.dto.IncluyeDTO;
import com.espaciosdeportivos.model.*;
import com.espaciosdeportivos.repository.*;
import com.espaciosdeportivos.service.IIncluyeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IncluyeServiceImpl implements IIncluyeService {

    private final IncluyeRepository incluyeRepository;
    private final ReservaRepository reservaRepository;
    private final CanchaRepository canchaRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Override
    public IncluyeDTO asociarCanchaDisciplinaAReserva(IncluyeDTO dto) {
        // Validar existencia de las entidades relacionadas
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + dto.getIdReserva()));

        Cancha cancha = canchaRepository.findById(dto.getIdCancha())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + dto.getIdCancha()));

        Disciplina disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina no encontrada con ID: " + dto.getIdDisciplina()));

        // Crear ID compuesto
        IncluyeId id = new IncluyeId(dto.getIdCancha(), dto.getIdReserva(), dto.getIdDisciplina());

        // Verificar si ya existe (opcional, según reglas de negocio)
        if (incluyeRepository.existsById(id)) {
            throw new IllegalStateException("La asociación ya existe");
        }

        // Calcular monto total
        //double horas = java.time.Duration.between(reserva.getHoraInicio(), reserva.getHoraFin()).toHours();
        double minutos = java.time.Duration.between(reserva.getHoraInicio(), reserva.getHoraFin()).toMinutes();
        double horas = minutos / 60.0;
        double montoTotal = cancha.getCostoHora() * horas;

        // Crear y guardar
        //Incluye incluye = convertToEntity(dto);
        Incluye incluye = Incluye.builder()
                .id(id)
                .reserva(reserva)
                .cancha(cancha)
                .disciplina(disciplina)
                .montoTotal(montoTotal)
                .build();

        incluye = incluyeRepository.save(incluye);
        return convertToDTO(incluye);
    }

    @Override
    public Double obtenerMontoTotal(Long idReserva, Long idCancha, Long idDisciplina) {
        IncluyeId id = new IncluyeId(idCancha, idReserva, idDisciplina);
        Incluye incluye = incluyeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asociación no encontrada"));
        return incluye.getMontoTotal();
    }


    @Override
    public IncluyeDTO obtenerPorReservaCanchaDisciplina(Long idReserva, Long idCancha, Long idDisciplina) {
        IncluyeId id = new IncluyeId(idCancha, idReserva, idDisciplina);
        Incluye incluye = incluyeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asociación no encontrada"));
        return convertToDTO(incluye);
    }

    @Override
    public void desasociarCanchaDisciplinaDeReserva(Long idReserva, Long idCancha, Long idDisciplina) {
        IncluyeId id = new IncluyeId(idCancha, idReserva, idDisciplina);
        if (!incluyeRepository.existsById(id)) {
            throw new EntityNotFoundException("Asociación no encontrada");
        }
        incluyeRepository.deleteById(id);
    }

    @Override
    public List<IncluyeDTO> obtenerPorReserva(Long idReserva) {
        return incluyeRepository.findByIdReserva(idReserva) 
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncluyeDTO> obtenerPorCancha(Long idCancha) {
        return incluyeRepository.findByCanchaIdCancha(idCancha)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncluyeDTO> obtenerPorDisciplina(Long idDisciplina) {
        return incluyeRepository.findByIdDisciplina(idDisciplina)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private IncluyeDTO convertToDTO(Incluye incluye) {
        return IncluyeDTO.builder()
                .idReserva(incluye.getReserva().getIdReserva())
                .idCancha(incluye.getCancha().getIdCancha())
                .idDisciplina(incluye.getDisciplina().getIdDisciplina())
                .montoTotal(incluye.getMontoTotal())
                .build();
    }
    // Dentro de IncluyeServiceImpl.java

    private Incluye convertToEntity(IncluyeDTO dto) {
        if (dto == null) return null;
        
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + dto.getIdReserva()));

        Cancha cancha = canchaRepository.findById(dto.getIdCancha())
                .orElseThrow(() -> new EntityNotFoundException("Cancha no encontrada con ID: " + dto.getIdCancha()));

        Disciplina disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina no encontrada con ID: " + dto.getIdDisciplina()));

        // 3. **CONSTRUIR LA ENTIDAD** (Sintaxis corregida)
        return Incluye.builder()
                .reserva(reserva) // Asignar la entidad Reserva
                .cancha(cancha)   // Asignar la entidad Cancha
                .disciplina(disciplina) // Asignar la entidad Disciplina
                .montoTotal(dto.getMontoTotal()) // Asignar el monto
                .build();
    }
}
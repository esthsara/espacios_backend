package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.MacrodistritoDTO;
import com.espaciosdeportivos.model.Macrodistrito;
import com.espaciosdeportivos.repository.MacrodistritoRepository;
import com.espaciosdeportivos.service.IMacrodistritoService;
import com.espaciosdeportivos.validation.MacrodistritoValidator;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MacrodistritoServiceImpl implements IMacrodistritoService {

    private final MacrodistritoRepository macrodistritoRepository;
    private final MacrodistritoValidator macrodistritoValidator;

    @Override
    @Transactional(readOnly = true)
    public List<MacrodistritoDTO> obtenerTodosLosMacrodistritos() {
        return macrodistritoRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                //aqui dice que funciona desde java 8 pero podria usar el tolist directamente
    }


    @Override
    @Transactional(readOnly = true)
    public List<MacrodistritoDTO> ListarTodos() {
        return macrodistritoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                //aqui dice que funciona desde java 8 pero podria usar el tolist directamente
    }


    @Override
    @Transactional(readOnly = true)
    public MacrodistritoDTO obtenerMacrodistritoPorId(Long idMacrodistrito) {
        Macrodistrito macrodistrito = macrodistritoRepository.findById(idMacrodistrito)
                .orElseThrow(() -> new RuntimeException("Macrodistrito no encontrado con ID: " + idMacrodistrito));
        return convertToDTO(macrodistrito);
    }


    @Override
    @Transactional
    public MacrodistritoDTO crearMacrodistrito(MacrodistritoDTO macrodistritoDTO) {
        macrodistritoValidator.validarMacrodistrito(macrodistritoDTO);
        // if (macrodistritoRepository.existsByNombreIgnoreCase(macrodistritoDTO.getNombre())) {
        //     throw new MacrodistritoValidator.BusinessException("Ya existe un macrodistrito con ese nombre.");
        // }
        Macrodistrito macrodistrito = convertToEntity(macrodistritoDTO);
        macrodistrito.setIdMacrodistrito(null);//Esto fuerza a JPA a generar un nuevo ID cuando guardas la entidad.
        macrodistrito.setEstado(Boolean.TRUE);

        Macrodistrito guardado = macrodistritoRepository.save(macrodistrito);
        return convertToDTO(guardado);
    }


    @Override
    @Transactional
    public MacrodistritoDTO actualizarMacrodistrito(Long id, MacrodistritoDTO macrodistritoDTO) {
        Macrodistrito existente = macrodistritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Macrodistrito no encontrado con ID: " + id));

        macrodistritoValidator.validarMacrodistrito(macrodistritoDTO);
        // Si quiero que actualizar no repita el mismo nombre en otro id
        // macrodistritoRepository.findByNombreIgnoreCase(dto.getNombre())
        //         .filter(m -> !m.getIdMacrodistrito().equals(id))
        //         .ifPresent(m -> { throw new MacrodistritoValidator.BusinessException("Ya existe un macrodistrito con ese nombre."); });

        existente.setNombre(macrodistritoDTO.getNombre());
        existente.setDescripcion(macrodistritoDTO.getDescripcion());
        existente.setEstado(macrodistritoDTO.getEstado());
        Macrodistrito actualizada = macrodistritoRepository.save(existente);

        return convertToDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminarMacrodistritoFisicamente(Long id) {
        Macrodistrito existente = macrodistritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Macrodistrito no encontrado con ID: " + id));
        macrodistritoRepository.delete(existente);
        
    }

    @Override
    @Transactional
    public MacrodistritoDTO eliminarMacrodistrito(Long id, Boolean nuevoEstado) {
        Macrodistrito existente = macrodistritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Macrodistrito no encontrado con ID: " + id));

        existente.setEstado(nuevoEstado);
        Macrodistrito eliminada = macrodistritoRepository.save(existente);
        return convertToDTO(eliminada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MacrodistritoDTO> buscarPorNombre(String nombre) {
        return macrodistritoRepository.buscarPorNombre(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Macrodistrito obtenerMacrodistritoConBloqueo(Long id) {
        Macrodistrito macrodistrito = macrodistritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Macrodistrito no encontrado con ID: " + id));
        try{
            Thread.sleep(15000); // Simula retardo
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return macrodistrito;
    }


    // --------- mapping ----------
    private MacrodistritoDTO convertToDTO(Macrodistrito m) {
        return MacrodistritoDTO.builder()
                .idMacrodistrito(m.getIdMacrodistrito())
                .nombre(m.getNombre())
                .descripcion(m.getDescripcion())
                .estado(m.getEstado())
                .build();
    }

    private Macrodistrito convertToEntity(MacrodistritoDTO d) {
        return Macrodistrito.builder()
                .idMacrodistrito(d.getIdMacrodistrito())
                .nombre(d.getNombre())
                .descripcion(d.getDescripcion())
                .estado(d.getEstado() != null ? d.getEstado() : Boolean.TRUE)
                .build();
    }
}

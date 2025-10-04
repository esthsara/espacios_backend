/*package com.espaciosdeportivos.service.impl;



import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.repository.DisciplinaRepository;
import com.espaciosdeportivos.service.IDisciplinaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DisciplinaServiceImpl implements IDisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    // Mapear Entity -> DTO
    private DisciplinaDTO mapToDTO(Disciplina disciplina) {
        return DisciplinaDTO.builder()
                .idDisciplina(disciplina.getIdDisciplina())
                .nombre(disciplina.getNombre())
                .descripcion(disciplina.getDescripcion())
                .build();
    }

    // Mapear DTO -> Entity
    private Disciplina mapToEntity(DisciplinaDTO dto) {
        return Disciplina.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    @Override
    public List<DisciplinaDTO> listarTodas() {
        return disciplinaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DisciplinaDTO obtenerPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con ID: " + id));
        return mapToDTO(disciplina);
    }

    @Override
    public DisciplinaDTO crear(DisciplinaDTO dto) {
        // Validación: evitar duplicados por nombre
        if (disciplinaRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una disciplina con ese nombre.");
        }
        Disciplina disciplina = mapToEntity(dto);
        Disciplina nueva = disciplinaRepository.save(disciplina);
        return mapToDTO(nueva);
    }

    @Override
    public DisciplinaDTO actualizar(Long id, DisciplinaDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada con ID: " + id));

        disciplina.setNombre(dto.getNombre());
        disciplina.setDescripcion(dto.getDescripcion());

        Disciplina actualizada = disciplinaRepository.save(disciplina);
        return mapToDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new RuntimeException("Disciplina no encontrada con ID: " + id);
        }
        disciplinaRepository.deleteById(id);
    }

    @Override
    public List<DisciplinaDTO> buscarPorNombre(String nombre) {
        return disciplinaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
*/

// service/impl/DisciplinaServiceImpl.java
package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.repository.DisciplinaRepository;

//agegado PA img
import com.espaciosdeportivos.repository.ImagenRepository;
import com.espaciosdeportivos.service.IDisciplinaService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisciplinaServiceImpl implements IDisciplinaService {
    //IMPORTA EL REPOSITORY DE LAS IMG
    @Autowired
    private ImagenRepository imagenRepository;
    //------------------------------------------------
    private final DisciplinaRepository disciplinaRepository;

    @Override
    public DisciplinaDTO crearDisciplina(DisciplinaDTO dto) {
        Disciplina disciplina = Disciplina.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .estado(true)
                .build();
        disciplinaRepository.save(disciplina);
        dto.setId(disciplina.getId());
        dto.setEstado(disciplina.getEstado());
        return dto;
    }

    @Override
    public List<DisciplinaDTO> listarTodas() {
        return disciplinaRepository.findByEstadoTrue().stream()
                .map(d -> {
                    DisciplinaDTO dto = new DisciplinaDTO();
                    dto.setId(d.getId());
                    dto.setNombre(d.getNombre());
                    dto.setDescripcion(d.getDescripcion());
                    dto.setEstado(d.getEstado());
                    return dto;
                }).collect(Collectors.toList());
    }
    //OBTINE SOLO LA DISCIPLINA SIN IMAGENS
    /*@Override
    public DisciplinaDTO obtenerPorId(Long id) {
        Disciplina d = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        dto.setDescripcion(d.getDescripcion());
        dto.setEstado(d.getEstado());
        return dto;
    }*/
    //PRUEBA PARA OBTENER LISTA DE IMG X ID
    @Override
    public DisciplinaDTO obtenerPorId(Long id) {
        Disciplina d = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));

        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        dto.setDescripcion(d.getDescripcion());
        dto.setEstado(d.getEstado());

        // Llenar imágenes
        List<ImagenDTO> imagenes = imagenRepository.findByDisciplinaIdAndEstadoTrue(d.getId()).stream()
                .map(img -> {
                    ImagenDTO imgDto = new ImagenDTO();
                    imgDto.setId(img.getId());
                    imgDto.setUrl(img.getUrl());
                    imgDto.setNombreArchivo(img.getNombreArchivo());
                    imgDto.setEstado(img.getEstado());
                    return imgDto;
                }).toList();

        dto.setImagenes(imagenes);

        return dto;
    }



    @Override
    public DisciplinaDTO actualizarDisciplina(Long id, DisciplinaDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));
        disciplina.setNombre(dto.getNombre());
        disciplina.setDescripcion(dto.getDescripcion());
        disciplinaRepository.save(disciplina);
        dto.setId(disciplina.getId());
        dto.setEstado(disciplina.getEstado());
        return dto;
    }

    @Override
    public void eliminarDisciplina(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));
        disciplina.setEstado(false); // eliminación lógica
        disciplinaRepository.save(disciplina);
    }
}

package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.DisciplinaCreateDTO;
import com.espaciosdeportivos.dto.DisciplinaDTO;
import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.service.IDisciplinaService;
import com.espaciosdeportivos.service.IImagenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
@RequiredArgsConstructor
public class DisciplinaController {

    private final IDisciplinaService disciplinaService;
    private final IImagenService imagenService;

    // -----------------------------
    // Listar todas las disciplinas
    // -----------------------------
    @GetMapping
    public List<DisciplinaDTO> listar() {
        return disciplinaService.listarTodas();
    }

    // -----------------------------
    // Obtener disciplina por ID
    // -----------------------------
    @GetMapping("/{id}")
    public DisciplinaDTO obtener(@PathVariable Long id) {
        return disciplinaService.obtenerPorId(id);
    }

    // -----------------------------
    // Crear nueva disciplina
    // -----------------------------
    @PostMapping
    public DisciplinaDTO crear(@Valid @RequestBody DisciplinaDTO dto) {
        return disciplinaService.crearDisciplina(dto);
    }

    // -----------------------------
    // Crear disciplina + imágenes
    // -----------------------------
    @PostMapping("/crear-con-imagenes")
    @Transactional
    public DisciplinaDTO crearConImagenes(@ModelAttribute DisciplinaCreateDTO dto) throws Exception {
        // 1️⃣ Convertir DisciplinaCreateDTO a DisciplinaDTO
        DisciplinaDTO dtoBase = new DisciplinaDTO();
        dtoBase.setNombre(dto.getNombre());
        dtoBase.setDescripcion(dto.getDescripcion());
        dtoBase.setEstado(true);

        // 2️⃣ Crear disciplina
        DisciplinaDTO disciplina = disciplinaService.crearDisciplina(dtoBase);

        // 3️⃣ Subir imágenes si existen
        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            for (var file : dto.getImagenes()) {
                imagenService.guardarImagen(file, disciplina.getId());
            }
        }

        // 4️⃣ Listar imágenes y agregarlas al DTO
        List<ImagenDTO> imagenes = imagenService.listarImagenesPorDisciplina(disciplina.getId());
        disciplina.setImagenes(imagenes);

        return disciplina;
    }

    // -----------------------------
    // Actualizar disciplina
    // -----------------------------
    @PutMapping("/{id}")
    public DisciplinaDTO actualizar(@PathVariable Long id, @Valid @RequestBody DisciplinaDTO dto) {
        return disciplinaService.actualizarDisciplina(id, dto);
    }

    // -----------------------------
    // Eliminar disciplina (lógica)
    // -----------------------------
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        disciplinaService.eliminarDisciplina(id);
    }

    // -----------------------------
    // Buscar disciplinas por nombre
    // -----------------------------
    // Ej: http://localhost:8032/api/disciplinas/buscar?nombre=Volleyball
    /*@GetMapping("/buscar")
    public List<DisciplinaDTO> buscarPorNombre(@RequestParam String nombre) {
        return disciplinaService.buscarPorNombre(nombre);
    }*/
}

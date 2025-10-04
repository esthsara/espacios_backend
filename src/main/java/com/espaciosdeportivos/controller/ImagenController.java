// controller/ImagenController.java
package com.espaciosdeportivos.controller;

import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.service.IImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {

    private final IImagenService imagenService;

    @PostMapping("/{disciplinaId}")
    public ImagenDTO subirImagen(@RequestParam("file") MultipartFile file,
                                 @PathVariable Long disciplinaId) throws IOException {
        return imagenService.guardarImagen(file, disciplinaId);
    }

    @GetMapping("/disciplina/{disciplinaId}")
    public List<ImagenDTO> listarPorDisciplina(@PathVariable Long disciplinaId) {
        return imagenService.listarImagenesPorDisciplina(disciplinaId);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        imagenService.eliminarImagen(id);
    }
}

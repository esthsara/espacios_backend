/*package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ImagenDTO;
import java.util.List;

public interface IImagenService {
    ImagenDTO crearImagen(ImagenDTO imagenDTO, Long idDisciplina);
    ImagenDTO actualizarImagen(Long id, ImagenDTO imagenDTO);
    void eliminarImagen(Long id);
    ImagenDTO obtenerImagen(Long id);
    List<ImagenDTO> listarImagenesPorDisciplina(Long idDisciplina);
}*/

// service/IImagenService.java
package com.espaciosdeportivos.service;

import com.espaciosdeportivos.dto.ImagenDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImagenService {
    ImagenDTO guardarImagen(MultipartFile file, Long disciplinaId) throws IOException;
    List<ImagenDTO> listarImagenesPorDisciplina(Long disciplinaId);
    void eliminarImagen(Long id);
}

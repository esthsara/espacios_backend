/*package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.config.FileStorageProperties;
import com.espaciosdeportivos.dto.ImagenDto;
import com.espaciosdeportivos.model.Imagen;
import com.espaciosdeportivos.repository.ImagenRepository;
import com.espaciosdeportivos.service.IImagenService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImagenServiceImpl implements IImagenService, InitializingBean {

    private final Path fileStorageLocation;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    public ImagenServiceImpl(FileStorageProperties properties) {
        this.fileStorageLocation = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
    }

    @Override
    public void afterPropertiesSet() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta para almacenar imágenes", e);
        }
    }

    @Override
    public ImagenDto guardarImagen(MultipartFile file, String ownerType, Long ownerId) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "-" + originalFileName;

        try {
            Path ownerFolder = this.fileStorageLocation.resolve(ownerType);
            if (!Files.exists(ownerFolder)) {
                Files.createDirectories(ownerFolder);
            }

            Path destinationFile = ownerFolder.resolve(fileName);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            Imagen imagen = Imagen.builder()
                    .url("/uploads/" + ownerType + "/" + fileName)
                    .activa(true)
                    .fechaCreacion(LocalDateTime.now())
                    .ownerType(ownerType)
                    .ownerId(ownerId)
                    .build();

            return mapToDto(imagenRepository.save(imagen));

        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }
    }

    @Override
    public List<ImagenDto> obtenerImagenes(String ownerType, Long ownerId) {
        return imagenRepository.findByOwnerTypeAndOwnerIdAndActivaTrue(ownerType, ownerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarImagen(Long id) {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        imagen.setActiva(false);
        imagenRepository.save(imagen);
    }

    private ImagenDto mapToDto(Imagen imagen) {
        return ImagenDto.builder()
                .id(imagen.getId())
                .url(imagen.getUrl())
                .activa(imagen.getActiva())
                .fechaCreacion(imagen.getFechaCreacion())
                .build();
    }
}
*/
// service/impl/ImagenServiceImpl.java
package com.espaciosdeportivos.service.impl;

import com.espaciosdeportivos.dto.ImagenDTO;
import com.espaciosdeportivos.model.Disciplina;
import com.espaciosdeportivos.model.Imagen;
import com.espaciosdeportivos.repository.DisciplinaRepository;
import com.espaciosdeportivos.repository.ImagenRepository;
import com.espaciosdeportivos.service.IImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImagenServiceImpl implements IImagenService {

    private final ImagenRepository imagenRepository;
    private final DisciplinaRepository disciplinaRepository;

    private final String UPLOAD_DIR = "C:/Users/DELL/Desktop/Proy/subiendoprueba/img/";

    @Override
    public ImagenDTO guardarImagen(MultipartFile file, Long disciplinaId) throws IOException {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR, "disciplinas");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Imagen imagen = Imagen.builder()
                .nombreArchivo(fileName)
                .url("/img/disciplinas/" + fileName)
                .estado(true)
                .disciplina(disciplina)
                .build();

        imagenRepository.save(imagen);

        ImagenDTO dto = new ImagenDTO();
        dto.setId(imagen.getId());
        dto.setNombreArchivo(fileName);
        dto.setUrl(imagen.getUrl());
        dto.setEstado(imagen.getEstado());
        return dto;
    }

    @Override
    public List<ImagenDTO> listarImagenesPorDisciplina(Long disciplinaId) {
        return imagenRepository.findByDisciplinaIdAndEstadoTrue(disciplinaId).stream()
                .map(img -> {
                    ImagenDTO dto = new ImagenDTO();
                    dto.setId(img.getId());
                    dto.setUrl(img.getUrl());
                    dto.setNombreArchivo(img.getNombreArchivo());
                    dto.setEstado(img.getEstado());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public void eliminarImagen(Long id) {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        imagen.setEstado(false); // eliminación lógica
        imagenRepository.save(imagen);
    }
}

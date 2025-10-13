package com.espaciosdeportivos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {
    
    private List<String> allowedTypes;
    
    @Override
    public void initialize(ValidFileType constraintAnnotation) {
        this.allowedTypes = Arrays.asList(constraintAnnotation.allowedTypes());
    }
    
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        // Si el archivo es nulo o está vacío, se considera válido (la validación de required se hace aparte)
        if (file == null || file.isEmpty()) {
            return true;
        }
        
        String fileName = file.getOriginalFilename();
        
        // Si no hay nombre de archivo, es inválido
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        // Obtener la extensión del archivo
        String extension = "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            extension = fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        
        // Si no tiene extensión, es inválido
        if (extension.isEmpty()) {
            return false;
        }
        
        // Verificar si la extensión está permitida
        boolean isValid = allowedTypes.contains(extension);
        
        // Personalizar el mensaje de error si es inválido
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "Tipo de archivo '" + extension + "' no permitido. Formatos aceptados: " + 
                String.join(", ", allowedTypes)
            ).addConstraintViolation();
        }
        
        return isValid;
    }
}
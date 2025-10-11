package com.espaciosdeportivos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileType {
    
    String message() default "Tipo de archivo no permitido. Solo se permiten: {allowedTypes}";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    String[] allowedTypes() default {"jpg", "jpeg", "png", "gif", "webp"};
}
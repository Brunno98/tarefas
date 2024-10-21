package br.com.brunno.tarefas.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = { UsuarioExisteValidator.class })
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsuarioExiste {
    String message() default "Usuario não encontrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

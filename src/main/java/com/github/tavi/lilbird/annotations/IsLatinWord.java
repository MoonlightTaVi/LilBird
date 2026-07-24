package com.github.tavi.lilbird.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;


/**
 * This constraint annotation indicates that 
 * a {@code String} field / parameter may contain only latin letters of any case,
 * hyphens ("-") and white spaces.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = IsLatinWord.Validator.class)
public @interface IsLatinWord {

    String message() default "The parameter may contain only latin letters, hyphens and white spaces.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<IsLatinWord,String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value.matches("(?=.*[a-zA-Z])[0-9a-zA-Z\\s-]+");
        }

    }
}

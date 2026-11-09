package com.github.tavi.lilbird.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;


/**
 * A {@code String} may contain only latin letters of any case,
 * hyphens ({@code -}) and white space, and must contain at least one letter.
 * {@code null} is not considered valid.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD, 
    ElementType.PARAMETER
})
@Constraint(validatedBy = IsLatinWord.Validator.class)
public @interface IsLatinWord {

    String message() default "The parameter may contain only latin letters, hyphens and white spaces.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class Validator implements ConstraintValidator<IsLatinWord,String> {

        /** Letters, hyphens and white space, with at least one letter. */
        public static final String PATTERN = "(?=.*[a-zA-Z])[a-zA-Z\\s-]+";

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && value.matches(PATTERN);
        }

    }

}

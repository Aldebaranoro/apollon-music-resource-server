package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Указывает, что данное поле необходимо проверить на валидность UUID
 */
@Target({FIELD, PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
@ReportAsSingleViolation
@Documented
public @interface UUID {

    String message() default "{invalid.uuid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

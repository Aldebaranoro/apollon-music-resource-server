package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Указывает, что данное поле необходимо проверить на валидность Discord identity
 */
@Target({FIELD, PARAMETER, TYPE_USE})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Pattern(regexp = "^(.*)#(\\d{4})$")
@ReportAsSingleViolation
@Documented
public @interface DiscordIdentity {

    String message() default "{invalid.discordIdentity}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

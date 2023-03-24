package com.flight.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validator {
    StringValidator[] value() default {};


    enum StringValidator implements Predicate<String> {
        NON_EMPTY(s -> !s.isBlank()),
        TIME(s -> s.matches("^\\d{2}:\\d{2}$")),
        WORDS(s -> s.matches("^[A-Za-z]+(\\s+[A-Za-z]+)*$")),
        DIGITS(s -> s.matches("^\\d+$")),
        POSITIVE_OR_NULL(s -> DIGITS.test(s) && Integer.parseInt(s) >= 0),
        NUMBERPLATE(s -> s.matches("^[A-Za-z]{3,4}-\\d{3}$"));

        private final Predicate<String> predicate;

        StringValidator(Predicate<String> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(String s) {
            return predicate.test(s);
        }
    }
}


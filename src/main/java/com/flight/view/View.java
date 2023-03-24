package com.flight.view;

import javafx.scene.control.Control;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface View {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface SingleVale {
//        Class<? extends CrudRepository> listSupplier() default CrudRepository.class;


        String attributeExtractor() default "";

        String[] list() default {};

        Class<? extends Control> control();

        String name();

        Class<? extends StringConverter<?>> stringConverter() default DefaultStringConverter.class;

        Class<?> listValueClass() default CrudRepository.class;
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface IncludeSuper {
    }
}

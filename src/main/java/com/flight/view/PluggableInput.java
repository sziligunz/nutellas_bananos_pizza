package com.flight.view;

import com.flight.Main;
import com.flight.model.Validator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import lombok.SneakyThrows;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.*;

public interface PluggableInput<T> {
    T getItem();

    void setItem(T t);

    Property<T> itemProperty();

    default void setValidator(Optional<Validator> validator) {

    }

    default void setStringConverter(StringConverter<T> converter) {
    }

//    default boolean validate(T t) {
//        return true;
//    }


    class PluggableComboBox<T> extends ComboBox<T> implements PluggableInput<T> {
        private final Property<T> property = new SimpleObjectProperty<>();


        public PluggableComboBox(Collection<T> items) {
            super.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> property.setValue(newV));
            super.setEditable(false);
            ObservableList<T> observableItems = FXCollections.observableArrayList(items);
            super.setItems(observableItems);
        }

        @SneakyThrows
        static PluggableComboBox<?> fromAnnotation(View.SingleVale annotation) {
            if (annotation.list().length >= 1) return new PluggableComboBox<>(List.of(annotation.list()));

//            CrudRepository<?, ?> repository = annotation.listSupplier().getConstructor().newInstance();
            CrudRepository<?, ?> repository = Main.getRepositoryFor(annotation.listValueClass());
            Collection<Object> all = new LinkedList<>();
            repository.findAll().forEach(all::add);
            PluggableComboBox<Object> pluggableComboBox = new PluggableComboBox<>(all);

            if (annotation.attributeExtractor().isBlank()) return pluggableComboBox;


            pluggableComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Object object) {
                    if (Objects.isNull(object)) return null;
                    return new ReflectiveObjectProperty<>(object, annotation.attributeExtractor(), object.getClass()).get()
                            .toString();
                }

                @Override
                public Object fromString(String string) {
                    if (Objects.isNull(string)) return null;
                    return all.stream().filter(o -> Objects.equals(this.toString(o), string)).findAny().orElseThrow();
                }
            });

            return pluggableComboBox;
        }


        @Override
        public T getItem() {
            return super.getSelectionModel().getSelectedItem();
        }

        @Override
        public void setItem(T t) {
            super.getSelectionModel().select(t);
        }

        @Override
        public Property<T> itemProperty() {
            return property;
        }
    }


    class PluggableTextField<T> extends TextField implements PluggableInput<T> {
        private final ObjectProperty<T> objectProperty = new SimpleObjectProperty<>();
        private final Property<String> textProperty = new SimpleObjectProperty<>() {

            @Override
            public void setValue(String string) {
//                if (validate((T) string)) super.setValue(string);
//                else PluggableTextField.super.setText(super.get());

                super.setValue(string);
            }
        };
        private StringConverter<T> converter;
//        private Optional<Validator> validator = Optional.empty();

        {
            if (Objects.nonNull(super.getText())) textProperty.setValue(super.getText());

            super.focusedProperty().addListener((obs, oldV, newV) -> {
                if (!newV) textProperty.setValue(super.getText());
            });

            super.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (e.getCode() == KeyCode.ENTER) textProperty.setValue(super.getText());
                if (e.getCode() == KeyCode.ESCAPE) super.setText(textProperty.getValue());
                e.consume();
            });


            try {
                Bindings.bindBidirectional(textProperty, objectProperty, (StringConverter<T>) new DefaultStringConverter());
            } catch (Exception ignored) {

            }
        }

        @Override
        public T getItem() {
            return converter.fromString(textProperty.getValue());
        }

        @Override
        public void setItem(T t) {
            String string = converter.toString(t);
            super.setText(string);
            textProperty.setValue(string);
        }

        @Override
        public Property<T> itemProperty() {
            return objectProperty;
        }

//        @Override
//        public void setValidator(Optional<Validator> validator) {
//            this.validator = validator;
//        }


        @Override
        public void setStringConverter(StringConverter<T> converter) {
            this.converter = converter;
            Bindings.unbindBidirectional(textProperty, objectProperty);
            Bindings.bindBidirectional(textProperty, objectProperty, converter);
        }

//        @Override
//        public boolean validate(T t) {
//            if (t.getClass() != String.class) return true;
//            String string = (String) t;
//            return validator.stream().map(Validator::value).flatMap(Arrays::stream)
//                    .allMatch(stringValidator -> stringValidator.test(string));
//        }
    }


    class PluggableCheckBox extends CheckBox implements PluggableInput<Boolean> {

        @Override
        public Boolean getItem() {
            return super.isSelected();
        }

        @Override
        public void setItem(Boolean aBoolean) {
            super.setSelected(aBoolean);
        }

        @Override
        public Property<Boolean> itemProperty() {
            return super.selectedProperty();
        }
    }


    class PluggableDatePicker extends DatePicker implements PluggableInput<Date> {
        private final Property<Date> dateProperty = new SimpleObjectProperty<>();

        {
            if (Objects.nonNull(super.getValue()))
                dateProperty.setValue(Date.valueOf(super.valueProperty().getValue()));
            super.valueProperty().addListener((obs, oldV, newV) -> dateProperty.setValue(Date.valueOf(newV)));
        }

        @Override
        public Date getItem() {
            if (Objects.isNull(super.getValue())) return null;
            return Date.valueOf(super.getValue());
        }

        @Override
        public void setItem(Date date) {
            super.setValue(date.toLocalDate());
        }

        @Override
        public Property<Date> itemProperty() {
            return dateProperty;
        }
    }

}

package com.flight.view;

import com.flight.model.Validator;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class EditCell<IN extends Control & PluggableInput<T>, S, T> extends TableCell<S, T> {
    public EditCell(IN input, Optional<Validator> validator) {
        input.setValidator(validator);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(() -> {
            if (Objects.nonNull(getItem())) input.setItem(getItem());
            if (Objects.isNull(getItem())) setContentDisplay(ContentDisplay.TEXT_ONLY);
        });

        input.itemProperty().addListener((obs, oldV, newV) -> {
            super.getTableView().setEditable(true);
            super.getTableColumn().setEditable(true);
            super.setEditable(true);
            super.startEdit();

            super.getTableView().edit(super.getTableRow().getIndex(), super.getTableColumn());
            super.commitEdit(newV);

            super.setEditable(false);
            super.getTableColumn().setEditable(false);
            super.getTableView().setEditable(false);
        });

        setGraphic(input);
        setAlignment(Pos.CENTER);
    }

    public static <IN extends Control & PluggableInput<Object>> IN getPluggable(View.SingleVale annotation, boolean disabled) {
        Map<Class<?>, Supplier<PluggableInput<?>>> classToIN = Map.of(
                TextField.class, PluggableInput.PluggableTextField::new,
                CheckBox.class, PluggableInput.PluggableCheckBox::new,
                DatePicker.class, PluggableInput.PluggableDatePicker::new,
                ComboBox.class, () -> PluggableInput.PluggableComboBox.fromAnnotation(annotation)
        );


        IN in = (IN) classToIN.get(annotation.control()).get();
        in.setDisable(disabled);


        try {
            in.setStringConverter((StringConverter<Object>) annotation.stringConverter().getConstructor()
                    .newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return in;
    }

}
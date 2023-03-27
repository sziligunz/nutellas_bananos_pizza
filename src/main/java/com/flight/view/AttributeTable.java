package com.flight.view;

import com.flight.Main;
import com.flight.model.Validator;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AttributeTable<T> extends VBox {
    private static final Map<KeyCode, Consumer<TableView<?>>> KEY_TO_TABLE_ACTION = Map.of(
            KeyCode.DELETE, tableView -> tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem()),

            KeyCode.ESCAPE, tableView -> tableView.getSelectionModel().clearSelection()
    );

    private static final Consumer<TableView<?>> DO_NOTHING = tableView -> {
    };


    private final Class<T> type;

    public AttributeTable(Main main, ObservableList<T> items, Class<T> type, Runnable onCommitChanges) {
        this.type = type;

        TableView<T> tableView = new TableView<>();
        tableView.setItems(items);

        Button back = new Button("<-");
        back.setOnAction(e -> main.gotoMenu());


        getFields(type).stream()
                .filter(field -> field.getAnnotation(View.SingleVale.class) != null)
                .map(field -> {
                    View.SingleVale annotation = field.getAnnotation(View.SingleVale.class);
                    TableColumn<T, Object> tableColumn = new TableColumn<>(annotation.name());

                    tableColumn.setCellValueFactory(p -> new ReflectiveObjectProperty<>(p.getValue(), field.getName(), field.getType()));

                    tableColumn.setCellFactory(p -> new EditCell<>(
                            EditCell.getPluggable(annotation, false),
                            Optional.ofNullable(field.getAnnotation(Validator.class))
                    ));
                    tableColumn.setReorderable(false);

                    return tableColumn;
                })
                .forEach(tableView.getColumns()::add);


        tableView.getColumns().add(new TableColumn<T, Void>("Actions") {
            {
                setCellFactory(features -> new TableCell<>() {
                    private final Button deleteButton = new Button("delete");

                    {
                        deleteButton.setOnAction(e -> tableView.getItems().remove(getTableRow().getItem()));
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox container = new HBox();
                            container.getChildren().add(deleteButton);
                            setGraphic(container);
                        }
                    }
                });
            }
        });

        tableView.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            KEY_TO_TABLE_ACTION.getOrDefault(e.getCode(), DO_NOTHING).accept(tableView);
            tableView.refresh();
        });


        Pane pane = creationRow(tableView);

        Button commitChanges = new Button("Commit changes");
        commitChanges.setOnAction(e -> {
            onCommitChanges.run();
            tableView.refresh();
        });
        getChildren().addAll(back, tableView, pane, commitChanges);
    }


    private List<Field> getFields(Class<T> type) {
        List<Field> superFields = new ArrayList<>();

        if (Objects.nonNull(type.getAnnotation(View.IncludeSuper.class)))
            superFields.addAll(Arrays.asList(type.getSuperclass().getDeclaredFields()));

        superFields.addAll(Arrays.asList(type.getDeclaredFields()));
        return superFields;
    }


    private FlowPane creationRow(TableView<T> tableView) {
        FlowPane flowPane = new FlowPane();
//        its kinda hacky to have a Predicate with side effects.
        List<Predicate<T>> updaters = getFields(type).stream()
                .filter(field -> field.getAnnotation(View.SingleVale.class) != null)
                .map(field -> {
                    View.SingleVale annotation = field.getAnnotation(View.SingleVale.class);
                    PluggableInput<Object> input = EditCell.getPluggable(annotation, false);
                    Label label = new Label(annotation.name() + ": ");
                    label.setLabelFor((Node) input);
                    VBox vBox = new VBox(label, (Node) input);
                    vBox.setSpacing(5);
                    vBox.setAlignment(Pos.CENTER);
                    flowPane.getChildren().addAll(vBox);
                    input.setValidator(Optional.ofNullable(field.getAnnotation(Validator.class)));

                    return (Predicate<T>) v -> {
                        (new ReflectiveObjectProperty<>(v, field.getName(), field.getType())).set(input.getItem());
//                        return input.validate(input.getItem());
                        return true;
                    };
                })
                .toList();


        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            T t = newNoArgsInstance();
            boolean allValid = updaters.stream()
                    .map(updater -> updater.test(t))
                    .reduce(true, (a, b) -> a && b);
            if (!allValid) return;
            tableView.getItems().add(t);
            tableView.refresh();
        });

        addButton.setPrefWidth(120);

        flowPane.getChildren().add(addButton);
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        flowPane.setAlignment(Pos.CENTER_LEFT);

        return flowPane;
    }


    @SneakyThrows
    private T newNoArgsInstance() {
        return type.getConstructor().newInstance();
    }


}

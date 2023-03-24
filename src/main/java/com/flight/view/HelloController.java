package com.flight.view;

import com.flight.Main;
import com.flight.model.ClassClassifier;
import com.flight.repo.ClassClassifierRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class HelloController {
    private final ClassClassifierRepository repository;
    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void initialize() {
        System.out.println(repository.findAll());
        Pane root = (Pane) welcomeText.getParent();
        root.getChildren().clear();

        root.getChildren().add(Main.createAttributeTable(repository, ClassClassifier.class));
    }
}
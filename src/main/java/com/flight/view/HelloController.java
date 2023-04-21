package com.flight.view;

import com.flight.Main;
import com.flight.model.ClassClassifier;
import com.flight.model.Hotel;
import com.flight.repo.ClassClassifierRepository;
import com.flight.repo.HotelRepository;
import com.flight.repo.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.BufferedReader;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class HelloController {
    private final HotelRepository repository;
    private final UserRepository userRepository;
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
    }
}
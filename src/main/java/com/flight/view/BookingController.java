package com.flight.view;

import com.flight.model.Airport;
import com.flight.model.Schedule;
import com.flight.repo.ScheduleRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class BookingController implements Initializable {

    private final ScheduleRepository repository;
    @FXML
    private ComboBox<String> depCombo;
    @FXML
    private ComboBox<String> arCombo;
    @FXML
    private DatePicker date;
    @FXML
    private CheckBox child;
    @FXML
    private CheckBox meal;
    @FXML
    private TableView<Schedule> tableView;
    @FXML
    private TableColumn<Schedule, String> depCol;
    @FXML
    private TableColumn<Schedule, String> arCol;
    @FXML
    private TableColumn<Schedule, Date> dateCol;
    @FXML
    private TableColumn<Schedule, String> childCol;
    @FXML
    private TableColumn<Schedule, String> mealCol;
    @FXML
    private TableColumn<Schedule, Void> bookCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> departures = FXCollections.observableArrayList(repository.getDepartures());
        depCombo.setItems(departures);
        ObservableList<String> arrivals = FXCollections.observableArrayList(repository.getArrivals());
        arCombo.setItems(arrivals);

        List<Schedule> list = new LinkedList<>();
        repository.findAll().forEach(list::add);
        System.out.println(list);
        tableView.getItems().setAll(list);
        System.out.println(tableView.getItems());

        depCol.setCellValueFactory(p -> {
            String city = p.getValue().getFlight().getDeparture().getCity();
            return new SimpleStringProperty(city);
        });
        arCol.setCellValueFactory(p -> {
            String city = p.getValue().getFlight().getDeparture().getCity();
            return new SimpleStringProperty(city);
        });
        dateCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        childCol.setCellValueFactory(p -> {
            String city = p.getValue().getFlight().isChildFriendly()?"Yes":"No";
            return new SimpleStringProperty(city);
        });
        mealCol.setCellValueFactory(p -> {
            String city = p.getValue().getFlight().isMealAvailable()?"Yes":"No";
            return new SimpleStringProperty(city);
        });
        bookCol.setCellFactory(param -> new TableCell<>(){
            private final Button bookBtn = new Button("Book");

            {
                bookBtn.setOnAction(event -> {
                    System.out.println(getTableRow().getItem());
                });
            }
        });
    }
}

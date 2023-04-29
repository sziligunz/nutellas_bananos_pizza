package com.flight.view;

import com.flight.model.Airport;
import com.flight.model.Schedule;
import com.flight.model.User;
import com.flight.repo.ScheduleRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.sql.Date;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class BookingController implements Initializable {

    private Stage stage;
    private Scene scene;
    public User user;
    public ConfigurableApplicationContext springContext;
    private final ScheduleRepository repository;
    @FXML
    private ComboBox<String> depCombo;
    @FXML
    private ComboBox<String> arCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox childBox;
    @FXML
    private CheckBox mealBox;
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
    @FXML
    private Button search;
    @FXML
    private Button reset;

    private List<Schedule> list;

    private void searchSchedule(){
        ObservableList<Schedule> objectObservableList = tableView.getItems().stream().filter(e->{
            boolean dep = depCombo.getValue()==null || Objects.equals(e.getFlight().getDeparture().getCity(), depCombo.getValue());
            boolean ar = arCombo.getValue()==null || Objects.equals(e.getFlight().getArrival().getCity(), arCombo.getValue());
            boolean date = datePicker.getValue()==null || Objects.equals(e.getDepartureTime(), Date.valueOf(datePicker.getValue()));
            boolean child = childBox.isSelected() == e.getFlight().isChildFriendly();
            boolean meal = mealBox.isSelected() == e.getFlight().isMealAvailable();
            //System.out.println("Dep: " + dep + " Ar: " + ar + " Date:" + date + " Child:" + child + " Meal: "+ meal);
            return dep && ar && date && child && meal;
        }).collect(FXCollections::observableArrayList, List::add, List::addAll);
        tableView.setItems(objectObservableList);
    }

    private void resetFilter(){
        tableView.getItems().setAll(list);
    }

    private void booking(Schedule schedule) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("book.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();
        BookController controller = loader.getController();
        controller.springContext = this.springContext;
        controller.user = this.user;
        controller.schedule = schedule;
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> departures = FXCollections.observableArrayList(repository.getDepartures());
        depCombo.setItems(departures);
        ObservableList<String> arrivals = FXCollections.observableArrayList(repository.getArrivals());
        arCombo.setItems(arrivals);

        search.setOnAction(e->searchSchedule());
        reset.setOnAction(e->resetFilter());

        list = new LinkedList<>();
        repository.findAll().forEach(list::add);
        resetFilter();
        depCol.setCellValueFactory(p -> {
            String city = p.getValue().getFlight().getDeparture().getCity();
            return new SimpleStringProperty(city);
        });
        arCol.setCellValueFactory(p -> {
            String city = p.getValue().getFlight().getArrival().getCity();
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
        bookCol.setCellFactory(param ->  new TableCell<>(){
            private final Button bookBtn = new Button("Book");

            {
                bookBtn.setOnAction(event -> {
                    try {
                        booking(getTableRow().getItem());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                //System.out.println(bookBtn);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                }
                else{
                    setGraphic(bookBtn);
                }
            }

        });
    }
}
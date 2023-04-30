package com.flight.view;


import com.flight.Main;
import com.flight.model.Booking;
import com.flight.model.ClassClassifier;
import com.flight.model.Schedule;
import com.flight.model.User;
import com.flight.repo.BookingRepository;
import com.flight.repo.ClassClassifierRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class MyBooksController {



    public ConfigurableApplicationContext springContext;
    public User user;
    public Schedule schedule;
    public Main main;
    @FXML
    private TableView<Booking> tableView;
    @FXML
    private TableColumn<Booking, String> depCol;
    @FXML
    private TableColumn<Booking, String> arCol;
    @FXML
    private TableColumn<Booking, Date> dateCol;
    @FXML
    private TableColumn<Booking, Integer> seatCol;
    @FXML
    private TableColumn<Booking, String> classCol;
    @FXML
    private TableColumn<Booking, Integer> priceCol;
    @FXML
    private TableColumn<Booking, String> insCol;
    @FXML
    private TableColumn<Booking, Void> actionCol;

    private final ClassClassifierRepository classClassifierRepository;
    private final BookingRepository bookingRepository;
    private List<Booking> pickedList;
    private List<Booking> removedList;
    private List<ClassClassifier> classClassifiers;

    public void getPicked() {

    }

    public void listInit() {
        pickedList = bookingRepository.getBookingsByUser(user);
        classClassifiers = new LinkedList<>();
        classClassifierRepository.findAll().forEach(e->classClassifiers.add(e));
        removedList = new LinkedList<>();
    }

    public void back(){
        Stage stage = (Stage)tableView.getScene().getWindow();
        main.loadMenu();
        stage.setScene(main.menu);
    }
    public void save(){
        bookingRepository.deleteAll(removedList);
        removedList.clear();
    }

    public void refreshTable(){
        tableView.getItems().clear();
        tableView.getItems().addAll(pickedList);
    }

    public void init(){
        listInit();
        tableView.getItems().addAll(pickedList);

        depCol.setCellValueFactory(p -> {
            String city = p.getValue().getSchedule().getFlight().getDeparture().getCity();
            return new SimpleStringProperty(city);
        });
        arCol.setCellValueFactory(p -> {
            String city = p.getValue().getSchedule().getFlight().getArrival().getCity();
            return new SimpleStringProperty(city);
        });
        dateCol.setCellValueFactory(p -> {
            Date city = p.getValue().getSchedule().getDepartureTime();
            return new SimpleObjectProperty<>(city);
        });
        seatCol.setCellValueFactory(p -> {
            int seatNumber = p.getValue().getSeatNumber();
            return new SimpleObjectProperty<>(seatNumber);
        });
        classCol.setCellValueFactory(p -> {
            Optional<String> clazzOptional = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()  && obj.getSchedule().equals(p.getValue().getSchedule())).map(ClassClassifier::getClazz).findFirst();
            String clazz = clazzOptional.orElse("");
            return new SimpleStringProperty(clazz);
        });
        priceCol.setCellValueFactory(p -> {
            Optional<String> clazzOptional  = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber() && obj.getSchedule().equals(p.getValue().getSchedule())).map(ClassClassifier::getClazz).findFirst();
            int price = 0;
            String clazz = clazzOptional.orElse("");
            switch (clazz) {
                case "comercial" -> price = p.getValue().getSchedule().getCommercialPrice();
                case "business" -> price = p.getValue().getSchedule().getBusinessPrice();
                case "first" -> price = p.getValue().getSchedule().getFirstClassPrice();
            }
            return new SimpleObjectProperty<>(price);
        });
        insCol.setCellValueFactory(p -> {
            String clazz = p.getValue().getInsurancePackage().getName();
            return new SimpleStringProperty(clazz);
        });
        actionCol.setCellFactory(param ->  new TableCell<>(){
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(event ->{
                    //availableList.add(getTableRow().getItem());
                    removedList.add(getTableRow().getItem());
                    pickedList.remove(getTableRow().getItem());
                    refreshTable();
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
                    setGraphic(deleteBtn);
                }
            }

        });
    }
}

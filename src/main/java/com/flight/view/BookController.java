package com.flight.view;

import com.flight.model.*;
import com.flight.repo.BookingRepository;
import com.flight.repo.ClassClassifierRepository;
import com.flight.repo.InsurancePackageRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


@Component
@Scope("prototype")
@RequiredArgsConstructor
public class BookController implements Initializable {
    public ConfigurableApplicationContext springContext;
    public User user;
    public Schedule schedule;

    @FXML
    private TableView<Booking> avaTableView;
    @FXML
    private TableColumn<Booking, Integer> avaSeatCol;
    @FXML
    private TableColumn<Booking, String> avaClassCol;
    @FXML
    private TableColumn<Booking, Integer> avaPriceCol;
    @FXML
    private TableColumn<Booking, Void> avaInsureCol;
    @FXML
    private TableColumn<Booking, Void> avaActionCol;

    @FXML
    private TableView<Booking> pickTableView;
    @FXML
    private TableColumn<Booking, Integer> pickSeatCol;
    @FXML
    private TableColumn<Booking, String> pickClassCol;
    @FXML
    private TableColumn<Booking, Integer> pickPriceCol;
    @FXML
    private TableColumn<Booking, Void> pickInsureCol;
    @FXML
    private TableColumn<Booking, Void> pickActionCol;

    private List<Booking> availableList;
    private List<Booking> pickedList;
    private List<ClassClassifier> classClassifiers;

    private final ClassClassifierRepository classClassifierRepository;
    private final BookingRepository bookingRepository;
    private final InsurancePackageRepository insurancePackageRepository;

    private void generateClassClassifier(){
        List<ClassClassifier> generateList = new LinkedList<>();
        for(int i = 1; i <= schedule.getPlane().getCommercialCapacity(); i++){
            ClassClassifier temp = new ClassClassifier();
            temp.setClazz("comercial");
            temp.setSeatNumber(i);
            temp.setSchedule(schedule);
            generateList.add(temp);

        }
        int maxBusiness = schedule.getPlane().getCommercialCapacity() + schedule.getPlane().getBusinessCapacity();
        for(int i = schedule.getPlane().getCommercialCapacity() + 1; i <= maxBusiness; i++){
            ClassClassifier temp = new ClassClassifier();
            temp.setClazz("business");
            temp.setSeatNumber(i);
            temp.setSchedule(schedule);
            generateList.add(temp);
        }
        int maxFirst = schedule.getPlane().getCommercialCapacity() + schedule.getPlane().getBusinessCapacity() + schedule.getPlane().getFirstClassCapacity();
        for(int i = maxBusiness + 1; i < maxFirst; i++){
            ClassClassifier temp = new ClassClassifier();
            temp.setClazz("first");
            temp.setSeatNumber(i);
            temp.setSchedule(schedule);
            generateList.add(temp);
        }
        classClassifierRepository.saveAll(generateList);
    }

    private void generateTables(){
        int first = schedule.getPlane().getFirstClassCapacity();
        int business = schedule.getPlane().getBusinessCapacity();
        int comercial = schedule.getPlane().getCommercialCapacity();
        availableList = new LinkedList<>();
        classClassifiers = classClassifierRepository.getClassClassifierBySchedule(schedule);
        pickedList = bookingRepository.getBookingsByScheduleAnAndUser(schedule, user);
        for (int i = 1; i <= first + business + comercial; i++){
            Booking temp = new Booking();
            temp.setSeatNumber(i);
            temp.setSchedule(schedule);
            availableList.add(temp);
        }
        availableList.removeAll(pickedList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (classClassifierRepository.getClassClassifierBySchedule(this.schedule) == null){
            generateClassClassifier();
        }
        LinkedList<String> linkedList = new LinkedList<>();
        insurancePackageRepository.findAll().forEach(e->linkedList.add(e.getName()));
        ObservableList<String> insurancePackeges = FXCollections.observableArrayList(linkedList);
        generateTables();
        avaTableView.getItems().addAll(availableList);
        avaSeatCol.setCellValueFactory(p -> {
            int seatNumber = p.getValue().getSeatNumber();
            return new SimpleObjectProperty<>(seatNumber);
        });
        avaClassCol.setCellValueFactory(p -> {
            String clazz = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()).map(ClassClassifier::getClazz).toList().get(0);
            return new SimpleStringProperty(clazz);
        });
        avaPriceCol.setCellValueFactory(p -> {
            String temp  = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()).map(ClassClassifier::getClazz).toList().get(0);
            Integer price = 0;
            switch (temp) {
                case "comercial" -> price = p.getValue().getSchedule().getCommercialPrice();
                case "business" -> price = p.getValue().getSchedule().getCommercialPrice();
                case "first" -> price = p.getValue().getSchedule().getCommercialPrice();
            }
            return new SimpleObjectProperty<>(price);
        });
        avaInsureCol.setCellFactory(param ->  new TableCell<>(){
            private final ComboBox<String> insurancePackageCombobox = new ComboBox<>();

            {
                insurancePackageCombobox.setItems(insurancePackeges);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                }
                else{
                    setGraphic(insurancePackageCombobox);
                }
            }

        });
        avaActionCol.setCellFactory(param ->  new TableCell<>(){
            private final Button bookBtn = new Button("Book");

            {
                bookBtn.setOnAction(event ->{});
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

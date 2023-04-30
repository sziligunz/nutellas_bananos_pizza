package com.flight.view;

import com.flight.Main;
import com.flight.model.*;
import com.flight.repo.BookingRepository;
import com.flight.repo.ClassClassifierRepository;
import com.flight.repo.InsurancePackageRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


@Component
@Scope("prototype")
@RequiredArgsConstructor
public class BookController {
    public ConfigurableApplicationContext springContext;
    public User user;
    public Schedule schedule;
    public Main main;

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
    private TableColumn<Booking, String> pickInsureCol;
    @FXML
    private TableColumn<Booking, Void> pickActionCol;

    private List<Booking> availableList;
    private List<Booking> pickedList;
    private List<ClassClassifier> classClassifiers;
    private LinkedList<String> packages;
    private final ClassClassifierRepository classClassifierRepository;
    private final BookingRepository bookingRepository;
    private final InsurancePackageRepository insurancePackageRepository;

    public void setSchedule(Schedule schedule, User user){
        System.out.println("SETTER");
        this.schedule = schedule;
        this.user = user;
        System.out.println(this.schedule);
        init();
    }
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
        for(int i = maxBusiness + 1; i <= maxFirst; i++){
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
        pickedList = bookingRepository.getBookingsBySchedule(schedule);
        for (int i = 1; i <= first + business + comercial; i++){
            Booking temp = new Booking();
            temp.setSeatNumber(i);
            temp.setSchedule(schedule);
            temp.setUser(user);
            availableList.add(temp);
        }
        pickedList.forEach(e->e.setUser(user));
        availableList.removeAll(pickedList);
        pickedList.clear();
    }

    public void refreshTables(){
        pickTableView.getItems().clear();
        avaTableView.getItems().clear();
        avaTableView.getItems().addAll(availableList);
        pickTableView.getItems().addAll(pickedList);
        avaTableView.refresh();
        pickTableView.refresh();
    }

    public void save(){
        System.out.println(pickedList);
        bookingRepository.saveAll(pickedList);
    }
    public void init() {
        System.out.println("INIT");
        System.out.println(this.schedule);
        System.out.println(classClassifierRepository.getClassClassifierBySchedule(this.schedule));
        int sum = schedule.getPlane().getBusinessCapacity() + schedule.getPlane().getCommercialCapacity() + schedule.getPlane().getFirstClassCapacity();
        List<ClassClassifier> classifierList = classClassifierRepository.getClassClassifierBySchedule(this.schedule);
        System.out.println(sum + "=?=" + classifierList.size());
        if (classifierList.size() != sum ){
            System.out.println("Generating...");
            generateClassClassifier();
        }
        packages = new LinkedList<>();
        insurancePackageRepository.findAll().forEach(e->packages.add(e.getName()));
        ObservableList<String> insurancePackeges = FXCollections.observableArrayList(packages);
        generateTables();
        {
            avaTableView.getItems().addAll(availableList);
            avaSeatCol.setCellValueFactory(p -> {
                int seatNumber = p.getValue().getSeatNumber();
                return new SimpleObjectProperty<>(seatNumber);
            });
            avaClassCol.setCellValueFactory(p -> {
                Optional<String> clazzOptional = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()).map(ClassClassifier::getClazz).findFirst();
                String clazz = clazzOptional.orElse("");
                return new SimpleStringProperty(clazz);
            });
            avaPriceCol.setCellValueFactory(p -> {
                Optional<String> clazzOptional = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()).map(ClassClassifier::getClazz).findFirst();
                int price = 0;
                String clazz = clazzOptional.orElse("");
                switch (clazz) {
                    case "comercial" -> price = p.getValue().getSchedule().getCommercialPrice();
                    case "business" -> price = p.getValue().getSchedule().getBusinessPrice();
                    case "first" -> price = p.getValue().getSchedule().getFirstClassPrice();
                }
                return new SimpleObjectProperty<>(price);
            });
            avaInsureCol.setCellFactory(param -> new TableCell<>() {
                private final ComboBox<String> insurancePackageCombobox = new ComboBox<>();

                {
                    insurancePackageCombobox.setItems(insurancePackeges);
                    insurancePackageCombobox.setOnAction(e->{
                        String selected = insurancePackageCombobox.getSelectionModel().getSelectedItem();
                        getTableRow().getItem().setInsurancePackage(insurancePackageRepository.getInsurancePackageByName(selected).orElse(null));
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(insurancePackageCombobox);
                    }
                }

            });
            avaActionCol.setCellFactory(param -> new TableCell<>() {
                private final Button bookBtn = new Button("Book");

                {
                    bookBtn.setOnAction(event -> {
                        Booking booking = getTableRow().getItem();
                        if(booking.getInsurancePackage() != null){
                            pickedList.add(booking);
                            availableList.remove(booking);
                            refreshTables();
                        }
                    });
                    //System.out.println(bookBtn);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(bookBtn);
                    }
                }

            });
        }

        {
            pickTableView.getItems().addAll(pickedList);
            pickSeatCol.setCellValueFactory(p -> {
                int seatNumber = p.getValue().getSeatNumber();
                return new SimpleObjectProperty<>(seatNumber);
            });
            pickClassCol.setCellValueFactory(p -> {
                Optional<String> clazzOptional = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()).map(ClassClassifier::getClazz).findFirst();
                String clazz = clazzOptional.orElse("");
                return new SimpleStringProperty(clazz);
            });
            pickPriceCol.setCellValueFactory(p -> {
                Optional<String> clazzOptional  = classClassifiers.stream().filter(obj -> obj.getSeatNumber() == p.getValue().getSeatNumber()).map(ClassClassifier::getClazz).findFirst();
                int price = 0;
                String clazz = clazzOptional.orElse("");
                switch (clazz) {
                    case "comercial" -> price = p.getValue().getSchedule().getCommercialPrice();
                    case "business" -> price = p.getValue().getSchedule().getBusinessPrice();
                    case "first" -> price = p.getValue().getSchedule().getFirstClassPrice();
                }
                return new SimpleObjectProperty<>(price);
            });
            pickInsureCol.setCellValueFactory(p -> {
                String clazz = p.getValue().getInsurancePackage().getName();
                return new SimpleStringProperty(clazz);
            });
            pickActionCol.setCellFactory(param ->  new TableCell<>(){
                private final Button deleteBtn = new Button("Delete");

                {
                    deleteBtn.setOnAction(event ->{
                        availableList.add(getTableRow().getItem());
                        pickedList.remove(getTableRow().getItem());
                        refreshTables();
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

    public void back() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("flight-book.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();
        BookingController controller = loader.getController();
        controller.springContext = this.springContext;
        controller.user = this.user;
        controller.main = this.main;
        controller.init();
        Scene scene = new Scene(root);
        Stage stage = (Stage)avaTableView.getScene().getWindow();
        stage.setScene(scene);
    }
}

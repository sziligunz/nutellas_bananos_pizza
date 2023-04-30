package com.flight.view;

import com.flight.Main;
import com.flight.model.Schedule;
import com.flight.model.User;
import com.flight.repo.UserRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import oracle.sql.INTERVALDS;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Arrays;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class ComplexController {

    private final UserRepository userRepository;
    public ConfigurableApplicationContext springContext;
    public User user;
    public Schedule schedule;
    public Main main;
    @FXML
    private Text ipb_output;
    @FXML
    private Text ipms_output;
    @FXML
    private Text ipbat_output;
    @FXML
    private Text afvra_output;
    @FXML
    private Text tbof_output;
    @FXML
    private Text mofpd_output;

    public void back(){
        Stage stage = (Stage)ipb_output.getScene().getWindow();
        main.loadMenu();
        //controller.initialize();
        //Scene scene = new Scene(root);
        //stage = main.stage;
        stage.setScene(main.menu);
    }
    public void initialize() {
        String[] res = {"Departure time\tInsurance package\tMoney\n"};
        userRepository.insurance_packages_bought().stream().map(Arrays::toString).map(e -> e.replace(",", "\t").replace("[", "").replace("]", "").replace("00:00:00.0", "") + "\n").forEach(e -> res[0] += e);
        ipb_output.setText(res[0]);

        res[0] = "Departure time\tInsurance package\tDB\n";
        userRepository.insurance_packages_money_summed().stream().map(Arrays::toString).map(e -> e.replace(",", "\t").replace("[", "").replace("]", "").replace("00:00:00.0", "") + "\n").forEach(e -> res[0] += e);
        ipms_output.setText(res[0]);

        res[0] = "Insurance package\tDB\n";
        userRepository.insurance_packages_bought_all_time().stream().map(Arrays::toString).map(e -> e.replace(",", "\t").replace("[", "").replace("]", "") + "\n").forEach(e -> res[0] += e);
        ipbat_output.setText(res[0]);

        res[0] = "Airplane model\tAirport\n";
        userRepository.airplanes_fly_from_vaccine().stream().map(Arrays::toString).map(e -> e.replace(",", "\t").replace("[", "").replace("]", "") + "\n").forEach(e -> res[0] += e);
        afvra_output.setText(res[0]);

        res[0] = "Airplane model\tDeparture time\tDB\n";
        userRepository.seats_reserved_on_flight().stream().map(Arrays::toString).map(e -> e.replace(",", "\t").replace("[", "").replace("]", "").replace("00:00:00.0", "") + "\n").forEach(e -> res[0] += e);
        tbof_output.setText(res[0]);

        res[0] = "Departure time\tDuration (minutes)\n";
        userRepository.minutes_of_flight_per_day().stream().map(Arrays::toString).map(e -> e.replace(",", "\t").replace("[", "").replace("]", "").replace("00:00:00.0", "") + "\n").forEach(e -> res[0] += e);
        mofpd_output.setText(res[0]);
    }

}

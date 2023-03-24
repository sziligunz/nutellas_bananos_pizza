open module com.flight {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    requires spring.data.commons;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.core;
    requires spring.data.jpa;
    requires ojdbc8;


//    opens com.flight.gui to javafx.fxml, spring.core;
//    opens com.flight.repo to spring.core;
//    opens com.flight to javafx.fxml, spring.core;
    exports com.flight.view;
    exports com.flight.repo;
    exports com.flight.model;
    exports com.flight;
}
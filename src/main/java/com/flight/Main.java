package com.flight;

import com.flight.view.AttributeTable;
import com.flight.view.HelloController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class Main extends Application {
    private static Repositories repositories;
    private ConfigurableApplicationContext springContext;
    private Parent root;


    public static void main(String[] args) {
        launch(Main.class, args);
    }


    public static <T> CrudRepository<T, ?> getRepositoryFor(Class<T> tClass) {
        return (CrudRepository<T, ?>) repositories.getRepositoryFor(tClass).orElseThrow();
    }

    public static <T> AttributeTable<T> createAttributeTable(CrudRepository<T, ?> repository, Class<T> type) {
        List<T> all = new LinkedList<>();
        repository.findAll().forEach(all::add);
        List<T> removed = new LinkedList<>();
        ObservableList<T> observableList = FXCollections.observableArrayList(all);


        observableList.addListener((ListChangeListener<? super T>) change -> {
            change.next();
            if (change.wasRemoved()) {
                removed.addAll(change.getRemoved());
                all.removeAll(change.getRemoved());
            }
        });

        return new AttributeTable<>(observableList, type, () -> {
//            IntStream.range(0, all.size()).forEach(i -> {
//                repository.save(observableList.get(i));
//            });
//
//            IntStream.range(all.size(), observableList.size()).forEach(i -> {
//                repository.save(observableList.get(i));
//            });

//            System.out.println("all: " + all);
//            System.out.println("obs: " + observableList);


            List<T> a = new ArrayList<>();
            repository.findAll().forEach(a::add);

            a.removeAll(observableList);


            repository.saveAll(observableList);
            repository.deleteAll(removed);
            repository.deleteAll(a);
            removed.clear();
        });
    }

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(Main.class).web(WebApplicationType.NONE).run();
        Main.repositories = new Repositories(springContext);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("hello-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();

//        root = createAttributeTable(hotelRepository, Hotel.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

//        System.out.println(planeRepository.findAll());
    }

    @Override
    public void stop() {
        springContext.stop();
        Platform.exit();
    }
}
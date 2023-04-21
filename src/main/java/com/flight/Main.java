package com.flight;

import com.flight.model.Hotel;
import com.flight.model.User;
import com.flight.repo.UserRepository;
import com.flight.view.AttributeTable;
import com.flight.view.HelloController;
import com.flight.view.UserController;
import com.flight.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaRepositories
public class Main extends Application {
    private static Repositories repositories;
    private ConfigurableApplicationContext springContext;
    private UserController userController;
    private Stage stage;
    private Scene menu;
    public User user;


    public static void main(String[] args) {
        launch(Main.class, args);
    }


    public static <T> CrudRepository<T, ?> getRepositoryFor(Class<T> tClass) {
        return (CrudRepository<T, ?>) repositories.getRepositoryFor(tClass).orElseThrow();
    }

    public <T> AttributeTable<T> createAttributeTable(Class<T> type) {
        CrudRepository<T, ?> repository = getRepositoryFor(type);
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

        return new AttributeTable<>(this, observableList, type, () -> {
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
    public void init() throws IOException {
        springContext = new SpringApplicationBuilder(Main.class).web(WebApplicationType.NONE).run();
        Main.repositories = new Repositories(springContext);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("hello-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
//        root = fxmlLoader.load();


        userController = new UserController((UserRepository)getRepositoryFor(User.class));
        VBox menuRoot = new VBox();
        menuRoot.setAlignment(Pos.CENTER);
        menuRoot.setSpacing(5);

        //TODO login, register, logout gombok csinálnak dolgokat
        Button login = new Button("Login");
        login.setOnAction(e -> {
            stage.setScene(sceneGen("login"));
        });
        Button register = new Button("Register");
        Button logout = new Button("Logout");
        menuRoot.getChildren().add(login);

        try (
                InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("com.flight.model".replaceAll("[.]", "/"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)))
        ) {
            reader.lines().filter(line -> line.endsWith(".class"))
                    .map(line -> {
                        try {
                            return Class.forName("com.flight.model." + line.substring(0, line.lastIndexOf('.')));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(clazz -> Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(View.SingleVale.class)))
                    .forEach(clazz -> {
                        Button button = new Button(clazz.getSimpleName());

                        button.setOnAction(e -> stage.setScene(new Scene(createAttributeTable(clazz))));

                        menuRoot.getChildren().add(button);
                    });
        }

        menu = new Scene(menuRoot);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(menu);
        stage.show();
    }

    public Scene sceneGen(String name){
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        Button back = new Button("Back");
        back.setOnAction(e->gotoMenu());
        box.getChildren().add(back);
        if(name.equals("login")){
            System.out.println("login");
            Label email = new Label("Email");
            Label pw = new Label("Password");
            TextField emailTF = new TextField();
            TextField pwTF = new TextField();
            Button login = new Button("Login");
            login.setOnAction(e->{
                try{
                    user = userController.login(emailTF.getText(), pwTF.getText());
                    gotoMenu();
                }
                catch (Exception exception){
                    System.err.println(exception);
                }
            });
            box.getChildren().add(email);
            box.getChildren().add(emailTF);
            box.getChildren().add(pw);
            box.getChildren().add(pwTF);
            box.getChildren().add(login);
            return new Scene(box);
        }
        return null;
    }

    @Override
    public void stop() {
        springContext.stop();
        Platform.exit();
    }

    public void gotoMenu() {
        stage.setScene(menu);
        System.out.println(user);
    }
}
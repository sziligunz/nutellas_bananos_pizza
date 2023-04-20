package com.flight.view;

import com.flight.model.User;
import com.flight.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Iterator;
import java.util.Objects;

@Component
@Scope("prototype")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    public String login(String email, String pw){
        if(userRepository.getUserByEmail(email).isEmpty()){
            return "Nincs ilyen felhasználó";
        }
        User user = userRepository.getUserByEmail(email).get();
        if(user.getEmail().equals(email) && user.getHashedPassword().equals(pw)){
            return "Siker";
        }
        return "Nem egyezik a jelszó vagy email";
    }

    public String registerUser(String email, String name, String pw){
        if (Objects.equals(email, "") || Objects.equals(pw, "") || Objects.equals(name, "") ) {
            return "Failed";
        }
        if(userRepository.getUserByEmail(email).isPresent()){
            return "Failed";
        }
        Iterable<User> users = userRepository.findAll();
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(pw);
        user.setPrivilege("user");

        try {

            userRepository.save(user);
        } catch (Exception ignored) {
            return "Failed";
        }
        return "Success";
    }
}

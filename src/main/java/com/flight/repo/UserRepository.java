package com.flight.repo;

import com.flight.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT user FROM User user WHERE user.email = :#{#email}")
    Optional<User> getUserByEmail(@Param("email") String email);


    @Query("SELECT user FROM User user WHERE user.email = :#{#email} and user.hashedPassword = :#{#pw}")
    Optional<User> getUserByEmailAndHashedPassword(@Param("email") String email, @Param("pw") String pw);


    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.privilege = :#{#newRole} WHERE user.email = :#{#email}")
    void updateUserRole(@Param("email") int email, @Param("newRole") String newRole);


    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.hashedPassword = :#{#newPassword} WHERE user.email = :#{#email}")
    void updateUserPassword(@Param("email") int email, @Param("newPassword") String newPassword);
}
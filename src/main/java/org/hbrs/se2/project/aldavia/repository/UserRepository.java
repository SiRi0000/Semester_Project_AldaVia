package org.hbrs.se2.project.aldavia.repository;

import org.hbrs.se2.project.aldavia.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // SELECT firstname, lastname, id
    // FROM User p
    // WHERE p.userid = [StringValueOf( userid )] AND p.password = [StringValueOf( password )]
    Optional<User> findUserByUseridAndPassword ( String userid , String password);

    Optional<User> findUserByEmail(String mail);

    Optional<User> findByUserid(String username);

    Optional<User> findUserByUserid(String userId);

    Optional<User> findUserByEmailAndPassword ( String userid , String password);

    void deleteByUserid(String userName);
}
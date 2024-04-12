package com.demo.sqlite.repositories;

import com.demo.sqlite.models.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    @Query(value = "SELECT u FROM clients u WHERE u.email = :email AND u.password_hash = :password_hash")
    Optional<Client> findClientByEmailAndPassword(@Param("email") String email, @Param("password_hash") String passwordHash);

    @Query(value = "SELECT u.id FROM clients u WHERE u.email = :email")
    Optional<Integer> existEmail(@Param("email") String email);

}

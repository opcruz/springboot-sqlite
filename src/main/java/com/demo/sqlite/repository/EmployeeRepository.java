package com.demo.sqlite.repository;

import com.demo.sqlite.model.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
   @Query(value = "SELECT u FROM employees u WHERE u.email = :email AND u.passwordHash = :password_hash")
   Optional<Employee> findEmployeeByEmailAndPassword(@Param("email") String email,
         @Param("password_hash") String passwordHash);

   @Query(value = "SELECT u.id FROM employees u WHERE u.email = :email")
   Optional<Integer> existEmail(@Param("email") String email);

}

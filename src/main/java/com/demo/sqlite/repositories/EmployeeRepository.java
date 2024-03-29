package com.demo.sqlite.repositories;

import com.demo.sqlite.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    @Query(value = "SELECT u FROM employees u WHERE u.email = :email AND u.passwordhash = :password_hash")
    Optional<Employee> loginEmployee(@Param("email") String email, @Param("password_hash") String passwordHash);

}

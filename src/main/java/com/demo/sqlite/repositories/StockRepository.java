package com.demo.sqlite.repositories;

import com.demo.sqlite.models.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

    @Query(value = "SELECT new com.demo.sqlite.models.Stock(u.code, u.description, u.color, u.category, u.quantity, u.price," +
            " u.status, u.created_by, u.updated_by) FROM stock u")
    Iterable<Stock> allWithoutImage();

    @Query(value = "SELECT new com.demo.sqlite.models.Stock(u.code, u.description, u.color, u.category, u.quantity, u.price," +
            " u.status, u.created_by, u.updated_by) FROM stock u WHERE u.description LIKE %:phrase%")
    Iterable<Stock> findByName(@Param("phrase") String phrase);

    @Query(value = "SELECT u.image FROM stock u WHERE u.code = :code")
    Optional<byte[]> findImageByCode(@Param("code") Integer code);

}

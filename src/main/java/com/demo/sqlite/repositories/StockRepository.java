package com.demo.sqlite.repositories;

import com.demo.sqlite.dtos.StockResponseDTO;
import com.demo.sqlite.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

    @Query(value = "SELECT new com.demo.sqlite.dtos.StockResponseDTO(u.code, u.description, s.id,s.category, s.description," +
            " u.quantity, u.price, u.status, u.created_by, u.updated_by) FROM stock u JOIN categories s ON u.category_id = s.id" +
            " WHERE (:phrase IS NULL OR u.description LIKE %:phrase%)")
    Page<StockResponseDTO> filterByPhraseAndPagination(@Param("phrase") String phrase, Pageable pageable);

    @Query(value = "SELECT new com.demo.sqlite.dtos.StockResponseDTO(u.code, u.description, s.id,s.category, s.description," +
            " u.quantity, u.price, u.status, u.created_by, u.updated_by) FROM stock u JOIN categories s ON u.category_id = s.id" +
            " WHERE u.code = :code")
    Optional<StockResponseDTO> findStockResponseDTOById(@Param("code") Integer code);

    @Query(value = "SELECT u.image FROM stock u WHERE u.code = :code")
    Optional<byte[]> findImageByCode(@Param("code") Integer code);

}

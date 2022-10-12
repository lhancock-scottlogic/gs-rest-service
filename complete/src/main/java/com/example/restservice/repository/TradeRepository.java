package com.example.restservice.repository;
import com.example.restservice.Trade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<Trade,Integer> {
}

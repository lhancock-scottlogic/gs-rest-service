package com.example.restservice.repository;
import com.example.restservice.UserOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<UserOrder,Integer> {
}


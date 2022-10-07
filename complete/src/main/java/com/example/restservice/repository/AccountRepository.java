package com.example.restservice.repository;
import com.example.restservice.model.AccountModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountModel,Integer> {
}

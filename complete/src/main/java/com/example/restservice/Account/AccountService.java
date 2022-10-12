package com.example.restservice.Account;
import com.example.restservice.model.AccountModel;
import com.example.restservice.repository.AccountRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import io.jsonwebtoken.Jwts;

import java.sql.*;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;


    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    public AccountService () {
    }

    // ********** REPOSITORY METHODS **********
    public void saveOrUpdate(AccountModel account) {
        accountRepository.save(account);
    }
    // Test method from tutorial
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (usernameExists(username)) {
            return new User(username, generateToken(username, username, getIdFromUserLogin(username)), new ArrayList<>());// return new User object with token
        }
        else {
            throw new UsernameNotFoundException("User with username " + username + "not found.");
        }
    }


    public List<AccountModel> findAll() {
        return (List<AccountModel>) accountRepository.findAll();
    }

    // ********** Methods called by AccountController **********
    public String createAccount(String username,String password) throws SQLException {
        if (!usernameExists(username)) {
            saveOrUpdate(new AccountModel(username, password));
            return "Account created successfully";
        }
        return "Username already taken";
    }

    public ResponseEntity<String> login(String username, String password) throws SQLException {
        if (isValidLogin(username, password)) {
            //auth = SecurityContextHolder.getContext().getAuthentication(); // uncomment if auth stops working
            return new ResponseEntity<>(generateToken(username, username, getIdFromUserLogin(username)), HttpStatus.OK);// return token
        }
        else {
            return new ResponseEntity<>("Not authenticated", HttpStatus.BAD_REQUEST); // return "not authenticated" message
        }
    }



    public boolean isValidLogin(String username, String password) {
        List<AccountModel> accountList = findAll();
        for (AccountModel account : accountList) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                System.out.println("Username and password match");
                return true;
            }
            else if (account.getUsername().equals(username)) {
                System.out.println("Username match, but incorrect password");
                return false;
            }
        }
        System.out.println("Username and password do not match");
        return false;
    }

    public int getIdFromUserLogin(String username) {
        List<AccountModel> accountList = findAll();
        //return accountList.stream().filter(account -> account.getUsername().equals(username)).findAny();
        for (AccountModel account : accountList) {
            if (account.getUsername().equals(username)) {
                return account.getId();
            }
        }
        return -1;
    }

    public boolean usernameExists(String username) {
        List<AccountModel> accountList = findAll();
        for(AccountModel account : accountList) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String generateToken(String subject, String username, int id) { // generate JWT token for returning on successful login
        return Jwts.builder().setSubject(username).claim("username", username).claim("id", id).signWith(
                SignatureAlgorithm.HS256,
                "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()
        ).compact();
    }
}

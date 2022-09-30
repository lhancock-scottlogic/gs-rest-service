package com.example.restservice.Account;
import com.example.restservice.Order;
import com.example.restservice.model.AccountModel;
import com.example.restservice.repository.AccountRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.*;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;
import java.sql.*;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    public AccountService () {
    }

    // ********** REPOSITORY METHODS **********
    public void saveOrUpdate(AccountModel account) {
        accountRepository.save(account);
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

    public String login(String username, String password) throws SQLException {
        if (isValidLogin(username, password)) {
           return generateToken(username, username, password);// return token
        }
        else {
            return "Not authenticated"; // return "not authenticated" message
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

    public boolean usernameExists(String username) {
        List<AccountModel> accountList = findAll();
        for(AccountModel account : accountList) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String generateToken(String subject, String username, String password) { // generate JWT token for returning on successful login
        return Jwts.builder().setSubject(username).claim("username", username).claim("password", password).signWith(
                SignatureAlgorithm.HS256,
                TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
        ).compact();
    }
}

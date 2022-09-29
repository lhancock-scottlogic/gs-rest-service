package com.example.restservice.Account;
import com.example.restservice.Order;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
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

@Service
public class AccountService {
    public List<Account> accountList;
    public AccountService(ArrayList<Account> accountList) {
        this.accountList = accountList;
        this.accountList.add(new Account(0, "Lucy", "password000"));
        this.accountList.add(new Account(1, "Luke", "password111"));
        this.accountList.add(new Account(2, "Coco", "password222"));
    }

    public String createAccount(String username,String password) {
        return null;
    }
    public String login(String username, String password) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime(); // setting today ad tomorrow variables for the issue/expiration dates of the token

        if (isValidLogin(username, password)) {
//            String jws = Jwts.builder()
//                    .setIssuer("Matcher")
//                    .setSubject("msilverman")
//                    .claim("name", "Micah Silverman")
//                    .claim("scope", "admins")
//                    // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
//                    .setIssuedAt(today)
//                    // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
//                    .setExpiration(tomorrow)
//                    .signWith(
//                            SignatureAlgorithm.HS256,
//                            TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
//                    )
//                    .compact();
           String jws = generateToken(username, username, password);
           return jws; // return token
        }
        else {
            return "Not authenticated"; // return "not authenticated" message
        }
    }

    public boolean isValidLogin(String username, String password) {
        for (Account account : this.accountList) {
            System.out.println("\n" + account);
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

    public String generateToken(String subject, String username, String password) {
        return Jwts.builder().setSubject(username).claim("username", username).claim("password", password).signWith(
                SignatureAlgorithm.HS256,
                TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
        ).compact();
    }
}

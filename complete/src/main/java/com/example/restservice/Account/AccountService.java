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
import java.sql.*;

@Service
public class AccountService {
    public List<Account> accountList;
    public AccountService(ArrayList<Account> accountList) {
        this.accountList = accountList;
        this.accountList.add(new Account(0, "Lucy", "password000"));
        this.accountList.add(new Account(1, "Luke", "password111"));
        this.accountList.add(new Account(2, "Coco", "password222"));
    }

    public String createAccount(String username,String password) throws SQLException {
        Connection conn = connectToDatabase();
        if (!usernameExists(username)) {
            this.accountList.add(new Account(0, username, password));
            closeConnection(conn);
            return "Account created successfully";
        }
        closeConnection(conn);
        return "Username already taken";
    }

    public String login(String username, String password) throws SQLException {
        Connection conn = connectToDatabase();
        if (isValidLogin(username, password)) {
            closeConnection(conn);
           return generateToken(username, username, password);// return token
        }
        else {
            closeConnection(conn);
            return "Not authenticated"; // return "not authenticated" message
        }
    }

    public Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
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

    public boolean usernameExists(String username) {
        for(Account account : this.accountList) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String generateToken(String subject, String username, String password) {
        return Jwts.builder().setSubject(username).claim("username", username).claim("password", password).signWith(
                SignatureAlgorithm.HS256,
                TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
        ).compact();
    }
}

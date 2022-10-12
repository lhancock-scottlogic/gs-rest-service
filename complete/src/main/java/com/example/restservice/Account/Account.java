package com.example.restservice.Account;
import org.springframework.data.annotation.Id;

public class Account {
    int accountId;
    String username;
    String password;
    public Account(int accountId, String username, String password) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
    }

    public int getaccountId() {
        return accountId;
    }

    public void setaccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

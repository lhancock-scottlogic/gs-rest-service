package com.example.restservice.Account;
import com.example.restservice.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "api/v1/matcher")
public class AccountController {
        @Autowired
        public AccountService accountService;

        @PostMapping(value = "/login")
        public String login(@RequestParam String username, @RequestParam String password) throws SQLException {
            return accountService.login(username, password);
        }

        @PostMapping("/createAccount")
        public String createAccount(@RequestParam String username, @RequestParam String password) throws SQLException {
                return accountService.createAccount(username, password);
        }
}

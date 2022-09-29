package com.example.restservice.Account;
import com.example.restservice.ChartOrder;
import com.example.restservice.Matcher.MatcherService;
import com.example.restservice.Order;
import com.example.restservice.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/matcher")
public class AccountController {
        @Autowired
        public AccountService accountService;

        @PostMapping(value = "/login")
        public String login(@RequestParam String username, @RequestParam String password) {
            return accountService.login(username, password);
        }
}

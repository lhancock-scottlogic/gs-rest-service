package com.example.restservice.Account;
import com.example.restservice.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping(path = "api/v1/matcher")
public class AccountController {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        public AccountService accountService;


        @PostMapping(value = "auth", consumes = "application/json", produces = "application/json")
        public ResponseEntity<String> loginCheck(@RequestBody @Valid AccountModel account) throws SQLException {
                return accountService.login(account.getUsername(), account.getPassword());
        }

        private void authenticate(String username, String password) throws Exception {
                try {
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)); // todo: problem is here
                }
                catch (DisabledException e) {
                        throw new Exception("USER_DISABLED", e);
                }
                catch (BadCredentialsException e) {
                        throw new Exception("INVALID_CREDENTIALS", e);
                }
        }

        @PostMapping(value = "/login")
        public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) throws SQLException {
            return accountService.login(username, password);
        }

        @PostMapping("/createAccount")
        public String createAccount(@RequestParam String username, @RequestParam String password) throws SQLException {
                return accountService.createAccount(username, password);
        }
}

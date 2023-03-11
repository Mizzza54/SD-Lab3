package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Gerasimov
 */
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/create-account")
    public void createAccount() {
        accountService.create();
    }

    @GetMapping("/add-money")
    public void addMoney(@RequestParam int accountId, @RequestParam int value) {
        accountService.addAmount(accountId, value);
    }

    @GetMapping("/get-account")
    public ResponseEntity<String> getAccount(@RequestParam int accountId) {
        return ResponseEntity.ok(accountService.read(accountId).toString());
    }

    @GetMapping("/get-total-amount")
    public ResponseEntity<Integer> getTotalAmount(@RequestParam int accountId) {
        return ResponseEntity.ok(accountService.getTotalAmount(accountId));
    }

    @GetMapping("/buy-stock")
    public void buyStock(@RequestParam int accountId, @RequestParam String stockName) {
        accountService.buyStock(accountId, stockName);
    }

    @GetMapping("/sell-stock")
    public void sellStock(@RequestParam int accountId, @RequestParam String stockName) {
        accountService.sellStock(accountId, stockName);
    }

    @GetMapping("/get-all-accounts")
    public ResponseEntity<String> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAll());
    }
}

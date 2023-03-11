package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Gerasimov
 */
public class Account {
    private final int accountId;
    private final Map<String, Stock> stocks;

    private int amount;

    public Account(int accountId) {
        this.accountId = accountId;
        this.amount = 0;
        this.stocks = new HashMap<>();
    }

    public int getAccountId() {
        return accountId;
    }

    public int getAmount() {
        return amount;
    }

    public Map<String, Stock> getStocks() {
        return stocks;
    }

    public void addAmount(int value) {
        amount += value;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", stocks=" + stocks +
                ", amount=" + amount +
                '}';
    }
}

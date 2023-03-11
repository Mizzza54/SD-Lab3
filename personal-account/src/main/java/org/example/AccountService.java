package org.example;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Michael Gerasimov
 */
@Service
public class AccountService {
    private final AccountDao accountDao;
    private final StockClient stockClient;
    private int accountCount;

    public AccountService(AccountDao accountDao, StockClient stockClient) {
        this.accountDao = accountDao;
        this.stockClient = stockClient;
        accountCount = 0;
    }

    public int create() {
        int accountId = accountCount;
        accountCount++;
        accountDao.create(new Account(accountId));
        return accountId;
    }

    public Account read(int accountId) {
        return accountDao.read(accountId);
    }

    public void addAmount(int accountId, int value) {
        Account account = read(accountId);
        account.addAmount(value);
    }

    public void addStock(int accountId, Stock stock) {
        Account account = read(accountId);
        account.getStocks().put(stock.getName(), stock);
    }

    public int getTotalAmount(int accountId) {
        Account account = read(accountId);
        int stockCost = account.getStocks().values().stream()
                .mapToInt(stock -> stockClient.getStockPrice(stock.getName())* stock.getCount())
                .sum();
        return stockCost + account.getAmount();
    }

    public void buyStock(int accountId, String stockName) {
        Account account = read(accountId);
        int stockPrice = stockClient.getStockPrice(stockName);
        if (account.getAmount() >= stockPrice) {
            if (stockClient.buyStock(stockName)) {
                Map<String, Stock> stockMap = account.getStocks();
                if (stockMap.containsKey(stockName)) {
                    int count = stockMap.get(stockName).getCount();
                    stockMap.put(stockName, new Stock(stockName, count + 1));
                } else {
                    stockMap.put(stockName, new Stock(stockName, 1));
                }
                account.addAmount(-stockPrice);
            }
        }
    }

    public void sellStock(int accountId, String stockName) {
        Account account = read(accountId);
        int stockCount = account.getStocks().get(stockName).getCount();
        if (stockCount > 0 && stockClient.sellStock(stockName)) {
            int stockPrice = stockClient.getStockPrice(stockName);
            account.addAmount(stockPrice);
            account.getStocks().put(stockName, new Stock(stockName, stockCount - 1));
        }
    }

    public String getAll() {
        return accountDao.readAll().stream().map(Account::toString).collect(Collectors.joining("\n"));
    }
}

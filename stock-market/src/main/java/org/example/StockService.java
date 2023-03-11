package org.example;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Michael Gerasimov
 */
@Service
public class StockService {
    private final StockDao stockDao;

    public StockService(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    public void createStock(String name, int price, int count) {
        stockDao.create(new Stock(name, price, count));
    }

    public Stock getStockByName(String name) {
        return stockDao.read(name);
    }

    public void deleteStockByName(String name) {
        stockDao.delete(name);
    }

    public Optional<Integer> getStockCountByName(String name) {
        return Optional.ofNullable(stockDao.read(name)).map(Stock::getCount);
    }

    public Optional<Integer> getStockPriceByName(String name) {
        return Optional.ofNullable(stockDao.read(name)).map(Stock::getPrice);
    }

    public String getAllStocks() {
        return stockDao.readAll().stream().map(Stock::toString).collect(Collectors.joining("\n"));
    }

    public boolean buyStock(String name) {
        Stock stock = stockDao.read(name);
        if (stock == null) {
            return false;
        }

        if (stock.getCount() > 0) {
            stockDao.create(new Stock(stock.getName(), stock.getPrice(), stock.getCount() - 1));
            return true;
        }

        return false;
    }

    public boolean sellStock(String name) {
        Stock stock = stockDao.read(name);
        if (stock == null) {
            return false;
        }

        stockDao.create(new Stock(stock.getName(), stock.getPrice(), stock.getCount() + 1));
        return true;
    }
}

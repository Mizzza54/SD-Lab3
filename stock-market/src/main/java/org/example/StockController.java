package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Gerasimov
 */
@RestController
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/add-stock")
    public ResponseEntity<Void> addStock(@RequestParam String name) {
        stockService.createStock(name, 0, 0);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete-stock")
    public ResponseEntity<Void> deleteStock(@RequestParam String name) {
        stockService.deleteStockByName(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stock-count")
    public ResponseEntity<Integer> stockCount(@RequestParam String name) {
        return stockService.getStockCountByName(name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock-price")
    public ResponseEntity<Integer> stockPrice(@RequestParam String name) {
        return stockService.getStockPriceByName(name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buy-stock")
    public ResponseEntity<Void> buyStock(@RequestParam String name) {
        return stockService.buyStock(name)
                ? ResponseEntity.ok().build()
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/sell-stock")
    public ResponseEntity<Void> sellStock(@RequestParam String name) {
        return stockService.sellStock(name)
                ? ResponseEntity.ok().build()
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all-stocks")
    public ResponseEntity<String> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/update-stock")
    public ResponseEntity<Void> updateStock(
            @RequestParam String name,
            @RequestParam int price,
            @RequestParam int count
    ) {
        stockService.createStock(name, price, count);
        return ResponseEntity.ok().build();
    }
}

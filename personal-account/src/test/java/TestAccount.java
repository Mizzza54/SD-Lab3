import org.example.AccountDao;
import org.example.AccountService;
import org.example.Stock;
import org.example.StockClient;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;

import java.net.URI;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author Michael Gerasimov
 */
@Testcontainers
public class TestAccount {
    private static final String URI = "http://localhost:8080";

    @Container
    public static GenericContainer simpleWebServer
            = new FixedHostPortGenericContainer("stock-market:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);

    private final RestTemplate restTemplate = new RestTemplate();
    private final StockClient stockClient = new StockClient(URI, restTemplate);
    private final AccountService accountService = new AccountService(new AccountDao(new HashMap<>()), stockClient);

    @BeforeClass
    public static void mainSetUp() {
        simpleWebServer.start();
    }

    @Test
    public void buyStockTest() {
        addStock("buyStockTest-stock", 100, 100);
        int accountId = accountService.create();
        accountService.addAmount(accountId, 1000);
        assertEquals(1000, accountService.read(accountId).getAmount());

        accountService.buyStock(accountId, "buyStockTest-stock");
        assertEquals(99, stockClient.getStockCount("buyStockTest-stock"));
        assertEquals(900, accountService.read(accountId).getAmount());
        assertEquals(1, accountService.read(accountId).getStocks().get("buyStockTest-stock").getCount());
        assertEquals("buyStockTest-stock", accountService.read(accountId).getStocks().get("buyStockTest-stock").getName());
        assertEquals(1000, accountService.getTotalAmount(accountId));
    }

    @Test
    public void sellStockTest() {
        addStock("sellStockTest-stock", 100, 100);
        int accountId = accountService.create();
        accountService.addStock(accountId, new Stock("sellStockTest-stock", 5));
        assertEquals(0, accountService.read(accountId).getAmount());
        assertEquals(1, accountService.read(accountId).getStocks().values().size());
        assertEquals("sellStockTest-stock", accountService.read(accountId).getStocks().get("sellStockTest-stock").getName());
        assertEquals(5, accountService.read(accountId).getStocks().get("sellStockTest-stock").getCount());

        accountService.sellStock(accountId, "sellStockTest-stock");
        assertEquals(101, stockClient.getStockCount("sellStockTest-stock"));
        assertEquals(100, accountService.read(accountId).getAmount());
        assertEquals(1, accountService.read(accountId).getStocks().values().size());
        assertEquals("sellStockTest-stock", accountService.read(accountId).getStocks().get("sellStockTest-stock").getName());
        assertEquals(4, accountService.read(accountId).getStocks().get("sellStockTest-stock").getCount());
    }

    @Test
    public void dynamicUpdatePriceTest() {
        addStock("dynamicUpdatePriceTest-stock", 100, 100);
        int accountId = accountService.create();
        accountService.addStock(accountId, new Stock("dynamicUpdatePriceTest-stock", 5));
        assertEquals(0, accountService.read(accountId).getAmount());
        assertEquals(1, accountService.read(accountId).getStocks().values().size());
        assertEquals("dynamicUpdatePriceTest-stock", accountService.read(accountId).getStocks().get("dynamicUpdatePriceTest-stock").getName());
        assertEquals(5, accountService.read(accountId).getStocks().get("dynamicUpdatePriceTest-stock").getCount());
        assertEquals(500, accountService.getTotalAmount(accountId));

        addStock("dynamicUpdatePriceTest-stock", 100, 500);

        assertEquals(0, accountService.read(accountId).getAmount());
        assertEquals(1, accountService.read(accountId).getStocks().values().size());
        assertEquals("dynamicUpdatePriceTest-stock", accountService.read(accountId).getStocks().get("dynamicUpdatePriceTest-stock").getName());
        assertEquals(5, accountService.read(accountId).getStocks().get("dynamicUpdatePriceTest-stock").getCount());
        assertEquals(2500, accountService.getTotalAmount(accountId));
    }

    private void addStock(String name, int count, int price) {
        URI uri = UriComponentsBuilder.fromUriString(URI)
                .pathSegment("update-stock")
                .queryParam("name", name)
                .queryParam("price", price)
                .queryParam("count", count)
                .build()
                .toUri();

        restTemplate.getForObject(uri, Void.class);
    }
}

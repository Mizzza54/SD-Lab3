package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Gerasimov
 */
public class AccountDao {
    private final Map<Integer, Account> accountById;

    public AccountDao(Map<Integer, Account> accountById) {
        this.accountById = accountById;
    }

    public void create(Account account) {
        accountById.put(account.getAccountId(), account);
    }

    public Account read(int accountId) {
        return accountById.get(accountId);
    }

    public List<Account> readAll() {
        return new ArrayList<>(accountById.values());
    }

    public void delete(int accountId) {
        accountById.remove(accountId);
    }
}

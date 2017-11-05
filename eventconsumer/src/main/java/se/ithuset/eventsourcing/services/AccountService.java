package se.ithuset.eventsourcing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import se.ithuset.bank.AccountBalanceEvent;

@Component
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateBalance(AccountBalanceEvent event) {
        jdbcTemplate.update("update account set balance=balance+? where id=?",event.getAmount(), event.getAccountId());
    }
}

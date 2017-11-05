package se.ithuset.bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountBalanceEvent {

    private long accountId;
    private BigDecimal amount;

    @JsonCreator
    public AccountBalanceEvent(@JsonProperty("accountId") long accountId, @JsonProperty("amount") BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AccountBalanceEvent{" +
                "accountId=" + accountId +
                ", amount=" + amount +
                '}';
    }
}

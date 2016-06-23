package banking;

import model.TransactionHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BankingServiceTest {

    private static final double FIFTY = 50.0;
    private static final double TWENTY = 20.0;
    private static final double TEN = 10.0;
    private static final double ZERO = 0.0;

    private Account one;
    private Account two;
    private Account three;
    private Account four;
    private Account five;
    private Account six;
    private Account seven;
    private Account eight;
    private Account richBoy;

    @Before
    public void setUp() {
        one = new Account(100.0);
        two = new Account(300.0);
        three = new Account(10.0);
        four = new Account(330.0);
        five = new Account(ZERO);
        six = new Account(ZERO);
        seven = new Account(ZERO);
        eight = new Account(ZERO);
        richBoy = new Account(FIFTY);
    }

    @Test
    public void simpleTransactionsSucceeds() {
        BankingService.handleRequests(Stream.of(new TransactionHolder(one, FIFTY, two)
                , new TransactionHolder(two, TWENTY, one)
                , new TransactionHolder(one, TEN, three)).collect(Collectors.toList()));
        assertSaldo(Stream.of(one, two, three), 60.0, 330.0, 20.0);
    }

    @Test
    public void simpleTransactionsWithConflictSucceeds() {
        BankingService.handleRequests(Stream.of(new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(two, TEN, one)).collect(Collectors.toList()));
        assertSaldo(Stream.of(one, two), ZERO, 400.0);
    }

    @Test
    @Ignore
    public void complexTransactionsFailsOnDeadLock() {
        List<TransactionHolder> oneList = Stream.of(
                new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(one, 110.0, two)
        ).collect(Collectors.toList());
        BankingService.handleRequests(oneList);
    }

    @Test
    public void complexTransactionsSucceeds() {
        List<TransactionHolder> oneList = Stream.of(
                new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(four, 330.0, one)
        ).collect(Collectors.toList());
        BankingService.handleRequests(oneList);
        assertSaldo(Stream.of(one, two, four), 100.0, 630.0, ZERO);
    }

    @Test
    public void longerLoopTransactionsSucceeds() {
        List<TransactionHolder> transactions = Stream.of(
                new TransactionHolder(five, FIFTY, six),
                new TransactionHolder(six, FIFTY, seven),
                new TransactionHolder(seven, FIFTY, eight),
                new TransactionHolder(eight, FIFTY, richBoy),
                new TransactionHolder(richBoy, FIFTY, five)
        ).collect(Collectors.toList());
        BankingService.handleRequests(transactions);
        assertSaldo(Stream.of(five, six, seven, eight, richBoy), ZERO, ZERO, ZERO, ZERO, FIFTY);
    }

    private static void assertSaldo(Stream<Account> accounts, Double... doubles) {
        Assert.assertArrayEquals(accounts.map(Account::getSaldo).toArray(), doubles);
    }
}
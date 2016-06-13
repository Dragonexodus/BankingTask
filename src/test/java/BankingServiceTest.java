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
    public void setUp(){
        one = new Account(100.0);
        two = new Account(300.0);
        three = new Account(10.0);
        four = new Account(330.0);
    }

    @Test
    public void simpleTransactionsSucceeds() {
        BankingService.handleRequests(Stream.of(new TransactionHolder(one, FIFTY, two)
                , new TransactionHolder(two, TWENTY, one)
                , new TransactionHolder(one, TEN, three)).collect(Collectors.toList()));
        Assert.assertArrayEquals(Stream.of(one, two, three).map(Account::getSaldo).toArray(),
                new Double[]{60.0, 330.0, 20.0});
    }

    @Test
    public void simpleTransactionsWithConflictSucceeds() {
        BankingService.handleRequests(Stream.of(new TransactionHolder(one, 110.0, two)
                , new TransactionHolder(two, TEN, one)).collect(Collectors.toList()));
        Assert.assertArrayEquals(Stream.of(one, two).map(Account::getSaldo).toArray(),
                new Double[]{0.0, 400.0});
    }

    @Test
    @Ignore
    public void complexTransactionsFails() {
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
        Assert.assertArrayEquals(Stream.of(one, two, four).map(Account::getSaldo).toArray(),
                new Double[]{100.0, 630.0, 0.0});
    }

    @Test
    public void longerLoopTransactionsSucceeds() {
        five = new Account(0.0);
        six = new Account(0.0);
        seven = new Account(0.0);
        eight = new Account(0.0);
        richBoy = new Account(50.0);

        List<TransactionHolder> transactions = Stream.of(
                new TransactionHolder(five, 50.0, six),
                new TransactionHolder(six, 50.0, seven),
                new TransactionHolder(seven, 50.0, eight),
                new TransactionHolder(eight, 50.0, richBoy),
                new TransactionHolder(richBoy, 50.0, five)
        ).collect(Collectors.toList());
        BankingService.handleRequests(transactions);
        Assert.assertArrayEquals(Stream.of(five, six, seven, eight, richBoy).map(Account::getSaldo).toArray(),
                new Double[]{0.0, 0.0, 0.0, 0.0, 50.0});
    }
}
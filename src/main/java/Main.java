import banking.Account;
import banking.BankingService;
import model.TransactionHolder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final double FIFTY = 50.0;
    private static final double ZERO = 0.0;

    public static void main(String[] args) {
        Account one = new Account(ZERO);
        Account two = new Account(ZERO);
        Account three = new Account(ZERO);
        Account four = new Account(ZERO);
        Account richBoy = new Account(FIFTY);

        List<TransactionHolder> transactions = Stream.of(
                new TransactionHolder(one, FIFTY, two),
                new TransactionHolder(two, FIFTY, three),
                new TransactionHolder(three, FIFTY, four),
                new TransactionHolder(four, FIFTY, richBoy),
                new TransactionHolder(richBoy, FIFTY, one)
        ).collect(Collectors.toList());
        BankingService.handleRequests(transactions);

        transactions.stream().map(TransactionHolder::getOne).forEach(System.out::println);
    }
}

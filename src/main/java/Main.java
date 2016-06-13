import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static Account one;
    private static Account two;
    private static Account three;
    private static Account four;
    private static Account richBoy;

    public static void main(String[] args) {
        one = new Account(0.0);
        two = new Account(0.0);
        three = new Account(0.0);
        four = new Account(0.0);
        richBoy = new Account(50.0);

        List<TransactionHolder> transactions = Stream.of(
                new TransactionHolder(one, 50.0, two),
                new TransactionHolder(two, 50.0, three),
                new TransactionHolder(three, 50.0, four),
                new TransactionHolder(four, 50.0, richBoy),
                new TransactionHolder(richBoy, 50.0, one)
        ).collect(Collectors.toList());
        BankingService.handleRequests(transactions);

        transactions.stream().map(TransactionHolder::getOne).forEach(System.out::println);
    }
}

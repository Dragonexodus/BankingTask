import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BankingService {

    public static Lock lock;
    public static Condition c;

    public static void handleRequests(List<TransactionHolder> transactions) {
        lock = new ReentrantLock();
        c = lock.newCondition();

        List<Thread> threads = transactions.stream().map(tx -> new Thread(
                () -> {
                    Transaction transaction = new Transaction();
                    transaction.tryTransacion(tx);
                })).collect(Collectors.toList());
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

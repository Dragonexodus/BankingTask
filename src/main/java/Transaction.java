import java.util.logging.Logger;

public class Transaction {

    private static final int TEN = 10;
    private static final int ASecond = 1000000000;
    private boolean isValid;
    private static final Logger logger = Logger.getLogger(Transaction.class.getSimpleName());

    public void tryTransacion(TransactionHolder holder) {
        BankingService.lock.lock();
        int tries = 0;
        try {

            while (!transact(holder.getOne(), holder.getK(), holder.getTwo())) {
                logger.info("Thread:" + Thread.currentThread().getId() + ",tries: " + tries);
                tries++;
                BankingService.c.awaitNanos(ASecond);
                if (tries > TEN) {
                    BankingService.c.signal();
                    throw new RuntimeException("Wait to long for transaction:" + holder);
                }
            }
            BankingService.c.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            BankingService.lock.unlock();
        }
    }

    boolean transact(Account source, Double k, Account target) {
        if (k < 0) {
            throw new IllegalArgumentException("Negative k:" + k + " in 'Überweisung'.");
        }
        logger.info("transact with:" + source + "," + k + "," + target);
        //System.out.println("transact with:"+source+","+k+","+target);
        drawOut(source, k);
        if (isValid) {
            deposit(target, k);
            logger.info("transact with:" + source + "," + k + "," + target + ", succeeds.");
            //System.out.println("transact with:"+source+","+k+","+target+", succeeds.");
        } else {
            logger.info("transact with:" + source + "," + k + "," + target + ", fails.");
            // System.out.println("transact with:"+source+","+k+","+target+", fails.");
        }

        return isValid;
    }

    private void drawOut(Account source, Double k) {
        isValid = source.checkedDrawOutSaldo(k);
    }

    private void deposit(Account target, Double k) {
        target.addDeposit(k);
    }
}

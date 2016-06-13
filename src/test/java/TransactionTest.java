import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TransactionTest {

    private Account accountOne;
    private Account accountTwo;
    private Transaction transaction;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp(){
        accountOne = new Account(100.0);
        accountTwo = new Account(0.0);
        transaction = new Transaction();
    }

    @Test
    public void simpleTransactionShouldWork(){
        Assert.assertTrue(transaction.transact(accountOne,100.0,accountTwo));
    }

    @Test
    public void wrongTransactionShouldFail(){
        Assert.assertFalse(transaction.transact(accountOne,200.0,accountTwo));
    }

    @Test
    public void negativeTransactionShouldFail(){
        double k = -200.0;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Negative k:"+k+" in 'Überweisung'.");

        transaction.transact(accountOne, k,accountTwo);
    }
}
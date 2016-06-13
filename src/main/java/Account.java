import java.util.Objects;

public class Account {

    private Double saldo;

    public Account(Double saldo) {
        Objects.<Double>requireNonNull(saldo);
        this.saldo = saldo;
    }

    public boolean checkedDrawOutSaldo(Double saldo) {
        double difference = this.saldo - saldo;
        if (difference < 0) {
            return false;
        }
        this.saldo = difference;
        return true;
    }

    public void addDeposit(Double saldo) {
        this.saldo += saldo;
    }

    public Double getSaldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return "Account{" +
                "saldo=" + saldo +
                '}';
    }
}

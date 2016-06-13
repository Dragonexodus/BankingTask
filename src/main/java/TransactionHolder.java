public class TransactionHolder {
    private final Account one;
    private final Double k;
    private final Account two;

    public TransactionHolder(Account one, Double k, Account two) {
        this.one = one;
        this.k = k;
        this.two = two;
    }

    public Account getOne() {
        return one;
    }

    public Double getK() {
        return k;
    }

    public Account getTwo() {
        return two;
    }

    @Override
    public String toString() {
        return "TransactionHolder{" +
                "one=" + one +
                ", k=" + k +
                ", two=" + two +
                '}';
    }
}

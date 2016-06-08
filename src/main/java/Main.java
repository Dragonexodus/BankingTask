import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by dragonexodus on 03.06.16.
 */
public class Main {
    static final int MAX_SIZE = 1000;
    static LinkedList<Integer> buffer;
    static Lock l;
    static Condition c;

    public static void main(String[] args) throws InterruptedException {

        buffer = new LinkedList<>();

        l = new ReentrantLock();
        c = l.newCondition();

        Thread erz = new Thread(
                () -> {
                    while (true) {
                        l.lock();
                        try {
                            while (buffer.size() >= MAX_SIZE) {
                                c.await();
                            }

                            Random data = new Random();
                            int value = data.nextInt();
                            buffer.addLast(value);
                            System.out.println(buffer.getLast() + "," + buffer.size());
                            c.signalAll();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            l.unlock();
                        }
                    }
                }
        );

        Thread verb = new Thread(
                () -> {
                    {
                        while (true) {
                            l.lock();
                            try {
                                while (buffer.isEmpty()) {
                                    c.await();
                                }
                                System.out.println(buffer.getFirst() + " : " + buffer.size());
                                buffer.removeFirst();
                                c.signalAll();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                l.unlock();
                            }

                        }
                    }
                }
        );

        erz.start();
        verb.start();
        erz.join();
        while (true) ;
        //TODO: replace with read onto file
    }

}

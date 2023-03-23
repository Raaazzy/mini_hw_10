package org.example;

public class Main {
    private static final Object lock = new Object();
    private static final int[] buf = new int[10];
    private static int count = 0;

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep((long) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    while (count == 10) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    buf[count++] = i;
                    lock.notifyAll();
                }
            }
        }
    }

    static class Customer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep((long) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    while (count == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int data = buf[--count];
                    lock.notifyAll();
                    System.out.println("The customer consumed the number:: " + data);
                }
            }
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.start();
        Customer customer = new Customer();
        customer.start();
    }
}
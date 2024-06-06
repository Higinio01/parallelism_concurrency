package edu.pucmm;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author me@fredpena.dev
 * @created 06/06/2024  - 08:46
 */
public class ProducerConsumer {
    private static final int QUEUE_CAPACITY = 10;
    private static final int PRODUCER_COUNT = 2;
    private static final int CONSUMER_COUNT = 2;
    private static final int PRODUCE_COUNT = 100;

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        // Crear y comenzar los hilos de productores
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            new Thread(new Producer(queue)).start();
        }

        // Crear y comenzar los hilos de consumidores
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            new Thread(new Consumer(queue)).start();
        }
    }

    static class Producer implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final Random random = new Random();

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < PRODUCE_COUNT; i++) {
                try {
                    int number = random.nextInt(100); // Generar un número aleatorio
                    queue.put(number); // Poner el número en la cola
                    System.out.println("Productor " + Thread.currentThread().getId() + " produjo: " + number);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        private final BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Integer number = queue.take(); // Tomar el número de la cola
                    System.out.println("Consumidor " + Thread.currentThread().getId() + " consumió: " + number);
                    // Procesar el número (en este caso, simplemente imprimirlo)
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


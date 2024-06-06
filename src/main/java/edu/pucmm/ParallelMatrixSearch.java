package edu.pucmm;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author me@fredpena.dev
 * @created 06/06/2024  - 08:46
 */
public class ParallelMatrixSearch {

    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_COUNT = 4;
    private static final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int TARGET = 256; // Número a buscar

    public static void main(String[] args) {
        // Inicializar la matriz con valores aleatorios
        Random random = new Random();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                matrix[i][j] = random.nextInt(1000); // Valores entre 0 y 999
            }
        }

        // Medir el tiempo de ejecución de la búsqueda secuencial
        long startTime = System.currentTimeMillis();
        boolean result = sequentialSearch();
        long endTime = System.currentTimeMillis();
        System.out.println("Resultado búsqueda secuencial: " + result);
        System.out.println("Tiempo búsqueda secuencial: " + (endTime - startTime) + "ms");

        // Medir el tiempo de ejecución de la búsqueda paralela
        startTime = System.currentTimeMillis();
        result = parallelSearch();
        endTime = System.currentTimeMillis();
        System.out.println("Resultado búsqueda paralela: " + result);
        System.out.println("Tiempo búsqueda paralela: " + (endTime - startTime) + "ms");
    }

    private static boolean sequentialSearch() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (matrix[i][j] == TARGET) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean parallelSearch() {
        Thread[] threads = new Thread[THREAD_COUNT];
        int rowsPerThread = MATRIX_SIZE / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int startRow = t * rowsPerThread;
            final int endRow = (t == THREAD_COUNT - 1) ? MATRIX_SIZE : startRow + rowsPerThread;

            threads[t] = new Thread(() -> {
                for (int i = startRow; i < endRow && !found.get(); i++) {
                    for (int j = 0; j < MATRIX_SIZE && !found.get(); j++) {
                        if (matrix[i][j] == TARGET) {
                            found.set(true);
                            return;
                        }
                    }
                }
            });

            threads[t].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return found.get();
    }
}


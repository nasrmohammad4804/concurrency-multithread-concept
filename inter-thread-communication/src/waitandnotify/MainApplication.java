package waitandnotify;

import java.io.*;
import java.util.*;

public class MainApplication {


    public static void main(String[] args) throws IOException {

        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();

        Thread producer = new MatrixReaderProducer(threadSafeQueue, MatrixGenerator.MATRIX_PATH);
        Thread consumer = new MatrixMultiplierConsumer(threadSafeQueue,MatrixGenerator.MULTIPLICATION_MATRIX_PATH);

        producer.start();
        consumer.start();
    }

    private static class MatrixMultiplierConsumer extends Thread {

        private final ThreadSafeQueue queue;
        private final FileWriter fileWriter;

        public MatrixMultiplierConsumer(ThreadSafeQueue queue, String outputPath) throws IOException {
            this.queue= queue;
            this.fileWriter = new FileWriter(outputPath);
        }

        @Override
        public void run(){
            while (true){
                try {
                    MatrixPair pair = queue.remove();

                    if (pair==null){
                        System.out.println("reader read null means producer finished");
                        break;
                    }

                    float[][] matrix1= pair.matrix1;
                    float[][] matrix2 = pair.matrix2;

                    write(matrix1,matrix2);


                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                fileWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void write(float[][] matrix1,float[][] matrix2) throws IOException {

            double[][] result = new double[MatrixGenerator.Number][MatrixGenerator.Number];

            for (int row = 0 ; row<MatrixGenerator.Number; row++)
                for (int column = 0; column<MatrixGenerator.Number; column++)
                    for (int counter = 0; counter<MatrixGenerator.Number; counter++){
                        result[row][column] += matrix1[row][counter] * matrix2[counter][column];
                    }

            String data = "";
            for (int index = 0 ; index<MatrixGenerator.Number ; index++){

                String reduce = Arrays.stream(result[index])
                        .mapToObj(String::valueOf).reduce("", (text1, text2) -> text1 + " , " + text2);
                data+=reduce+"\n";
            }
            data+="\n";

            fileWriter.write(data);


        }
    }
    private static class MatrixReaderProducer extends Thread {

        private final ThreadSafeQueue threadSafeQueue;
        private final Scanner scanner;

        public MatrixReaderProducer(ThreadSafeQueue queue, String path) throws FileNotFoundException {
            this.threadSafeQueue = queue;
            this.scanner = new Scanner(new FileInputStream(path));
        }

        @Override
        public void run() {

            while (true) {

                float[][] matrix1 = read();
                float[][] matrix2 = read();

                try {

                    if (matrix1==null || matrix2==null){
                        threadSafeQueue.terminate();
                        System.out.println("no more matrix exist for reading");
                        return;
                    }

                    else threadSafeQueue.addPair(new MatrixPair(matrix1,matrix2));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        private float[][] read() {

            float[][] data = new float[MatrixGenerator.Number][MatrixGenerator.Number];

            for (int row = 0; row < MatrixGenerator.Number; row++) {

                if (!scanner.hasNextLine())
                    return null;

                String[] split = scanner.nextLine().replaceAll(" , ", ",").split(",");

                for (int column = 0; column < MatrixGenerator.Number; column++)
                    data[row][column] = Integer.parseInt(split[column]);


            }
            scanner.nextLine();

            return data;

        }

    }


    private static class ThreadSafeQueue {

        private final int MAX_SIZE = 30;
        private final Queue<MatrixPair> queue = new ArrayDeque<>();
        private boolean isEmpty = true;
        private boolean isTerminated = false;

        public synchronized void addPair(MatrixPair pair) throws InterruptedException {


            while (queue.size() == MAX_SIZE)
                wait();

            queue.add(pair);

            if (queue.size() == 1) {
                isEmpty = false;
            }
            notify();


        }

        public synchronized MatrixPair remove() throws InterruptedException {


            while (isEmpty && !isTerminated)
                wait();


            if (queue.size() == 1) {
                isEmpty = true;
            } else if (queue.isEmpty() && isTerminated) {
                return null;
            }


            notify();
            return queue.remove();

        }

        public synchronized void terminate() {
            isTerminated = true;

            notifyAll();
        }
    }

    private static class MatrixPair {
        private final float[][] matrix1;
        private final float[][] matrix2;


        public MatrixPair(float[][] matrix1, float[][] matrix2) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
        }

        @Override
        public String toString() {
            return "MatrixPair{" +
                    "matrix1=" + Arrays.deepToString(matrix1) +
                    ", matrix2=" + Arrays.deepToString(matrix2) +
                    '}';
        }
    }
}

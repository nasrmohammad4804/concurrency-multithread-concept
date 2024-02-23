package waitandnotify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MatrixGenerator {
    public static final String MATRIX_PATH = "./src/waitandnotify/matrix.txt";
    public static final String MULTIPLICATION_MATRIX_PATH = "./src/waitandnotify/multiplication.txt";
    public static final int Number = 10;
    public static void main(String[] args) throws IOException {
        generateMatrix();
    }
    private static void generateMatrix() throws IOException {
        File file = new File(MATRIX_PATH);
        final int MAX_NUMBER = 150;
        FileWriter fileWriter = new FileWriter(file);
        Random random = new Random();
        for (int index = 1; index < 10000; index++) {


            String[][] data = new String[Number][Number];
            for (int row = 0; row < Number; row++) {

                for (int column = 0; column < Number; column++) {
                    data[row][column] = String.valueOf(random.nextInt(MAX_NUMBER));
                }
                String reduce = Arrays.stream(data[row]).reduce("", (text1, text2) -> text1 + " , " + text2);
                reduce = reduce.replaceFirst(" , ", "");
                fileWriter.write(reduce+"\n");
            }

            fileWriter.write("\n");
        }

        fileWriter.flush();

        fileWriter.close();
    }
}

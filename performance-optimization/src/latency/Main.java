package latency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static latency.util.RGBUtility.*;

public class Main {
    private static String SOURCE_FILE="./resources/many-flowers.jpg";
    private static String DESTINATION_FILE= "./generate/recolor.jpg";
    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);

        long start  = System.currentTimeMillis();
//        calculateWithSingleThread(bufferedImage, resultImage);
        calculateWithMultiThread(13,bufferedImage,resultImage);

        long end = System.currentTimeMillis();
        System.out.println("calculation takes : "+ (end - start));
    }

    private static void calculateWithMultiThread(int numberOfThreads,BufferedImage bufferedImage, BufferedImage resultImage) throws InterruptedException, IOException {
        List<Thread> threads = new ArrayList<>();


        final int EACH_LENGTH = bufferedImage.getHeight() / numberOfThreads;

        for (int index = 0; index<numberOfThreads; index++){
            threads.add(
                    new Thread(
                            new ImageWriter(
                                    (index * EACH_LENGTH),
                                    (index+1) * EACH_LENGTH,
                                    bufferedImage,resultImage
                            )
                    )
            );

        }
        for (Thread thread : threads)
            thread.start();

        for (Thread thread : threads)
            thread.join();

        ImageIO.write(resultImage,"jpg",new FileOutputStream(DESTINATION_FILE));

    }


    private static void calculateWithSingleThread(BufferedImage bufferedImage, BufferedImage resultImage) throws IOException {
            changeRgbImage(bufferedImage,0,bufferedImage.getHeight(),resultImage);
            ImageIO.write(resultImage,"jpg",new FileOutputStream(DESTINATION_FILE));
    }

    private static class ImageWriter implements Runnable{

        private final int heightFrom;
        private final int heightTo;

        private final BufferedImage originalImage;
        private final BufferedImage resultImage;


        public ImageWriter( int heightFrom, int heightTo, BufferedImage originalImage, BufferedImage resultImage) {

            this.heightFrom = heightFrom;
            this.heightTo = heightTo;
            this.originalImage = originalImage;
            this.resultImage = resultImage;

        }

        @Override
        public void run() {
            try {
                changeRgbImage(originalImage, heightFrom,heightTo,resultImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package latency.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class RGBUtility {

    public static void changeRgbImage(BufferedImage bufferedImage,int heightFrom,int heightTo,BufferedImage resultImage) throws IOException {
        for (int width=0; width< bufferedImage.getWidth(); width++)
            for (int height=heightFrom; height< heightTo; height++)
                RGBUtility.setRGB(resultImage,width,height,giveNewRgb(bufferedImage,width,height));

    }
    public static int giveNewRgb(BufferedImage originalImage , int x , int y){
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        boolean isShapeGray = isShadeOfGray(red, green, blue);

        if (!isShapeGray)
            return rgb;

        int newRed=Math.min(255,red+10);
        int newGreen = Math.max(0,green - 80);
        int newBlue = Math.max(0,blue - 20);

        return  createRGBFromColor(newRed,newGreen,newBlue);
    }

    public static void setRGB(BufferedImage image,int x, int y, int rgb){
        image.getRaster().setDataElements(x,y,image.getColorModel().getDataElements(rgb,null));

    }

    public static boolean isShadeOfGray(int red, int green , int blue){
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(blue - green) < 30;
    }
    public static int createRGBFromColor(int red,int green,int blue){
        int rgb = 0;

        rgb = rgb | blue;
        rgb = rgb | (green << 8);
        rgb = rgb | (red << 16);
        rgb = rgb | 0xFF000000;

        return  rgb;
    }
    public static int getRed(int rgb){
        return (rgb & 0x00FF0000) >> 16;
    }
    public static int getGreen(int rgb){

        return  ( rgb & 0x00000FF00 ) >> 8;
    }
    public static int getBlue(int rgb){
        return  rgb & 0x000000FF;
    }
}

package ssm_test.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 创建图片工具类
 * @author disentice
 *声明三个方法;
 */
public class ImageUtil {

	//转换图片为ipg格式；确保jpg显示正常；
    public static BufferedImage change2jpg(File f) {
        try {
        	//获取toolkit对象，创建图片；
            Image i = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());
            //获取像素值，创建 PixelGrabber对象；
            PixelGrabber pg = new PixelGrabber(i, 0, 0, -1, -1, true);
            //像素点存入数组
            pg.grabPixels();
            //获取长高
            int width = pg.getWidth();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
            int height = pg.getHeight();
            //静态像素数组
            final int[] RGB_MASKS = { 0xFF0000, 0xFF00, 0xFF };
            //创建colorModel颜色alpha分量模型
            final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
            //缓冲器，参数
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
            //调用rester工厂方法，实例化WritableRaster
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
            //传模型，获取缓存区照片
            BufferedImage img = new BufferedImage(RGB_OPAQUE, raster, false, null);
            return img;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
  
    //改变图片大小
    public static void resizeImage(File srcFile, int width,int height, File destFile) {
        try {
        	//判断并获取文件；
            if(!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();
            //读入图片文件
            Image i = ImageIO.read(srcFile);
            //调用resize方法；
            i = resizeImage(i, width, height);
            //写出去
            ImageIO.write((RenderedImage) i, "jpg", destFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
      
    //改变图片大小
    public static Image resizeImage(Image srcImage, int width, int height) {
        try {
  
        	//声明bufferedImage
            BufferedImage buffImg = null;
            //创建对象并传参
            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //获取对象并制图
            buffImg.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
  
            return buffImg;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

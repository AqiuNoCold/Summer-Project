package Pages.Utils;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.awt.image.RenderedImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


public class ImageUtils {

    public static byte[] compressImage(File inputFile, int maxSizeBytes, int targetWidth, int targetHeight) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);

        // Resize image
        BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        float quality = 1.0f; // Start with high quality

        while (quality > 0.1f) {
            param.setCompressionQuality(quality);
            baos.reset();
            writer.write(null, new javax.imageio.IIOImage(resizedImage, null, null), param);

            if (baos.size() <= maxSizeBytes) {
                break;
            }

            quality -= 0.1f;
        }

        writer.dispose();
        ios.close();

        return baos.toByteArray();
    }


    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

    public static void saveCompressedImage(BufferedImage image, File outputFile, int width, int height) throws IOException {
        // Resize the image to the desired dimensions
        BufferedImage resizedImage = resizeImage(image, width, height);

        // Write the resized image to the output file as PNG
        ImageIO.write(resizedImage, "png", outputFile);
    }


}
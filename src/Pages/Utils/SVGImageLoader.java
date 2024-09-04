package Pages.Utils;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SVGImageLoader {
    public static ImageIcon loadSVGImage(String path, int width, int height) {
        try {
            InputStream inputStream = SVGImageLoader.class.getResourceAsStream(path);
            if (inputStream == null) {
                System.err.println("Error: Could not load image at " + path);
                return null;
            }

            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);

            TranscoderInput input = new TranscoderInput(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);

            transcoder.transcode(input, output);

            byte[] imageData = outputStream.toByteArray();
            return new ImageIcon(imageData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
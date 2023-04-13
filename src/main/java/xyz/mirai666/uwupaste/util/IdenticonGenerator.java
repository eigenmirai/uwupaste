package xyz.mirai666.uwupaste.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class IdenticonGenerator {
    private String hashAlgorithm;

    public IdenticonGenerator(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public BufferedImage ofUsername(String username) throws NoSuchAlgorithmException {
        int width = 5;
        int height = 5;

        MessageDigest digest = MessageDigest.getInstance(this.hashAlgorithm);
        byte[] hashBytes = digest.digest(username.getBytes(StandardCharsets.UTF_8));
        BitSet bits = BitSet.valueOf(hashBytes);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        byte[] colorBytes = bits.get(25, 48).toByteArray();
        Color color = new Color(colorBytes[0] & 0xFF, colorBytes[1] & 0xFF, colorBytes[2] & 0xFF);

        for (int i = 0; i < width*height; i++) {
            int x = i % width;
            int y = i / width;
            if (bits.get(i)) {
                image.setRGB(x, y, color.getRGB());
            } else {
                image.setRGB(x, y, Color.white.getRGB());
            }
        }
        return this.resizeImage(image, 250, 250);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}

package io.github.agus5534.googleocrtelegramas.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class ImgDownloader {
    private final URL imageUrl;

    public ImgDownloader(String imageUrl) throws MalformedURLException {
        this.imageUrl = new URL(imageUrl);
    }

    public BufferedImage downloadImage() throws IOException {
        return ImageIO.read(imageUrl);
    }

    public byte[] downloadImageBytes() throws IOException {
        BufferedImage image = downloadImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public String downloadImageBase64() throws IOException {
        byte[] imageBytes = downloadImageBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

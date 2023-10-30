package io.github.agus5534.googleocrtelegramas.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

public class ImgDownloaderService {

    public void downloadAndProcessImage(String imageUrl) {
        try {
            ImgDownloader imgDownloader = new ImgDownloader(imageUrl);

            BufferedImage image = imgDownloader.downloadImage();

            byte[] imageBytes = imgDownloader.downloadImageBytes();

            String imageBase64 = imgDownloader.downloadImageBase64();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

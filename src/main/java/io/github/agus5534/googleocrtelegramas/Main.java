package io.github.agus5534.googleocrtelegramas;

import io.github.agus5534.googleocrtelegramas.services.ImgDownloader;
import io.github.agus5534.googleocrtelegramas.utils.FileCreator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Main {

    public static FileCreator mainFolder = new FileCreator(new File(System.getProperty("user.home")), "elecciones-tests/");
    public static void main(String[] args) throws IOException {
        ImgDownloader imgDownloader = new ImgDownloader("https://i.imgur.com/8uUPupv.jpg");

        BufferedImage image = imgDownloader.downloadImage();

        byte[] imageBytes = imgDownloader.downloadImageBytes();

        String imageBase64 = imgDownloader.downloadImageBase64();

    }
}

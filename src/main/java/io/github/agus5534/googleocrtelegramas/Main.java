package io.github.agus5534.googleocrtelegramas;

import io.github.agus5534.googleocrtelegramas.utils.FileCreator;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static FileCreator mainFolder = new FileCreator(new File(System.getProperty("user.home")), "elecciones-tests/");
    public static void main(String[] args) {
        String tel = "/telegramas/tel" + ThreadLocalRandom.current().nextInt(1, 5) + ".tiff"; // En caso de agregar más, renombrar a telX.tiff y mover el bound de 5 por uno más por cada archivo
        var finalURL = Main.class.getResource(tel);


        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(new File(finalURL.toURI()).toPath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Ha ocurrido un error al buscar los resources, ¿Están bien cargados? ¿Está marcado el directorio como Resources Root?", e);
        }

        System.out.println("USANDO TELEGRAMA: " + tel);

        TelegramaDownloader.download(bytes);
    }
}

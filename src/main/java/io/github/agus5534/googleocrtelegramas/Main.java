package io.github.agus5534.googleocrtelegramas;

import io.github.agus5534.googleocrtelegramas.utils.FileCreator;

import java.io.File;

public class Main {

    public static FileCreator mainFolder = new FileCreator(new File(System.getProperty("user.home")), "elecciones-tests/");
    public static void main(String[] args) {
        TelegramaDownloader.download("0200700680X");
    }
}

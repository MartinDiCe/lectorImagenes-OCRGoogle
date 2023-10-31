package io.github.agus5534.googleocrtelegramas;

import com.google.common.primitives.Bytes;
import io.github.agus5534.googleocrtelegramas.utils.FileCreator;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static String URL_1 = "https://cdn.discordapp.com/attachments/967975761543839764/1168685619573174432/balotaje.txt";
    static String URL_2 = "https://cdn.discordapp.com/attachments/967975761543839764/1168739797309857912/balotaje-2.txt";
    static String URL_3 = "https://cdn.discordapp.com/attachments/967975761543839764/1168739824526700565/balotaje-3.txt";


    public static FileCreator mainFolder = new FileCreator(new File(System.getProperty("user.home")), "elecciones-tests/");
    public static void main(String[] args) {

        byte[] bytes;
        try {
            var url = new URL(URL_3).openStream(); // CAMBIAR CON URL DESDE ARRIBA SI DESEAN CAMBIAR EL TELEGRAMA!


            var string = IOUtils.toString(url, StandardCharsets.UTF_8);
            var stringArray = string.replace("[","").replace("]", "").replace(" ", "").split(",");

            bytes = new byte[stringArray.length];

            for(int i = 0; i < stringArray.length; i++) {
               bytes[i] = Byte.parseByte(stringArray[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TelegramaDownloader.download(bytes);
    }
}

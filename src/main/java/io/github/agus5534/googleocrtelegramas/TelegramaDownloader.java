package io.github.agus5534.googleocrtelegramas;

import io.github.agus5534.googleocrtelegramas.models.dto.DatosTelegrama;
import io.github.agus5534.googleocrtelegramas.ocr.TextReader;
import io.github.agus5534.googleocrtelegramas.utils.filesConfig.FileCreator;
import io.github.agus5534.googleocrtelegramas.utils.timings.TimingsReport;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.UUID;

public class TelegramaDownloader {

    private final byte[] bytes;
    private final FileCreator mesaFolder;

    public static void download(byte[] telegrama) {
        try {
            new TelegramaDownloader(telegrama).downloadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TelegramaDownloader(byte[] telegrama) {
        this.bytes = telegrama;
        this.mesaFolder = new FileCreator(Main.mainFolder.getFile(), UUID.randomUUID() +"/", true);

        Arrays.stream(mesaFolder.getContents()).forEach(File::delete);

    }

    public void downloadData() throws Exception {

        FileCreator telegramaJpg = new FileCreator(mesaFolder.getFile(), "telegrama-jpg.jpg");
        BufferedImage tiffImage = ImageIO.read(new ByteArrayInputStream(bytes));

        BufferedImage jpegImage = new BufferedImage(
                tiffImage.getWidth(),
                tiffImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        jpegImage.createGraphics().drawImage(tiffImage, 0, 0, Color.WHITE, null);

        ImageIO.write(jpegImage, "jpg", telegramaJpg.getFile());

        TimingsReport.report("Creado .jpg del telegrama");

        try {
            DatosTelegrama mesaInfo = TextReader.read(telegramaJpg.getFile());
            JSONObject mesaInfoJSON = new JSONObject(mesaInfo);

            TimingsReport.report("JSONObject construido");

            System.out.println(mesaInfoJSON.toString(2));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package io.github.agus5534.googleocrtelegramas;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.agus5534.googleocrtelegramas.utils.FileCreator;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

public class TelegramaDownloader {
    private final String mesaId;

    private final URL tiff, results;

    private final FileCreator mesaFolder, tiffBin, telegrama, filteredResults;

    public static void download(String mesaId) {
        try {
            new TelegramaDownloader(mesaId).downloadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TelegramaDownloader(String mesaId) throws MalformedURLException {
        this.mesaId = mesaId;

        this.results = new URL(String.format("https://resultados.gob.ar/backend-difu/scope/data/getScopeData/%s/1", mesaId));
        this.tiff = new URL(String.format("https://resultados.gob.ar/backend-difu/scope/data/getTiff/%s", mesaId));

        this.mesaFolder = new FileCreator(Main.mainFolder.getFile(), mesaId+"/");

        Arrays.stream(mesaFolder.getContents()).forEach(File::delete);

        this.tiffBin = new FileCreator(mesaFolder.getFile(), "tiffbin.bin");
        this.telegrama = new FileCreator(mesaFolder.getFile(), "telegrama.tiff");
        this.filteredResults = new FileCreator(mesaFolder.getFile(), "results.txt");
    }

    public void downloadData() throws Exception {

        // RESULTS.TXT

        var resultsString = processResults();

        Files.write(filteredResults.getFile().toPath(), resultsString.getBytes());

        // TIFF

        Files.write(tiffBin.getFile().toPath(), decodedTelegrama());

        ImageIO.write(ImageIO.read(tiffBin.getFile()), "tiff", telegrama.getFile());

    }

    /**
     * Decodea el binary del tiff
     */
    private byte[] decodedTelegrama() throws Exception {
        var root = getRootObject(tiff);
        var binary = root.get("encodingBinary").getAsString();

        return Base64.getDecoder().decode(binary);
    }


    /**
     * Procesa el results, es un json con data. Solo saqué la que nos sirve..
     */
    private String processResults() throws Exception {
        StringBuilder stringBuilder = new StringBuilder("DATOS MESA\n");
        var root = getRootObject(results);


        stringBuilder.append("Date: ").append(root.get("date").getAsString()).append("\n");
        stringBuilder.append("Electores: ").append(root.get("electores").getAsString()).append("\n");
        stringBuilder.append("Sobres Encontrados: ").append(root.get("sobres").getAsString()).append("\n\n");
        stringBuilder.append("RESULTADOS: ").append(root.get("sobres").getAsString()).append("\n");

        var partidos = root.get("partidos").getAsJsonArray();

        stringBuilder.append("LLA: ").append(partidos.get(1).getAsJsonObject().get("votos").getAsDouble()).append("\n");
        stringBuilder.append("UxP: ").append(partidos.get(2).getAsJsonObject().get("votos").getAsDouble());


        return stringBuilder.toString();
    }

    private final JsonObject getRootObject(URL url) throws Exception{
        URLConnection request = url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        return root.getAsJsonObject();
    }

}

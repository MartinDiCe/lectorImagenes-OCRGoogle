package io.github.agus5534.googleocrtelegramas.utils.files;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONFileWriter {
    private FileCreator baseDirectory;

    public JSONFileWriter(FileCreator baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public void writeJSON(JSONArray jsonArray, String filename) throws IOException {
        File fullPath = new File(baseDirectory.getFile(), filename);
        FileWriter fileWriter = new FileWriter(fullPath);
        String jsonString = jsonArray.toString(2);
        fileWriter.write(jsonString);
        fileWriter.flush();
        fileWriter.close();
    }
}

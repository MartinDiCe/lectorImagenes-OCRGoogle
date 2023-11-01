package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class TextReader {
    public static void read(File tiff) throws IOException {
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(tiff));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(Arrays.asList(request));
            List<AnnotateImageResponse> responses = response.getResponsesList();

            JSONArray annotationsArray = new JSONArray();

            int currentRightX = 0;
            String previousText = "";
            boolean isFirstSection = true;

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    String text = annotation.getDescription();
                    BoundingPoly boundingPoly = annotation.getBoundingPoly();
                    Vertex rightBottom = boundingPoly.getVertices(2);

                    if (!isFirstSection && rightBottom.getX() < currentRightX) {

                        int lastIndex = annotationsArray.length() - 1;
                        JSONObject lastSection = annotationsArray.getJSONObject(lastIndex);
                        lastSection.put("text", lastSection.getString("text"));
                    }

                    JSONObject annotationObject = new JSONObject();
                    annotationObject.put("text", text);

                    JSONArray verticesArray = new JSONArray();
                    for (Vertex vertex : boundingPoly.getVerticesList()) {
                        JSONObject vertexObject = new JSONObject();
                        vertexObject.put("x", vertex.getX());
                        vertexObject.put("y", vertex.getY());
                        verticesArray.put(vertexObject); // Utiliza put en lugar de add
                    }

                    annotationObject.put("vertices", verticesArray);

                    annotationsArray.put(annotationObject); // Utiliza put en lugar de add

                    // Actualizar las variables de seguimiento
                    currentRightX = rightBottom.getX();
                    previousText = text;
                    isFirstSection = false;
                }
            }

            System.out.println("JSON resultante:");
            System.out.println(annotationsArray.toString(2)); // Formatear el JSON con sangría para una mejor legibilidad
        }
    }
}

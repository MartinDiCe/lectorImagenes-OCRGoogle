package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextReader {
    public static void read(File tiff) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(tiff));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            String mesaText = "MESA"; // Texto a buscar
            String otroTexto = "00634/9"; // Otro texto a buscar

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    String text = annotation.getDescription();
                    BoundingPoly boundingPoly = annotation.getBoundingPoly();

                    if (text.contains(mesaText)) {
                        System.out.format("MESA Text: %s%n", text);
                        showPolygon(boundingPoly);
                    }

                    if (text.contains(otroTexto)) {
                        System.out.format("Otro Texto: %s%n", text);
                        showPolygon(boundingPoly);
                    }
                }
            }
        }
    }

    // Función para mostrar los vértices de un polígono
    private static void showPolygon(BoundingPoly boundingPoly) {
        List<Vertex> vertices = boundingPoly.getVerticesList();
        System.out.println("Polígono:");
        for (Vertex vertex : vertices) {
            System.out.format("Vertex X: %d, Y: %d%n", vertex.getX(), vertex.getY());
        }
    }
}
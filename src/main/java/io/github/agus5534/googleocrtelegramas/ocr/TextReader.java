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
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            String mesaText = "MESA";
            String mesaID = "00634/9";

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                BoundingPoly polygonMesa = null;

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    String text = annotation.getDescription();
                    BoundingPoly boundingPoly = annotation.getBoundingPoly();

                    if (text.contains(mesaText)) {
                        polygonMesa = boundingPoly;
                    }

                    if (polygonMesa != null) {
                        if (!text.contains(mesaText)) {
                            boolean rightOf = isRightOf(polygonMesa, boundingPoly);
                            boolean below = isBelow(polygonMesa, boundingPoly);

                            if (rightOf && !below) {
                                System.out.println("Texto a la derecha de MESA: " + text);
                                showPolygon(boundingPoly);
                            } else if (!rightOf && below) {
                                System.out.println("Texto debajo de MESA: " + text);
                                showPolygon(boundingPoly);
                            }
                        }

                        if (text.contains(mesaID)) {
                            System.out.format("Otro Texto: %s%n", text);
                            showPolygon(boundingPoly);
                        }
                    }
                }
            }
        }
    }

    private static void showPolygon(BoundingPoly boundingPoly) {
        List<Vertex> vertices = boundingPoly.getVerticesList();
        System.out.println("Polígono:");
        for (Vertex vertex : vertices) {
            System.out.format("Vertex X: %d, Y: %d%n", vertex.getX(), vertex.getY());
        }
    }

    private static boolean isRightOf(BoundingPoly polygon1, BoundingPoly polygon2) {
        List<Vertex> vertices1 = polygon1.getVerticesList();
        List<Vertex> vertices2 = polygon2.getVerticesList();

        int maxX1 = Integer.MIN_VALUE;
        int minX2 = Integer.MAX_VALUE;

        for (Vertex vertex : vertices1) {
            maxX1 = Math.max(maxX1, vertex.getX());
        }

        for (Vertex vertex : vertices2) {
            minX2 = Math.min(minX2, vertex.getX());
        }

        return maxX1 < minX2;
    }

    private static boolean isBelow(BoundingPoly polygon1, BoundingPoly polygon2) {
        List<Vertex> vertices1 = polygon1.getVerticesList();
        List<Vertex> vertices2 = polygon2.getVerticesList();

        int maxY1 = Integer.MIN_VALUE;
        int minY2 = Integer.MAX_VALUE;

        for (Vertex vertex : vertices1) {
            maxY1 = Math.max(maxY1, vertex.getY());
        }

        for (Vertex vertex : vertices2) {
            minY2 = Math.min(minY2, vertex.getY());
        }

        return maxY1 < minY2;
    }
}


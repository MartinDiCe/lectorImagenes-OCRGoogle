package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import io.github.agus5534.googleocrtelegramas.utils.PolygonUtils;

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
            String mesaID = "00680/6";

            BoundingPoly firstRightPolygon = null;
            BoundingPoly firstBelowPolygon = null;
            String textInFirstRightPolygon = "";
            String textInFirstBelowPolygon = "";

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
                            boolean rightOf = PolygonUtils.isRightOf(polygonMesa, boundingPoly);
                            boolean below = PolygonUtils.isBelow(polygonMesa, boundingPoly);

                            if (rightOf && firstRightPolygon == null) {
                                firstRightPolygon = boundingPoly;
                                textInFirstRightPolygon = text;
                            } else if (below && firstBelowPolygon == null) {
                                firstBelowPolygon = boundingPoly;
                                textInFirstBelowPolygon = text;
                            }
                        }

                        if (text.contains(mesaID)) {
                            System.out.format("MesaId: %s%n", text);
                            PolygonUtils.showPolygon(boundingPoly);
                        }

                        if (firstRightPolygon != null && firstBelowPolygon != null) {
                            break;
                        }
                    }
                }

                if (firstRightPolygon != null && firstBelowPolygon != null) {
                    break;
                }
            }

            if (firstRightPolygon != null) {
                System.out.println("Primer polígono a la derecha de MESA:");
                PolygonUtils.showPolygon(firstRightPolygon);
                System.out.println("Texto en el primer polígono a la derecha: " + textInFirstRightPolygon);
            }

            if (firstBelowPolygon != null) {
                System.out.println("Primer polígono debajo de MESA:");
                PolygonUtils.showPolygon(firstBelowPolygon);
                System.out.println("Texto en el primer polígono debajo: " + textInFirstBelowPolygon);
            }
        }
    }
}
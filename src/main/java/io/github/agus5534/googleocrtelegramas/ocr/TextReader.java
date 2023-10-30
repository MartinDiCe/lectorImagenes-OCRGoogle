package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import io.github.agus5534.googleocrtelegramas.models.Position;
import io.github.agus5534.googleocrtelegramas.utils.KeywordSearchConfig;
import io.github.agus5534.googleocrtelegramas.utils.PolygonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextReader {

    public static void read(File tiff, KeywordSearchConfig config) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(tiff));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            String keyword = config.getKeyword();
            BoundingPoly keywordPosition = null;
            BoundingPoly searchPosition = null;

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    String text = annotation.getDescription();
                    BoundingPoly boundingPoly = annotation.getBoundingPoly();

                    if (text.contains(keyword)) {
                        keywordPosition = boundingPoly;
                    } else if (keywordPosition != null) {
                        // Determina la posición según la configuración
                        if (config.getSearchPosition() == Position.RIGHT) {
                            if (PolygonUtils.isRightOf(boundingPoly, keywordPosition)) {
                                searchPosition = boundingPoly;
                                break;
                            }
                        } else if (config.getSearchPosition() == Position.LEFT) {
                            if (PolygonUtils.isLeftOf(boundingPoly, keywordPosition)) {
                                searchPosition = boundingPoly;
                                break;
                            }
                        } else if (config.getSearchPosition() == Position.ABOVE) {
                            if (PolygonUtils.isAbove(boundingPoly, keywordPosition)) {
                                searchPosition = boundingPoly;
                                break;
                            }
                        } else if (config.getSearchPosition() == Position.BELOW) {
                            if (PolygonUtils.isBelow(boundingPoly, keywordPosition)) {
                                searchPosition = boundingPoly;
                                break;
                            }
                        }
                    }
                }
            }

            AnnotateImageResponse res = null;

            for (AnnotateImageResponse resp : responses) {
                res = resp;
                if (resp.hasError()) {
                    System.out.format("Error: %s%n", resp.getError().getMessage());
                    return;
                }
            }

            if (searchPosition != null) {
                System.out.println("Se encontró la posición deseada:");
                PolygonUtils.showPolygon(searchPosition);

                if (res != null) {
                    for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                        if (PolygonUtils.arePolygonsEqual(annotation.getBoundingPoly(), searchPosition)) {
                            String textInSearchPosition = annotation.getDescription();
                            System.out.println("Texto dentro del polígono: " + textInSearchPosition);
                        }
                    }
                } else {
                    System.out.println("No se pudo obtener el texto dentro del polígono.");
                }
            } else {
                System.out.println("La posición deseada no se encontró en la imagen.");
            }

        }
    }

}

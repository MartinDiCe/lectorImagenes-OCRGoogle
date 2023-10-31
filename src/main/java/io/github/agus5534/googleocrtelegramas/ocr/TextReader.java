package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import io.github.agus5534.googleocrtelegramas.utils.KeywordSearchConfig;
import io.github.agus5534.googleocrtelegramas.utils.PolygonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TextReader {
    public static void read(File tiff, KeywordSearchConfig config) throws IOException {
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(tiff));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

        List<String> keywords = config.getKeywords();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            for (String keyword : keywords) {
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(feat)
                        .setImage(img)
                        .build();
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(Arrays.asList(request));
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        System.out.format("Error: %s%n", res.getError().getMessage());
                        return;
                    }

                    for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                        String text = annotation.getDescription();
                        BoundingPoly boundingPoly = annotation.getBoundingPoly();

                        if (text.contains(keyword)) {
                            System.out.println("Keyword: " + keyword);
                            System.out.println("Posición de búsqueda: " + config.getSearchPosition());
                            PolygonUtils.showPolygon(boundingPoly);
                        }
                    }
                }
            }
        }
    }
}

package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                HashMap<List<List<Integer>>, EntityAnnotation> test = new HashMap<>();

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {

                    BoundingPoly boundingPoly = annotation.getBoundingPoly();
                    List<Vertex> vertices = boundingPoly.getVerticesList();

                    List<List<Integer>> vertexList = new ArrayList<>();

                    for(var vertex : vertices) {
                        vertexList.add(List.of(vertex.getX(), vertex.getY()));
                    }

                    test.put(vertexList, annotation);

                    /*System.out.format("Text: %s%n", annotation.getDescription());
                    System.out.format("Position : %s%n", annotation.getBoundingPoly());*/
                }

                test.forEach((lists, entityAnnotation) -> {
                    var isVotoLLA = lists.equals(
                            List.of(
                                    List.of(1245, 928),
                                    List.of(1348, 935),
                                    List.of(1345, 969),
                                    List.of(1243, 962)
                            )
                    );

                    var isVotoUXP = lists.equals(
                            List.of(
                                    List.of(1232, 999),
                                    List.of(1340, 1002),
                                    List.of(1339, 1046),
                                    List.of(1231, 1043)
                            )
                    );

                    if(isVotoLLA) {
                        System.out.println("Votos LLA: " + entityAnnotation.getDescription());
                    }

                    if(isVotoUXP) {
                        System.out.println("Votos UxP: " + entityAnnotation.getDescription());
                    }
                });
            }
        }
    }
}

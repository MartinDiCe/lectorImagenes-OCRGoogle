package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import io.github.agus5534.googleocrtelegramas.utils.SumValueConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import static io.github.agus5534.googleocrtelegramas.utils.TextSearcher.findTextNearVertices;
import static io.github.agus5534.googleocrtelegramas.utils.VertexSum.sumVertices;

public class TextReader {
    public static void read(File tiff) throws IOException {

        SumValueConfig sumValueConfig = new SumValueConfig();
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
                    List<JSONObject> vertexList = new ArrayList<>();

                    for (Vertex vertex : boundingPoly.getVerticesList()) {
                        JSONObject vertexObject = new JSONObject();
                        vertexObject.put("x", vertex.getX());
                        vertexObject.put("y", vertex.getY());
                        vertexList.add(vertexObject);
                    }

                    vertexList.sort(Comparator.comparingInt(o -> o.getInt("x")));

                    for (JSONObject vertexObject : vertexList) {
                        verticesArray.put(vertexObject);
                    }

                    annotationObject.put("vertices", verticesArray);

                    annotationsArray.put(annotationObject);

                    currentRightX = rightBottom.getX();
                    previousText = text;
                    isFirstSection = false;
                }
            }


            //MESA
            JSONArray vert = null;

            for (int i = annotationsArray.length() - 1; i >= 0; i--) {
                JSONObject annotation = annotationsArray.getJSONObject(i);
                String text = annotation.getString("text");

                if (text.contains("MESA")) {
                    System.out.println("Texto: " + text);
                    JSONArray vertices = annotation.getJSONArray("vertices");
                    System.out.println("Vértices:");

                    // Guardar los vértices de MESA en la variable mesaVertices
                    vert = vertices;

                    for (int j = 0; j < vertices.length(); j++) {
                        JSONObject vertexObject = vertices.getJSONObject(j);
                        int x = vertexObject.getInt("x");
                        int y = vertexObject.getInt("y");
                        System.out.println("x: " + x + ", y: " + y);
                    }

                    break;
                }
            }

                System.out.println("Vértices almacenados: " + vert.toString());


            //UP
            JSONArray vert2 = null;

            for (int i = annotationsArray.length() - 1; i >= 0; i--) {
                JSONObject annotation = annotationsArray.getJSONObject(i);
                String text = annotation.getString("text");

                if (text.contains("VICEPRESIDENTE")) {
                    System.out.println("Texto: " + text);
                    JSONArray vertices = annotation.getJSONArray("vertices");
                    System.out.println("Vértices:");

                    // Guardar los vértices de MESA en la variable mesaVertices
                    vert2 = vertices;

                    for (int j = 0; j < vertices.length(); j++) {
                        JSONObject vertexObject = vertices.getJSONObject(j);
                        int x = vertexObject.getInt("x");
                        int y = vertexObject.getInt("y");
                        System.out.println("x: " + x + ", y: " + y);
                    }

                    break;
                }
            }

            System.out.println("Vértices almacenados: " + vert2.toString());

            //LLA
            JSONArray vert3 = null;

            for (int i = annotationsArray.length() - 1; i >= 0; i--) {
                JSONObject annotation = annotationsArray.getJSONObject(i);
                String text = annotation.getString("text");

                if (text.contains("VICEPRESIDENTE")) {
                    System.out.println("Texto: " + text);
                    JSONArray vertices = annotation.getJSONArray("vertices");
                    System.out.println("Vértices:");

                    // Guardar los vértices de MESA en la variable mesaVertices
                    vert3 = vertices;

                    for (int j = 0; j < vertices.length(); j++) {
                        JSONObject vertexObject = vertices.getJSONObject(j);
                        int x = vertexObject.getInt("x");
                        int y = vertexObject.getInt("y");
                        System.out.println("x: " + x + ", y: " + y);
                    }

                    break;
                }
            }

            System.out.println("Vértices almacenados: " + vert3.toString());

            //MESA -> MesaId
            JSONArray newVertices = sumVertices(vert, sumValueConfig.getSumValuesMesa());

            System.out.println("New Vertices: " + newVertices.toString());

            String foundText = findTextNearVertices(annotationsArray, newVertices);

            System.out.println("MESA: " + foundText);

            //VICEPRESIDENTE -> votos UP
            JSONArray newVertices2 = sumVertices(vert3, sumValueConfig.getSumValuesUP());

            System.out.println("New Vertices2: " + newVertices2.toString());

            String foundText2 = findTextNearVertices(annotationsArray, newVertices2);

            System.out.println("Votos UP: " + foundText2);

            //VICEPRESIDENTE -> votos LLA
            JSONArray newVertices3 = sumVertices(vert3, sumValueConfig.getSumValuesLLA());

            System.out.println("New Vertices3: " + newVertices3.toString());

            String foundText3 = findTextNearVertices(annotationsArray, newVertices3);

            System.out.println("Votos LLA: " + foundText3);

            //Obtener el JSON completo de la imagen para ir revisando los vertices de los textos en caso de errores
            //System.out.println("JSON: " + annotationsArray.toString(2));

        }
    }
}

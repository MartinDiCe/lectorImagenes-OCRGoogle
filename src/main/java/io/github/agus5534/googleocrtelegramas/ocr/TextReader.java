package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import io.github.agus5534.googleocrtelegramas.utils.configs.SumValueConfig;
import io.github.agus5534.googleocrtelegramas.utils.vertexs.VerticesFinder;
import org.json.JSONArray;
import org.json.JSONObject;

import static io.github.agus5534.googleocrtelegramas.utils.texts.TextSearcher.findTextNearVertices;
import static io.github.agus5534.googleocrtelegramas.utils.vertexs.VertexSum.sumVertices;

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

            //PRESIDENTE
            JSONArray vertPresidente = VerticesFinder.findVertices(annotationsArray, "PRESIDENTE","VICEPRESIDENTE");

            System.out.println("Vertices almacenados: " + vertPresidente.toString());

            //PRESIDENTE -> votos UP
            JSONArray verticeUP = sumVertices(vertPresidente, sumValueConfig.getSumValuesUP());

            System.out.println("VERTICE UP: " + verticeUP.toString());

            String votoUP = findTextNearVertices(annotationsArray, verticeUP);

            System.out.println("Votos UP: " + votoUP);

            //PRESIDENTE -> votos LLA
            JSONArray verticeLLA = sumVertices(vertPresidente, sumValueConfig.getSumValuesLLA());

            System.out.println("VERTICE LLA: " + verticeLLA.toString());

            String votoLLA = findTextNearVertices(annotationsArray, verticeLLA);

            System.out.println("Votos LLA: " + votoLLA);

            //PRESIDENTE -> votos NULOS
            JSONArray verticeNulos = sumVertices(vertPresidente, sumValueConfig.getSumValuesNulos());

            System.out.println("VERTICE Nulos: " + verticeNulos.toString());

            String votoNulos = findTextNearVertices(annotationsArray, verticeNulos);

            System.out.println("Votos Nulos: " + votoNulos);

            //MESA
            JSONArray vertMesa = VerticesFinder.findVertices(annotationsArray, "MESA",":");

            System.out.println("Vertices almacenados: " + vertMesa.toString());

            //MESA -> MesaId
            JSONArray verticeMesaID = sumVertices(vertMesa, sumValueConfig.getSumValuesMesa());

            System.out.println("New Vertices: " + verticeMesaID.toString());

            String numeroMesa = findTextNearVertices(annotationsArray, verticeMesaID);

            System.out.println("MESA: " + numeroMesa);

            //Obtener el JSON completo de la imagen para ir revisando los vertices de los textos en caso de errores
            System.out.println("JSON: " + annotationsArray.toString(2));

        }
    }
}

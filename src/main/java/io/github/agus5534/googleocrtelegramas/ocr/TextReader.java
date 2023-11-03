package io.github.agus5534.googleocrtelegramas.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import io.github.agus5534.googleocrtelegramas.exceptions.AnnotateImageException;
import io.github.agus5534.googleocrtelegramas.models.dto.DatosTelegrama;
import io.github.agus5534.googleocrtelegramas.utils.configs.SumValueConfig;
import io.github.agus5534.googleocrtelegramas.utils.texts.StringToNumberConverter;
import io.github.agus5534.googleocrtelegramas.utils.vertexs.VerticesFinder;
import io.github.agus5534.googleocrtelegramas.utils.filesConfig.JSONFileWriter;
import org.json.JSONArray;
import org.json.JSONObject;


import static io.github.agus5534.googleocrtelegramas.Main.mainFolder;
import static io.github.agus5534.googleocrtelegramas.utils.texts.TextSearcher.findTextNearVertices;
import static io.github.agus5534.googleocrtelegramas.utils.vertexs.VertexSum.sumVertices;

public class TextReader {
    public static DatosTelegrama read(File tiff) throws IOException {

        DatosTelegrama mesaInfo = new DatosTelegrama();
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
                    String errorMessage = "Error: " + res.getError().getMessage();
                    throw new AnnotateImageException(errorMessage);
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

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String fechaHora = dateFormat.format(date);

            String nombreJSONaGuardar = "estructura_" + fechaHora + ".json";

            JSONFileWriter jsonFileWriter = new JSONFileWriter(mainFolder);
            try {
                jsonFileWriter.writeJSON(annotationsArray, nombreJSONaGuardar);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONArray vertMesa = VerticesFinder.findVertices(annotationsArray, "MESA",":");

            JSONArray verticeMesaID = sumVertices(vertMesa, sumValueConfig.getSumValuesMesa());

            JSONArray vertPresidente = VerticesFinder.findVertices(annotationsArray, "PRESIDENTE","VICEPRESIDENTE");

            JSONArray verticeUP = sumVertices(vertPresidente, sumValueConfig.getSumValuesUP());

            JSONArray verticeLLA = sumVertices(vertPresidente, sumValueConfig.getSumValuesLLA());

            JSONArray verticeNulos = sumVertices(vertPresidente, sumValueConfig.getSumValuesNulos());

            JSONArray verticeRecurrido = sumVertices(vertPresidente, sumValueConfig.getSumValuesRecurridos());

            JSONArray verticeImpugnados = sumVertices(vertPresidente, sumValueConfig.getSumValuesImpugnados());

            JSONArray verticeBlanco = sumVertices(vertPresidente, sumValueConfig.getSumValuesBlanco());

            JSONArray verticeTotales = sumVertices(vertPresidente, sumValueConfig.getSumValuesTotal());

            String votoUP = findTextNearVertices(annotationsArray, verticeUP);
            int votoUPNumber = StringToNumberConverter.convert(votoUP);

            String votoLLA = findTextNearVertices(annotationsArray, verticeLLA);
            int votoLLANumber = StringToNumberConverter.convert(votoLLA);

            String votoNulos = findTextNearVertices(annotationsArray, verticeNulos);
            int votoNulosNumber = StringToNumberConverter.convert(votoNulos);

            String votoRecurrido = findTextNearVertices(annotationsArray, verticeRecurrido);
            int votoRecurridoNumber = StringToNumberConverter.convert(votoRecurrido);

            String votoImpugnado = findTextNearVertices(annotationsArray, verticeImpugnados);
            int votoImpugnadoNumber = StringToNumberConverter.convert(votoImpugnado);

            String votoBlanco = findTextNearVertices(annotationsArray, verticeBlanco);
            int votoBlancoNumber = StringToNumberConverter.convert(votoBlanco);

            String numeroMesa = findTextNearVertices(annotationsArray, verticeMesaID);

            String votoTotales = findTextNearVertices(annotationsArray, verticeTotales);
            int votoTotalesNumber = StringToNumberConverter.convert(votoTotales);

                mesaInfo.setMesaId(numeroMesa);
            try {
                mesaInfo.setConteoUp((votoUPNumber));
                mesaInfo.setConteoLla((votoLLANumber));
                mesaInfo.setVotosNulos((votoNulosNumber));
                mesaInfo.setVotosRecurridos((votoRecurridoNumber));
                mesaInfo.setVotosImpugnados((votoImpugnadoNumber));
                mesaInfo.setVotosEnBlancos((votoBlancoNumber));
                mesaInfo.setVotosEnTotal((votoTotalesNumber));
                } catch (NumberFormatException e) {
                mesaInfo.setConteoUp(-1);
                mesaInfo.setConteoLla(-1);
                mesaInfo.setVotosNulos(-1);
                mesaInfo.setVotosRecurridos(-1);
                mesaInfo.setVotosImpugnados(-1);
                mesaInfo.setVotosEnBlancos(-1);
                mesaInfo.setVotosEnTotal(-1);
            }
                if (votoUP+votoLLA+votoNulos+votoRecurrido+votoImpugnado+votoBlanco==votoTotales){
                    mesaInfo.setEsValido(true);
                } else {
                    mesaInfo.setEsValido(false);
                }

            } catch (AnnotateImageException e) {
                throw new RuntimeException(e);
            }

            return mesaInfo;
        }
}

package io.github.agus5534.googleocrtelegramas.ocr;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;


import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import io.github.agus5534.googleocrtelegramas.Main;
import io.github.agus5534.googleocrtelegramas.exceptions.AnnotateImageException;
import io.github.agus5534.googleocrtelegramas.models.DatosTelegrama;
import io.github.agus5534.googleocrtelegramas.configs.SumValueConfig;
import io.github.agus5534.googleocrtelegramas.utils.polygons.Polygon;
import io.github.agus5534.googleocrtelegramas.utils.texts.StringToNumberConverter;
import io.github.agus5534.googleocrtelegramas.utils.texts.TextConcatenator;
import io.github.agus5534.googleocrtelegramas.utils.timings.TimingsReport;
import io.github.agus5534.googleocrtelegramas.utils.polygons.vertexs.VertexFinder;
import io.github.agus5534.googleocrtelegramas.utils.files.JSONFileWriter;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.imageio.ImageIO;

import static io.github.agus5534.googleocrtelegramas.Main.mainFolder;
import static io.github.agus5534.googleocrtelegramas.utils.texts.TextSearcher.findTextNearVertices;
import static io.github.agus5534.googleocrtelegramas.utils.polygons.vertexs.VertexSum.sumVertices;

public class TextReader {
    public static DatosTelegrama read(BufferedImage image) throws IOException {

        DatosTelegrama mesaInfo = new DatosTelegrama();
        SumValueConfig sumValueConfig = new SumValueConfig();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        ByteString imgBytes = ByteString.copyFrom(baos.toByteArray());

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();

            TimingsReport.report("Request enviado");

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(request));
            List<AnnotateImageResponse> responses = response.getResponsesList();

            JSONArray annotationsArray = new JSONArray();

            int currentRightX = 0;
            boolean isFirstSection = true;

            Map<String, List<Vertex>> stringListMap = new HashMap<>();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    String errorMessage = "Error: " + res.getError().getMessage();
                    throw new AnnotateImageException(errorMessage);
                }

                TimingsReport.report("Respuesta obtenida");

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

                    if(Main.debugMode) {
                        Map<String, Integer> repeat = new HashMap<>();

                        if (stringListMap.containsKey(text)) {
                            var repeatCount = repeat.getOrDefault(text, 1);

                            stringListMap.put(text + " (Repetido: "+(repeatCount+1)+")", boundingPoly.getVerticesList()); // TODO SET APARICION N°X

                            repeat.replace(text, repeatCount+1);
                        } else {
                            stringListMap.put(text, boundingPoly.getVerticesList());
                        }
                    }



                    for (Vertex vertex : boundingPoly.getVerticesList()) {
                        JSONObject vertexObject = new JSONObject();
                        vertexObject.put("x", vertex.getX());
                        vertexObject.put("y", vertex.getY());
                        vertexList.add(vertexObject);
                    }

                    vertexList.sort(Comparator.comparingInt(o -> o.getInt("x")));


                    var map = new HashMap<String, List<Polygon>>();

                    if(Main.debugMode) {
                        stringListMap.forEach((s, vertices) -> {
                            List<Polygon> pol = new ArrayList<>();

                            vertices.forEach(v -> pol.add(new Polygon(v.getX(), v.getY())));

                            map.put(s.replaceAll("\n", "   "), pol);
                        });

                        List<Map.Entry<String, List<Polygon>>> map2 = new ArrayList<>(map.entrySet());
                        map2.sort(Map.Entry.comparingByValue(comparator()));

                        StringBuilder s = new StringBuilder();

                        map2.forEach(stringListEntry -> {
                            var textEntry = stringListEntry.getKey();
                            var listValue = stringListEntry.getValue();

                            s.append("TEXTO: ").append(textEntry).append("\n");

                            listValue.forEach(polygon -> s.append("      - X: ").append(polygon.x()).append(" Y: ").append(polygon.y()).append("\n"));

                            s.append("\n");
                        });

                        Files.write(Main.sortedPolygons.getFile().toPath(), s.toString().getBytes());
                    }

                    for (JSONObject vertexObject : vertexList) {
                        verticesArray.put(vertexObject);
                    }

                    annotationObject.put("vertices", verticesArray);

                    annotationsArray.put(annotationObject);

                    currentRightX = rightBottom.getX();
                    isFirstSection = false;
                }
            }


            annotationsArray = TextConcatenator.concatenateText(annotationsArray, 10, 4);
            //System.out.println("Clase concatenadora: " + concatenatedAnnotationsArray.toString(2));

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String fechaHora = dateFormat.format(date);

            String nombreJSONaGuardar = "estructura_" + fechaHora + ".json";

            JSONFileWriter jsonFileWriter = new JSONFileWriter(mainFolder);

            TimingsReport.report("Creado archivo .json");

            try {
                jsonFileWriter.writeJSON(annotationsArray, nombreJSONaGuardar);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONArray vertMesa = VertexFinder.findVertices(annotationsArray, "MESA",":");

            JSONArray verticeMesaID = sumVertices(vertMesa, sumValueConfig.getSumValuesMesa());

            JSONArray vertPresidente = VertexFinder.findVertices(annotationsArray, "PRESIDENTE","VICEPRESIDENTE");

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
            mesaInfo.setEsValido((votoUPNumber + votoLLANumber + votoNulosNumber + votoRecurridoNumber + votoImpugnadoNumber + votoBlancoNumber) == votoTotalesNumber);
            } catch (AnnotateImageException e) {
                throw new RuntimeException(e);
            }

        TimingsReport.report("JSON cargado con datos");

        return mesaInfo;
        }

    public static Comparator<List<Polygon>> comparator() {
        return ((o1, o2) -> {
            Integer pol1R = 0;
            Integer pol2R = 0;

            for(int i = 0; i < o1.size() ; i++) {
                var res = o1.get(0).y().compareTo(o2.get(0).y()) == 0 ? o1.get(0).x().compareTo(o2.get(0).x()) : o1.get(0).y().compareTo(o2.get(0).y());

                if(res == 0) {
                    pol1R++;
                    pol2R++;
                }

                if(res == 1) {
                    pol1R++;
                } else {
                    pol2R++;
                }
            }

            return pol1R.compareTo(pol2R);
        });
    }

}

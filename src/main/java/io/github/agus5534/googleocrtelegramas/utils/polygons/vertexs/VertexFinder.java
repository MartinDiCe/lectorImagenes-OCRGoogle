package io.github.agus5534.googleocrtelegramas.utils.polygons.vertexs;

import org.json.JSONArray;
import org.json.JSONObject;

public class VertexFinder {
    public static JSONArray findVertices(JSONArray annotationsArray, String keyword1, String keyword2) {
        JSONArray closestVertices1 = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = annotationsArray.length() - 1; i >= 0; i--) {
            JSONObject annotation = annotationsArray.getJSONObject(i);
            String text = annotation.getString("text");

            if (text.equals(keyword1)) {
                JSONArray annotationVertices = annotation.getJSONArray("vertices");
                JSONArray vertices1 = annotationVertices;

                for (int j = 0; j < annotationVertices.length(); j++) {
                    JSONObject vertexObject = annotationVertices.getJSONObject(j);
                    int x1 = vertexObject.getInt("x");
                    int y1 = vertexObject.getInt("y");

                    for (int k = i - 1; k >= 0; k--) {
                        JSONObject annotation2 = annotationsArray.getJSONObject(k);
                        String text2 = annotation2.getString("text");

                        if (text2.equals(keyword2)) {
                            JSONArray annotationVertices2 = annotation2.getJSONArray("vertices");

                            for (int l = 0; l < annotationVertices2.length(); l++) {
                                JSONObject vertexObject2 = annotationVertices2.getJSONObject(l);
                                int x2 = vertexObject2.getInt("x");
                                int y2 = vertexObject2.getInt("y");

                                // Calcular la distancia entre los vértices de keyword1 y keyword2
                                double distance = calculateDistance(x1, y1, x2, y2);

                                if (distance < minDistance) {
                                    minDistance = distance;
                                    closestVertices1 = vertices1;
                                }
                            }
                        }
                    }
                }
            }
        }

        return closestVertices1;
    }

    private static double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}

package io.github.agus5534.googleocrtelegramas.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.Math;


public class TextDistanceCalculator {
    public double calculateDistance(JSONArray vertices1, JSONArray vertices2) {
        if (vertices1.length() != vertices2.length()) {
            throw new IllegalArgumentException("Los dos conjuntos de vértices deben tener la misma cantidad de vértices.");
        }

        double distanciaTotal = 0.0;

        for (int i = 0; i < vertices1.length(); i++) {
            JSONObject vertex1 = vertices1.getJSONObject(i);
            JSONObject vertex2 = vertices2.getJSONObject(i);

            int x1 = vertex1.getInt("x");
            int y1 = vertex1.getInt("y");
            int x2 = vertex2.getInt("x");
            int y2 = vertex2.getInt("y");

            double distancia = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            distanciaTotal += distancia;
        }

        return distanciaTotal;
    }
}

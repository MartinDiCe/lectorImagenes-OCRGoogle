package io.github.agus5534.googleocrtelegramas.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class VertexSum {
    public static JSONArray sumVertices(JSONArray vertices, JSONArray sumValues) {
        JSONArray newVertices = new JSONArray();

        if (vertices.length() == sumValues.length()) {
            for (int i = 0; i < vertices.length(); i++) {
                JSONObject vertex = vertices.getJSONObject(i);
                JSONObject sumValue = sumValues.getJSONObject(i);

                int newX = vertex.getInt("x") + sumValue.getInt("x");
                int newY = vertex.getInt("y") + sumValue.getInt("y");

                JSONObject newVertex = new JSONObject();
                newVertex.put("x", newX);
                newVertex.put("y", newY);

                newVertices.put(newVertex);
            }
        } else {
            System.out.println("El número de vértices y valores de suma debe ser el mismo.");
        }

        return newVertices;
    }


}

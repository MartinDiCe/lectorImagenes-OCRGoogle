package io.github.agus5534.googleocrtelegramas.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class TextSearcher {
    public static String findTextNearVertices(JSONArray textData, JSONArray targetVertices) {
        String nearestText = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int i = 0; i < textData.length(); i++) {
            JSONObject annotation = textData.getJSONObject(i);
            JSONArray vertices = annotation.getJSONArray("vertices");
            String text = annotation.getString("text");

            boolean isNear = true;
            double distance = 0;

            for (int j = 0; j < targetVertices.length(); j++) {
                JSONObject targetVertex = targetVertices.getJSONObject(j);
                boolean found = false;

                for (int k = 0; k < vertices.length(); k++) {
                    JSONObject vertex = vertices.getJSONObject(k);
                    int xDiff = Math.abs(vertex.getInt("x") - targetVertex.getInt("x"));
                    int yDiff = Math.abs(vertex.getInt("y") - targetVertex.getInt("y"));
                    distance += Math.sqrt(xDiff * xDiff + yDiff * yDiff);

                    if (xDiff <= 10 && yDiff <= 10) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    isNear = false;
                    break;
                }
            }

            if (isNear) {
                return text;
            } else if (distance < nearestDistance) {
                nearestText = text;
                nearestDistance = distance;
            }
        }

        if (nearestText != null) {
            return "Texto más cercano: " + nearestText;
        } else {
            return "No se encontró ningún texto cercano.";
        }
    }
}
package io.github.agus5534.googleocrtelegramas.utils.texts;

import org.json.JSONArray;
import org.json.JSONObject;

public class TextSearcher {
    public static String findTextNearVertices(JSONArray textData, JSONArray targetVertices) {
        String nearestText = null;
        double nearestDistance = Double.MAX_VALUE;
        String nearestNonNumericText = null;

        for (int i = 0; i < textData.length(); i++) {
            JSONObject annotation = textData.getJSONObject(i);
            JSONArray vertices = annotation.getJSONArray("vertices");
            String text = annotation.getString("text");

            double distance = calculateDistance(vertices, targetVertices);

            if (isNumeric(text) && nearestText == null) {
                nearestText = text;
                nearestDistance = distance;
            } else if (!isNumeric(text) && nearestNonNumericText == null) {
                nearestNonNumericText = text;
            }

            if (distance < nearestDistance) {
                nearestText = text;
                nearestDistance = distance;
            }
        }

        if (nearestText != null) {
            return nearestText;
        } else if (nearestNonNumericText != null) {
            return nearestNonNumericText;
        } else {
            return "-1";
        }
    }

    private static double calculateDistance(JSONArray vertices1, JSONArray vertices2) {
        double distance = 0;

        for (int i = 0; i < vertices1.length(); i++) {
            JSONObject vertex1 = vertices1.getJSONObject(i);

            for (int j = 0; j < vertices2.length(); j++) {
                JSONObject vertex2 = vertices2.getJSONObject(j);

                int xDiff = Math.abs(vertex1.getInt("x") - vertex2.getInt("x"));
                int yDiff = Math.abs(vertex1.getInt("y") - vertex2.getInt("y"));
                double vertexDistance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
                distance += vertexDistance;
            }
        }

        return distance;
    }

    private static boolean isNumeric(String text) {

        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

package io.github.agus5534.googleocrtelegramas.utils.texts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TextConcatenator {
    public static JSONArray concatenateText(JSONArray annotationsArray, int maxDistance, int maxCharacterCount) {
        JSONArray concatenatedArray = new JSONArray();
        List<JSONObject> currentTexts = new ArrayList<>();

        for (int i = 0; i < annotationsArray.length(); i++) {
            JSONObject annotation = annotationsArray.getJSONObject(i);
            JSONArray vertices = annotation.getJSONArray("vertices");
            String text = annotation.getString("text");
            boolean concatenated = false;

            if (currentTexts.isEmpty()) {
                currentTexts.add(annotation);
            } else {
                JSONObject previousAnnotation = currentTexts.get(currentTexts.size() - 1);
                JSONArray previousVertices = previousAnnotation.getJSONArray("vertices");
                String previousText = previousAnnotation.getString("text");

                int minY = getMinY(vertices);
                int previousMinY = getMinY(previousVertices);

                if (Math.abs(minY - previousMinY) <= maxDistance) {
                    if (previousText.length() + text.length() <= maxCharacterCount) {
                        String concatenatedText = previousText + text;
                        JSONArray concatenatedVertices = combineVertices(previousVertices, vertices);

                        currentTexts.set(currentTexts.size() - 1, createAnnotation(concatenatedText, concatenatedVertices));
                        concatenated = true;
                    }
                }
            }

            if (!concatenated) {
                currentTexts.add(annotation);
            }
        }

        concatenatedArray.putAll(currentTexts);
        return concatenatedArray;
    }

    private static int getMinY(JSONArray vertices) {
        int minY = Integer.MAX_VALUE;
        for (int i = 0; i < vertices.length(); i++) {
            int y = vertices.getJSONObject(i).getInt("y");
            minY = Math.min(minY, y);
        }
        return minY;
    }

    private static JSONArray combineVertices(JSONArray vertices1, JSONArray vertices2) {
        JSONArray combinedVertices = new JSONArray();
        combinedVertices.put(vertices1.get(0));

        for (int i = 1; i < vertices1.length(); i++) {
            int x1 = vertices1.getJSONObject(i).getInt("x");
            int x2 = vertices2.getJSONObject(i).getInt("x");
            int minY1 = vertices1.getJSONObject(i).getInt("y");
            int minY2 = vertices2.getJSONObject(i).getInt("y");

            // Take the minimum Y-value and maximum X-value
            int newY = Math.min(minY1, minY2);
            int newX = Math.max(x1, x2);

            JSONObject combinedVertex = new JSONObject();
            combinedVertex.put("x", newX);
            combinedVertex.put("y", newY);
            combinedVertices.put(combinedVertex);
        }

        return combinedVertices;
    }

    private static JSONObject createAnnotation(String text, JSONArray vertices) {
        JSONObject annotation = new JSONObject();
        annotation.put("text", text);
        annotation.put("vertices", vertices);
        return annotation;
    }
}

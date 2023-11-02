package io.github.agus5534.googleocrtelegramas.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class TextSearcher {
    public static String findTextNearVertices(JSONArray textData, JSONArray targetVertices) {
        for (int i = 0; i < textData.length(); i++) {
            JSONObject annotation = textData.getJSONObject(i);
            JSONArray vertices = annotation.getJSONArray("vertices");
            String text = annotation.getString("text");

            boolean isNear = true;

            for (int j = 0; j < targetVertices.length(); j++) {
                JSONObject targetVertex = targetVertices.getJSONObject(j);

                boolean found = false;

                for (int k = 0; k < vertices.length(); k++) {
                    JSONObject vertex = vertices.getJSONObject(k);

                    int xDiff = Math.abs(vertex.getInt("x") - targetVertex.getInt("x"));
                    int yDiff = Math.abs(vertex.getInt("y") - targetVertex.getInt("y"));

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
            }
        }

        return "Text not found near the given vertices.";
    }
}
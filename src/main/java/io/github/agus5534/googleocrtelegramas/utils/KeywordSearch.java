package io.github.agus5534.googleocrtelegramas.utils;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;

public class KeywordSearch {
    public static BoundingPoly findKeywordInResponse(String keyword, AnnotateImageResponse response) {
        for (EntityAnnotation annotation : response.getTextAnnotationsList()) {
            String text = annotation.getDescription();
            if (text.contains(keyword)) {
                return annotation.getBoundingPoly();
            }
        }
        return null;
    }
}
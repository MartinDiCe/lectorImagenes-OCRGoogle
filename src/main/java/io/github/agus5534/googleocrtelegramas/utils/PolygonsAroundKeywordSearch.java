package io.github.agus5534.googleocrtelegramas.utils;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;

import java.util.ArrayList;
import java.util.List;

public class PolygonsAroundKeywordSearch {
    public static List<BoundingPoly> findPolygonsAroundKeyword(String keyword, AnnotateImageResponse response) {
        BoundingPoly keywordPosition = KeywordSearch.findKeywordInResponse(keyword, response);
        if (keywordPosition != null) {
            List<BoundingPoly> polygonsAroundKeyword = new ArrayList<>();
            for (EntityAnnotation annotation : response.getTextAnnotationsList()) {
                if (!PolygonUtils.arePolygonsEqual(annotation.getBoundingPoly(), keywordPosition)) {
                    if (PolygonUtils.isRightOf(annotation.getBoundingPoly(), keywordPosition) ||
                            PolygonUtils.isLeftOf(annotation.getBoundingPoly(), keywordPosition) ||
                            PolygonUtils.isAbove(annotation.getBoundingPoly(), keywordPosition) ||
                            PolygonUtils.isBelow(annotation.getBoundingPoly(), keywordPosition)) {
                        polygonsAroundKeyword.add(annotation.getBoundingPoly());
                    }
                }
            }
            return polygonsAroundKeyword;
        }
        return null;
    }
}
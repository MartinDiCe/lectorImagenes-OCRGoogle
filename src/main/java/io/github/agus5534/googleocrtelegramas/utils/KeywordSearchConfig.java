package io.github.agus5534.googleocrtelegramas.utils;

import io.github.agus5534.googleocrtelegramas.models.Position;
import com.google.cloud.vision.v1.BoundingPoly;

import java.util.List;

public class KeywordSearchConfig {
    private final List<String> keywords;
    private final Position searchPosition;

    public KeywordSearchConfig(List<String> keywords, Position searchPosition) {
        this.keywords = keywords;
        this.searchPosition = searchPosition;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Position getSearchPosition() {
        return searchPosition;
    }

    public BoundingPoly getReferencePoly(BoundingPoly referencePoly) {
        return referencePoly;
    }
}

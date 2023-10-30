package io.github.agus5534.googleocrtelegramas.utils;

import io.github.agus5534.googleocrtelegramas.models.Position;
import com.google.cloud.vision.v1.BoundingPoly;

public class KeywordSearchConfig {
    private final String keyword;
    private final Position searchPosition;

    public KeywordSearchConfig(String keyword, Position searchPosition) {
        this.keyword = keyword;
        this.searchPosition = searchPosition;
    }

    public String getKeyword() {
        return keyword;
    }

    public Position getSearchPosition() {
        return searchPosition;
    }

    public BoundingPoly getReferencePoly(BoundingPoly referencePoly) {
        return referencePoly;
    }
}

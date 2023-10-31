package io.github.agus5534.googleocrtelegramas.utils;

import com.google.cloud.vision.v1.BoundingPoly;

public class KeywordPolygon {
    private String keyword;
    private BoundingPoly boundingPoly;

    public KeywordPolygon(String keyword, BoundingPoly boundingPoly) {
        this.keyword = keyword;
        this.boundingPoly = boundingPoly;
    }

    public String getKeyword() {
        return keyword;
    }

    public BoundingPoly getBoundingPoly() {
        return boundingPoly;
    }
}

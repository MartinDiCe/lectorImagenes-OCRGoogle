package io.github.agus5534.googleocrtelegramas.utils.polygons;

import java.util.List;

public record Polygon(Integer x, Integer y) {

    public List<Polygon> of(Integer x, Integer y, Integer x2, Integer y2, Integer x3, Integer y3, Integer x4, Integer y4) {
        return List.of(new Polygon(x, y), new Polygon(x2, y2), new Polygon(x3, y3), new Polygon(x4, y4));
    }
}

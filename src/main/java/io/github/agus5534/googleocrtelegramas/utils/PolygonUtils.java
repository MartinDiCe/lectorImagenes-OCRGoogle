package io.github.agus5534.googleocrtelegramas.utils;

import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.Vertex;

import java.util.List;

public class PolygonUtils {

    public static void showPolygon(BoundingPoly boundingPoly) {
        List<Vertex> vertices = boundingPoly.getVerticesList();
        System.out.println("Polígono:");
        for (Vertex vertex : vertices) {
            System.out.format("Vertex X: %d, Y: %d%n", vertex.getX(), vertex.getY());
        }
    }

    public static boolean isRightOf(BoundingPoly polygon1, BoundingPoly polygon2) {
        List<Vertex> vertices1 = polygon1.getVerticesList();
        List<Vertex> vertices2 = polygon2.getVerticesList();

        int maxX1 = Integer.MIN_VALUE;
        int minX2 = Integer.MAX_VALUE;

        for (Vertex vertex : vertices1) {
            maxX1 = Math.max(maxX1, vertex.getX());
        }

        for (Vertex vertex : vertices2) {
            minX2 = Math.min(minX2, vertex.getX());
        }

        return maxX1 < minX2;
    }

    public static boolean isBelow(BoundingPoly polygon1, BoundingPoly polygon2) {
        List<Vertex> vertices1 = polygon1.getVerticesList();
        List<Vertex> vertices2 = polygon2.getVerticesList();

        int maxY1 = Integer.MIN_VALUE;
        int minY2 = Integer.MAX_VALUE;

        for (Vertex vertex : vertices1) {
            maxY1 = Math.max(maxY1, vertex.getY());
        }

        for (Vertex vertex : vertices2) {
            minY2 = Math.min(minY2, vertex.getY());
        }

        return maxY1 < minY2;
    }

}

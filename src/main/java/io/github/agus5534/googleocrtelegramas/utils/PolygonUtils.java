package io.github.agus5534.googleocrtelegramas.utils;

import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;
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

    public static boolean isAbove(BoundingPoly polygon1, BoundingPoly polygon2) {
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

    public static boolean isLeftOf(BoundingPoly polygon1, BoundingPoly polygon2) {
        List<Vertex> vertices1 = polygon1.getVerticesList();
        List<Vertex> vertices2 = polygon2.getVerticesList();

        int minX1 = Integer.MAX_VALUE;
        int maxX2 = Integer.MIN_VALUE;

        for (Vertex vertex : vertices1) {
            minX1 = Math.min(minX1, vertex.getX());
        }

        for (Vertex vertex : vertices2) {
            maxX2 = Math.max(maxX2, vertex.getX());
        }

        return minX1 < maxX2;
    }

    public static boolean isBelow(BoundingPoly polygon1, BoundingPoly polygon2) {
        List<Vertex> vertices1 = polygon1.getVerticesList();
        List<Vertex> vertices2 = polygon2.getVerticesList();

        int minY1 = Integer.MAX_VALUE;
        int maxY2 = Integer.MIN_VALUE;

        for (Vertex vertex : vertices1) {
            minY1 = Math.min(minY1, vertex.getY());
        }

        for (Vertex vertex : vertices2) {
            maxY2 = Math.max(maxY2, vertex.getY());
        }

        return minY1 > maxY2;
    }

    public static BoundingPoly findPositionForKeyword(List<EntityAnnotation> annotations, KeywordSearchConfig config, BoundingPoly referencePoly) {
        List<String> keywords = config.getKeywords();
        for (EntityAnnotation annotation : annotations) {
            String text = annotation.getDescription();
            for (String keyword : keywords) {
                if (text.contains(keyword)) {
                    switch (config.getSearchPosition()) {
                        case RIGHT:
                            if (isRightOf(annotation.getBoundingPoly(), config.getReferencePoly(referencePoly))) {
                                return annotation.getBoundingPoly();
                            }
                            break;
                        case ABOVE:
                            if (isAbove(annotation.getBoundingPoly(), config.getReferencePoly(referencePoly))) {
                                return annotation.getBoundingPoly();
                            }
                            break;
                        case LEFT:
                            if (isLeftOf(annotation.getBoundingPoly(), config.getReferencePoly(referencePoly))) {
                                return annotation.getBoundingPoly();
                            }
                            break;
                        case BELOW:
                            if (isBelow(annotation.getBoundingPoly(), config.getReferencePoly(referencePoly))) {
                                return annotation.getBoundingPoly();
                            }
                            break;
                    }
                }
            }
        }
        return null;
    }

    public static boolean arePolygonsEqual(BoundingPoly poly1, BoundingPoly poly2) {
        if (poly1.getVerticesCount() != poly2.getVerticesCount()) {
            return false;
        }

        for (int i = 0; i < poly1.getVerticesCount(); i++) {
            Vertex vertex1 = poly1.getVertices(i);
            Vertex vertex2 = poly2.getVertices(i);

            if (vertex1.getX() != vertex2.getX() || vertex1.getY() != vertex2.getY()) {
                return false;
            }
        }

        return true;
    }

}
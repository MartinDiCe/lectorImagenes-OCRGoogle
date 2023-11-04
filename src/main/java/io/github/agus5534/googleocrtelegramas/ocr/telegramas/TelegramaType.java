package io.github.agus5534.googleocrtelegramas.ocr.telegramas;

import io.github.agus5534.googleocrtelegramas.utils.polygons.Polygon;

import java.util.List;

public class TelegramaType {

    private final String provinciaKey;
    public final List<Polygon> mesaId, total, totalLLA, totalUxP;

    public TelegramaType(String provinciaKey, List<Polygon> mesaId, List<Polygon> total, List<Polygon> totalLLA, List<Polygon> totalUxP) {
        this.provinciaKey = provinciaKey;
        this.mesaId = mesaId;
        this.total = total;
        this.totalLLA = totalLLA;
        this.totalUxP = totalUxP;
    }
 }

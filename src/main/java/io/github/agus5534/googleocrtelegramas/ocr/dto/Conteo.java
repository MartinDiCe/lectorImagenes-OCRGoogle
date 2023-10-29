package io.github.agus5534.googleocrtelegramas.ocr.dto;

public class Conteo {

    private Integer lla;
    private Integer up;

    public Conteo(Integer lla, Integer up) {
        this.lla = lla;
        this.up = up;
    }

    public Integer getLla() {
        return lla;
    }

    public void setLla(Integer lla) {
        this.lla = lla;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }
}

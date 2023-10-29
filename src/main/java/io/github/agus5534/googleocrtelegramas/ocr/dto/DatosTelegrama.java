package io.github.agus5534.googleocrtelegramas.ocr.dto;

public class DatosTelegrama {

    private String mesaId;
    private Integer votosEnTotal;
    private Integer votosImpugnados;
    private Integer votosEnBlancos;
    private Integer votosRecurridos;
    private Conteo conteo;
    private Boolean esValido;

    public DatosTelegrama(String mesaId, Integer votosEnTotal, Integer votosImpugnados, Integer votosEnBlancos, Integer votosRecurridos, Conteo conteo, Boolean esValido) {
        this.mesaId = mesaId;
        this.votosEnTotal = votosEnTotal;
        this.votosImpugnados = votosImpugnados;
        this.votosEnBlancos = votosEnBlancos;
        this.votosRecurridos = votosRecurridos;
        this.conteo = conteo;
        this.esValido = esValido;
    }

    public String getMesaId() {
        return mesaId;
    }

    public void setMesaId(String mesaId) {
        this.mesaId = mesaId;
    }

    public Integer getVotosEnTotal() {
        return votosEnTotal;
    }

    public void setVotosEnTotal(Integer votosEnTotal) {
        this.votosEnTotal = votosEnTotal;
    }

    public Integer getVotosImpugnados() {
        return votosImpugnados;
    }

    public void setVotosImpugnados(Integer votosImpugnados) {
        this.votosImpugnados = votosImpugnados;
    }

    public Integer getVotosEnBlancos() {
        return votosEnBlancos;
    }

    public void setVotosEnBlancos(Integer votosEnBlancos) {
        this.votosEnBlancos = votosEnBlancos;
    }

    public Integer getVotosRecurridos() {
        return votosRecurridos;
    }

    public void setVotosRecurridos(Integer votosRecurridos) {
        this.votosRecurridos = votosRecurridos;
    }

    public Conteo getConteo() {
        return conteo;
    }

    public void setConteo(Conteo conteo) {
        this.conteo = conteo;
    }

    public Boolean getEsValido() {
        return esValido;
    }

    public void setEsValido(Boolean esValido) {
        this.esValido = esValido;
    }
}

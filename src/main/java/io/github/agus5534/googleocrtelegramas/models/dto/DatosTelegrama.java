package io.github.agus5534.googleocrtelegramas.models.dto;

import lombok.Data;

@Data
public class DatosTelegrama {
    private String mesaId;
    private int conteoUp;
    private int conteoLla;
    private int votosNulos;
    private int votosImpugnados;
    private int votosRecurridos;
    private int votosEnBlancos;
    private int votosEnTotal;
    private boolean esValido;

    public DatosTelegrama() {

    }

}
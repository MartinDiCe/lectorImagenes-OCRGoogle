package io.github.agus5534.googleocrtelegramas.models.dto;

import lombok.Data;

@Data
public class DatosTelegrama {
    private String mesaId;
    private String conteoUp;
    private String conteoLla;
    private String votosNulos;
    private String votosImpugnados;
    private String votosRecurridos;
    private String votosEnBlancos;
    private String votosEnTotal;
    private boolean esValido;

    public DatosTelegrama() {

    }

}
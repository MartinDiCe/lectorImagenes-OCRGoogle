package io.github.agus5534.googleocrtelegramas.utils;

import org.json.JSONArray;

public class SumValueConfig {
    private JSONArray sumValuesMesa;
    private JSONArray sumValuesUP;
    private JSONArray sumValuesLLA;
    private JSONArray sumValuesNulos;
    private JSONArray sumValuesMesaDosPuntos;

    public SumValueConfig() {

        sumValuesMesaDosPuntos = new JSONArray("[{\"x\":43,\"y\":2},{\"x\":45,\"y\":2},{\"x\":90,\"y\":5},{\"x\":100,\"y\":3}]");


        sumValuesMesa = new JSONArray("[{\"x\":43,\"y\":2},{\"x\":45,\"y\":2},{\"x\":90,\"y\":5},{\"x\":100,\"y\":3}]");
        sumValuesUP = new JSONArray("[{\"x\":-20,\"y\":32},{\"x\":-20,\"y\":40},{\"x\":-31,\"y\":33},{\"x\":-31,\"y\":41}]");
        sumValuesLLA = new JSONArray("[{\"x\":-109,\"y\":63},{\"x\":-19,\"y\":72},{\"x\":-56,\"y\":63},{\"x\":-56,\"y\":72}]");
        sumValuesNulos = new JSONArray("[{\"x\":-17,\"y\":96},{\"x\":-17,\"y\":104},{\"x\":-47,\"y\":96},{\"x\":-47,\"y\":104}]");
    }

    public JSONArray getSumValuesMesa() {
        return sumValuesMesa;
    }

    public JSONArray getSumValuesUP() {
        return sumValuesUP;
    }

    public JSONArray getSumValuesLLA() {
        return sumValuesLLA;
    }

    public JSONArray getSumValuesNulos() {
        return sumValuesLLA;
    }

    public JSONArray getSumValuesMesaDosPuntos() {
        return sumValuesLLA;
    }

}


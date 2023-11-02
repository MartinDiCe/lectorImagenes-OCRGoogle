package io.github.agus5534.googleocrtelegramas.utils;

import org.json.JSONArray;

public class SumValueConfig {
    private JSONArray sumValuesMesa;
    private JSONArray sumValuesUP;

    public SumValueConfig() {
        // Inicializa los valores de suma para MESA y VICEPRESIDENTE
        sumValuesMesa = new JSONArray("[{\"x\":43,\"y\":2},{\"x\":45,\"y\":2},{\"x\":90,\"y\":5},{\"x\":100,\"y\":3}]");
        sumValuesUP = new JSONArray("[{\"x\":-7,\"y\":32},{\"x\":-1,\"y\":31},{\"x\":-53,\"y\":22},{\"x\":-52,\"y\":30}]");
    }

    public JSONArray getSumValuesMesa() {
        return sumValuesMesa;
    }

    public JSONArray getSumValuesUP() {
        return sumValuesUP;
    }
}


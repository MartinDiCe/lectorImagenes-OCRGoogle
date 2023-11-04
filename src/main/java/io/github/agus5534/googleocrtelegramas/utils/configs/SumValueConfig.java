package io.github.agus5534.googleocrtelegramas.utils.configs;

import org.json.JSONArray;

public class SumValueConfig {
    private JSONArray sumValuesMesa;
    private JSONArray sumValuesUP;
    private JSONArray sumValuesLLA;
    private JSONArray sumValuesNulos;
    private JSONArray sumValuesRecurridos;
    private JSONArray sumValuesImpugnados;
    private JSONArray sumValuesBlanco;
    private JSONArray sumValuesTotal;

    public SumValueConfig() {

        sumValuesMesa = new JSONArray("[{\"x\":43,\"y\":2},{\"x\":45,\"y\":2},{\"x\":90,\"y\":5},{\"x\":100,\"y\":3}]");
        sumValuesUP = new JSONArray("[{\"x\":-15,\"y\":30},{\"x\":-20,\"y\":33},{\"x\":-25,\"y\":30},{\"x\":-25,\"y\":33}]");
        sumValuesLLA = new JSONArray("[{\"x\":-20,\"y\":50},{\"x\":-20,\"y\":63},{\"x\":-25,\"y\":50},{\"x\":-25,\"y\":63}]");
        sumValuesNulos = new JSONArray("[{\"x\":-20,\"y\":80},{\"x\":-20,\"y\":93},{\"x\":-25,\"y\":80},{\"x\":-25,\"y\":93}]");
        sumValuesRecurridos = new JSONArray("[{\"x\":-20,\"y\":110},{\"x\":-20,\"y\":123},{\"x\":-25,\"y\":110},{\"x\":-25,\"y\":123}]");
        sumValuesImpugnados = new JSONArray("[{\"x\":-20,\"y\":140},{\"x\":-20,\"y\":153},{\"x\":-25,\"y\":140},{\"x\":-25,\"y\":153}]");
        sumValuesBlanco = new JSONArray("[{\"x\":-20,\"y\":170},{\"x\":-20,\"y\":183},{\"x\":-25,\"y\":170},{\"x\":-25,\"y\":183}]");
        sumValuesTotal = new JSONArray("[{\"x\":-20,\"y\":200},{\"x\":-20,\"y\":213},{\"x\":-5,\"y\":200},{\"x\":-5,\"y\":213}]");

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
        return sumValuesNulos;
    }

    public JSONArray getSumValuesRecurridos() {
        return sumValuesRecurridos;
    }

    public JSONArray getSumValuesImpugnados() {
        return sumValuesImpugnados;
    }

    public JSONArray getSumValuesBlanco() {
        return sumValuesBlanco;
    }

    public JSONArray getSumValuesTotal() {
        return sumValuesTotal;
    }

}


package com.example.unal.myapplication;

public class DataEmpresa {
    public String nombreEmpresa;
    public String urlE;
    public String telefono;
    public String email;
    public String productos;
    public String clasificacion;

    public DataEmpresa() {
        this.nombreEmpresa = "a";
        this.urlE = " ";
        this.telefono = " ";
        this.email = " ";
        this.productos = " ";
        this.clasificacion = " ";
    }

    public void updateNombreEmpresa(String x){
        this.nombreEmpresa = x;
    }
    public void updateUrlE(String x){
        this.urlE = x;
    }
    public void updateTelefono(String x){
        this.telefono = x;
    }
    public void updateEmail(String x){
        this.email = x;
    }
    public void updateProductos(String x){
        this.email = x;
    }
    public void updateClasificacion(String x){
        this.email = x;
    }
}

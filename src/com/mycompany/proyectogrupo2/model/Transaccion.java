package com.mycompany.proyectogrupo2.model;

public class Transaccion {
    private String idTransaccion;
    private String tipo;
    private String fecha;       // "YYYY-MM-DD"
    private String idEmpleado;
    private String idActivo;
    private String idSucursal;

    public Transaccion() {}

    public Transaccion(String idTransaccion, String tipo, String fecha,
                       String idEmpleado, String idActivo, String idSucursal) {
        this.idTransaccion = idTransaccion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.idEmpleado = idEmpleado;
        this.idActivo = idActivo;
        this.idSucursal = idSucursal;
    }

    public String getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(String idTransaccion) { this.idTransaccion = idTransaccion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getIdActivo() { return idActivo; }
    public void setIdActivo(String idActivo) { this.idActivo = idActivo; }

    public String getIdSucursal() { return idSucursal; }
    public void setIdSucursal(String idSucursal) { this.idSucursal = idSucursal; }

    @Override
    public String toString() {
        return "Transaccion{" +
                "idTransaccion=" + idTransaccion +
                ", tipo=" + tipo +
                ", fecha=" + fecha +
                ", idEmpleado=" + idEmpleado +
                ", idActivo=" + idActivo +
                ", idSucursal=" + idSucursal +
                '}';
    }
}

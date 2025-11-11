package com.mycompany.proyectogrupo2.model;

public class Sucursal {
    private String idSucursal;
    private String nombreSucursal;
    private String direccion;
    private String telefono;

    public Sucursal() {}

    public Sucursal(String idSucursal, String nombreSucursal, String direccion, String telefono) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getIdSucursal() { return idSucursal; }
    public void setIdSucursal(String idSucursal) { this.idSucursal = idSucursal; }

    public String getNombreSucursal() { return nombreSucursal; }
    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "Sucursal{" +
                "idSucursal=" + idSucursal +
                ", nombreSucursal=" + nombreSucursal +
                ", direccion=" + direccion +
                ", telefono=" + telefono +
                '}';
    }
}

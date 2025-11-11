package com.mycompany.proyectogrupo2.model;

public class PuestoEmpleado {
    private String idEmpleado;
    private String puesto;
    private String idSucursalTrabajo;

    public PuestoEmpleado() {}

    public PuestoEmpleado(String idEmpleado, String puesto, String idSucursalTrabajo) {
        this.idEmpleado = idEmpleado;
        this.puesto = puesto;
        this.idSucursalTrabajo = idSucursalTrabajo;
    }

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public String getIdSucursalTrabajo() { return idSucursalTrabajo; }
    public void setIdSucursalTrabajo(String idSucursalTrabajo) { this.idSucursalTrabajo = idSucursalTrabajo; }

    @Override
    public String toString() {
        return "PuestoEmpleado{" +
                "idEmpleado=" + idEmpleado +
                ", puesto=" + puesto +
                ", idSucursalTrabajo=" + idSucursalTrabajo +
                '}';
    }
}

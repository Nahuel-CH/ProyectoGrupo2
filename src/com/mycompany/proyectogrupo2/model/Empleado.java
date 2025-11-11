package com.mycompany.proyectogrupo2.model;

public class Empleado {
    private String idEmpleado;
    private String nombreEmpleado;
    private String apellido;
    private String cedula;

    public Empleado() {}

    public Empleado(String idEmpleado, String nombreEmpleado, String apellido, String cedula) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.apellido = apellido;
        this.cedula = cedula;
    }

    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", nombreEmpleado=" + nombreEmpleado +
                ", apellido=" + apellido +
                ", cedula=" + cedula +
                '}';
    }
}

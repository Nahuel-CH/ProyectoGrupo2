package com.mycompany.proyectogrupo2.model;

public class Cliente {
    private String idCliente;
    private String nombreCliente;
    private String apellido;
    private String cedula;
    private String direccion;
    private String telefono;
    private String correo;

    public Cliente() {}

    public Cliente(String idCliente, String nombreCliente, String apellido, String cedula,
                   String direccion, String telefono, String correo) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombreCliente=" + nombreCliente +
                ", apellido=" + apellido +
                ", cedula=" + cedula +
                ", direccion=" + direccion +
                ", telefono=" + telefono +
                ", correo=" + correo +
                '}';
    }
}

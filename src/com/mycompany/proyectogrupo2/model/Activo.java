package com.mycompany.proyectogrupo2.model;

public class Activo {
    private String idActivo;
    private String nombreActivo;
    private String codigoBarras;
    private double valorActivo;
    private String estado;
    private String fechaMantenimiento; // "YYYY-MM-DD" o null

    public Activo() {}

    public Activo(String idActivo, String nombreActivo, String codigoBarras,
                  double valorActivo, String estado, String fechaMantenimiento) {
        this.idActivo = idActivo;
        this.nombreActivo = nombreActivo;
        this.codigoBarras = codigoBarras;
        this.valorActivo = valorActivo;
        this.estado = estado;
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public String getIdActivo() { return idActivo; }
    public void setIdActivo(String idActivo) { this.idActivo = idActivo; }

    public String getNombreActivo() { return nombreActivo; }
    public void setNombreActivo(String nombreActivo) { this.nombreActivo = nombreActivo; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public double getValorActivo() { return valorActivo; }
    public void setValorActivo(double valorActivo) { this.valorActivo = valorActivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaMantenimiento() { return fechaMantenimiento; }
    public void setFechaMantenimiento(String fechaMantenimiento) { this.fechaMantenimiento = fechaMantenimiento; }

    @Override
    public String toString() {
        return "Activo{" +
                "idActivo=" + idActivo +
                ", nombreActivo=" + nombreActivo +
                ", codigoBarras=" + codigoBarras +
                ", valorActivo=" + valorActivo +
                ", estado=" + estado +
                ", fechaMantenimiento=" + fechaMantenimiento +
                '}';
    }
}


package com.mycompany.proyectogrupo2.model;

public class UbicacionActivo {
    private String idUbicacionActivo;
    private String idActivo;
    private String tipoUbicacion; // 'Sucursal' | 'Cliente' | 'Mantenimiento'
    private String idDestino;     // puede ser null
    private String fechaInicio;   // "YYYY-MM-DD"
    private String fechaFin;      // "YYYY-MM-DD" o null

    public UbicacionActivo() {}

    public UbicacionActivo(String idUbicacionActivo, String idActivo, String tipoUbicacion,
                           String idDestino, String fechaInicio, String fechaFin) {
        this.idUbicacionActivo = idUbicacionActivo;
        this.idActivo = idActivo;
        this.tipoUbicacion = tipoUbicacion;
        this.idDestino = idDestino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getIdUbicacionActivo() { return idUbicacionActivo; }
    public void setIdUbicacionActivo(String idUbicacionActivo) { this.idUbicacionActivo = idUbicacionActivo; }

    public String getIdActivo() { return idActivo; }
    public void setIdActivo(String idActivo) { this.idActivo = idActivo; }

    public String getTipoUbicacion() { return tipoUbicacion; }
    public void setTipoUbicacion(String tipoUbicacion) { this.tipoUbicacion = tipoUbicacion; }

    public String getIdDestino() { return idDestino; }
    public void setIdDestino(String idDestino) { this.idDestino = idDestino; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        return "UbicacionActivo{" +
                "idUbicacionActivo=" + idUbicacionActivo +
                ", idActivo=" + idActivo +
                ", tipoUbicacion=" + tipoUbicacion +
                ", idDestino=" + idDestino +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}

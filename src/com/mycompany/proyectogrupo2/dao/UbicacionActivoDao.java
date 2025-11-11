package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.UbicacionActivo;
import java.sql.Date;
import java.util.Optional;

public interface UbicacionActivoDao {
    int insertar(int idActivo, String tipo, Integer idDestino, Date fechaInicio, Date fechaFin) throws Exception;
    Optional<UbicacionActivo> ultimaPorActivo(int idActivo) throws Exception;
}

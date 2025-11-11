package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.Transaccion;
import java.sql.Date;
import java.util.List;

public interface TransaccionDao {
    int insertar(String tipo, Date fecha, int idEmpleado, int idActivo, int idSucursal) throws Exception;
    List<Transaccion> porActivo(int idActivo) throws Exception;
}

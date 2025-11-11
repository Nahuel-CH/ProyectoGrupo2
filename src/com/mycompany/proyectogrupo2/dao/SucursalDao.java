package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.Sucursal;
import java.util.List;
import java.util.Optional;

public interface SucursalDao {
    int insertar(Sucursal s) throws Exception;
    Optional<Sucursal> porId(int id) throws Exception;
    int actualizar(Sucursal s) throws Exception;
    int eliminar(int id) throws Exception;
    List<Sucursal> listar() throws Exception;
}

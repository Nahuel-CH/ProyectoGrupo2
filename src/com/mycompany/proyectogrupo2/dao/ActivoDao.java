package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.Activo;
import java.util.List;
import java.util.Optional;

public interface ActivoDao {
    int insertar(Activo a) throws Exception;
    Optional<Activo> porId(int id) throws Exception;
    int actualizar(Activo a) throws Exception;
    List<Activo> listar() throws Exception;
}

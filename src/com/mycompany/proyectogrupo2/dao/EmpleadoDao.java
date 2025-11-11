package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoDao {
    int insertar(Empleado e) throws Exception;
    Optional<Empleado> porId(int id) throws Exception;
    int actualizar(Empleado e) throws Exception;
    int eliminar(int id) throws Exception;
    List<Empleado> listar() throws Exception;
}

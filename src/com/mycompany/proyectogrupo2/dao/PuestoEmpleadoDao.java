package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.PuestoEmpleado;
import java.util.Optional;

public interface PuestoEmpleadoDao {
    int insertar(PuestoEmpleado p) throws Exception;
    Optional<PuestoEmpleado> porEmpleado(int idEmpleado) throws Exception;
    int actualizar(PuestoEmpleado p) throws Exception;
    int eliminar(int idEmpleado) throws Exception;
}

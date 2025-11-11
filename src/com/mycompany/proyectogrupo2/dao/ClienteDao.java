package com.mycompany.proyectogrupo2.dao;

import com.mycompany.proyectogrupo2.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteDao {
    int insertar(Cliente c) throws Exception;
    Optional<Cliente> buscarPorCedula(String cedula) throws Exception;
    int actualizar(Cliente c) throws Exception;
    int eliminarPorCedula(String cedula) throws Exception;
    List<Cliente> listar() throws Exception;
}

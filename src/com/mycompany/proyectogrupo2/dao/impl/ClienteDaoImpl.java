package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.Cliente;
import com.mycompany.proyectogrupo2.dao.ClienteDao;
import com.mycompany.proyectogrupo2.util.DBConnection;

import java.sql.*;
import java.util.*;

public class ClienteDaoImpl implements ClienteDao {

    @Override
    public int insertar(Cliente c) throws Exception {
        String sql = "{CALL sp_cliente_insertar(?,?,?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, c.getNombreCliente());
            cs.setString(2, c.getApellido());
            cs.setString(3, c.getCedula());
            cs.setString(4, c.getDireccion());
            cs.setString(5, c.getTelefono());
            cs.setString(6, c.getCorreo());
            boolean hasResult = cs.execute();
            if (hasResult) try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return rs.getInt("IdNuevo");
            }
            return -1;
        }
    }

    @Override
    public Optional<Cliente> buscarPorCedula(String cedula) throws Exception {
        String sql = "{CALL sp_cliente_por_cedula(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, cedula);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public int actualizar(Cliente c) throws Exception {
        String sql = "{CALL sp_cliente_actualizar(?,?,?,?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, Integer.parseInt(c.getIdCliente()));
            cs.setString(2, c.getNombreCliente());
            cs.setString(3, c.getApellido());
            cs.setString(4, c.getCedula());
            cs.setString(5, c.getDireccion());
            cs.setString(6, c.getTelefono());
            cs.setString(7, c.getCorreo());
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public int eliminarPorCedula(String cedula) throws Exception {
        String sql = "{CALL sp_cliente_eliminar_por_cedula(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, cedula);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public List<Cliente> listar() throws Exception {
        String sql = "{CALL sp_cliente_listar()}";
        List<Cliente> out = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(String.valueOf(rs.getInt("IdCliente")));
        c.setNombreCliente(rs.getString("NombreCliente"));
        c.setApellido(rs.getString("Apellido"));
        c.setCedula(rs.getString("Cedula"));
        c.setDireccion(rs.getString("Direccion"));
        c.setTelefono(rs.getString("Telefono"));
        c.setCorreo(rs.getString("Correo"));
        return c;
    }
}

package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.Sucursal;
import com.mycompany.proyectogrupo2.dao.SucursalDao;
import com.mycompany.proyectogrupo2.util.DBConnection;

import java.sql.*;
import java.util.*;

public class SucursalDaoImpl implements SucursalDao {

    @Override
    public int insertar(Sucursal s) throws Exception {
        String sql = "{CALL sp_sucursal_insertar(?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, s.getNombreSucursal());
            cs.setString(2, s.getDireccion());
            cs.setString(3, s.getTelefono());
            boolean hasResult = cs.execute();
            if (hasResult) try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return rs.getInt("IdNuevo");
            }
            return -1;
        }
    }

    @Override
    public Optional<Sucursal> porId(int id) throws Exception {
        String sql = "{CALL sp_sucursal_por_id(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public int actualizar(Sucursal s) throws Exception {
        String sql = "{CALL sp_sucursal_actualizar(?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, Integer.parseInt(s.getIdSucursal()));
            cs.setString(2, s.getNombreSucursal());
            cs.setString(3, s.getDireccion());
            cs.setString(4, s.getTelefono());
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public int eliminar(int id) throws Exception {
        String sql = "{CALL sp_sucursal_eliminar(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public List<Sucursal> listar() throws Exception {
        String sql = "{CALL sp_sucursal_listar()}";
        List<Sucursal> out = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    private Sucursal map(ResultSet rs) throws SQLException {
        Sucursal s = new Sucursal();
        s.setIdSucursal(String.valueOf(rs.getInt("IdSucursal")));
        s.setNombreSucursal(rs.getString("NombreSucursal"));
        s.setDireccion(rs.getString("Direccion"));
        s.setTelefono(rs.getString("Telefono"));
        return s;
    }
}


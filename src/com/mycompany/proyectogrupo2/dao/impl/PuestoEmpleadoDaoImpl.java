package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.PuestoEmpleado;
import com.mycompany.proyectogrupo2.dao.PuestoEmpleadoDao;
import com.mycompany.proyectogrupo2.util.DBConnection;

import java.sql.*;
import java.util.Optional;

public class PuestoEmpleadoDaoImpl implements PuestoEmpleadoDao {

    @Override
    public int insertar(PuestoEmpleado p) throws Exception {
        String sql = "{CALL sp_puesto_insertar(?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, Integer.parseInt(p.getIdEmpleado()));
            cs.setString(2, p.getPuesto());
            cs.setInt(3, Integer.parseInt(p.getIdSucursalTrabajo()));
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public Optional<PuestoEmpleado> porEmpleado(int idEmpleado) throws Exception {
        String sql = "{CALL sp_puesto_por_empleado(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, idEmpleado);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public int actualizar(PuestoEmpleado p) throws Exception {
        String sql = "{CALL sp_puesto_actualizar(?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, Integer.parseInt(p.getIdEmpleado()));
            cs.setString(2, p.getPuesto());
            cs.setInt(3, Integer.parseInt(p.getIdSucursalTrabajo()));
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public int eliminar(int idEmpleado) throws Exception {
        String sql = "{CALL sp_puesto_eliminar(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, idEmpleado);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    private PuestoEmpleado map(ResultSet rs) throws SQLException {
        PuestoEmpleado p = new PuestoEmpleado();
        p.setIdEmpleado(String.valueOf(rs.getInt("IdEmpleado")));
        p.setPuesto(rs.getString("Puesto"));
        p.setIdSucursalTrabajo(String.valueOf(rs.getInt("IdSucursalTrabajo")));
        return p;
    }
}

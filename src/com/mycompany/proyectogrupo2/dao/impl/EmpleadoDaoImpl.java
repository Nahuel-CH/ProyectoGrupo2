package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.Empleado;
import com.mycompany.proyectogrupo2.dao.EmpleadoDao;
import com.mycompany.proyectogrupo2.util.DBConnection;

import java.sql.*;
import java.util.*;

public class EmpleadoDaoImpl implements EmpleadoDao {

    @Override
    public int insertar(Empleado e) throws Exception {
        String sql = "{CALL sp_empleado_insertar(?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, e.getNombreEmpleado());
            cs.setString(2, e.getApellido());
            cs.setString(3, e.getCedula());
            boolean hasResult = cs.execute();
            if (hasResult) try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return rs.getInt("IdNuevo");
            }
            return -1;
        }
    }

    @Override
    public Optional<Empleado> porId(int id) throws Exception {
        String sql = "{CALL sp_empleado_por_id(?)}";
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
    public int actualizar(Empleado e) throws Exception {
        String sql = "{CALL sp_empleado_actualizar(?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, Integer.parseInt(e.getIdEmpleado()));
            cs.setString(2, e.getNombreEmpleado());
            cs.setString(3, e.getApellido());
            cs.setString(4, e.getCedula());
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public int eliminar(int id) throws Exception {
        String sql = "{CALL sp_empleado_eliminar(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public List<Empleado> listar() throws Exception {
        String sql = "{CALL sp_empleado_listar()}";
        List<Empleado> out = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    private Empleado map(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdEmpleado(String.valueOf(rs.getInt("IdEmpleado")));
        e.setNombreEmpleado(rs.getString("NombreEmpleado"));
        e.setApellido(rs.getString("Apellido"));
        e.setCedula(rs.getString("Cedula"));
        return e;
    }
}

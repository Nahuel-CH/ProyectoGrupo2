package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.Activo;
import com.mycompany.proyectogrupo2.dao.ActivoDao;
import com.mycompany.proyectogrupo2.util.DBConnection;
import com.mycompany.proyectogrupo2.util.DateUtil;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ActivoDaoImpl implements ActivoDao {

    @Override
    public int insertar(Activo a) throws Exception {
        String sql = "{CALL sp_activo_insertar(?,?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setString(1, a.getNombreActivo());
            cs.setString(2, a.getCodigoBarras());
            cs.setBigDecimal(3, java.math.BigDecimal.valueOf(a.getValorActivo()));
            cs.setString(4, a.getEstado());
            cs.setDate(5, DateUtil.toSqlDate(a.getFechaMantenimiento()));

            boolean hasResult = cs.execute();
            if (hasResult) try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return rs.getInt("IdNuevo");
            }
            return -1;
        }
    }

    @Override
    public Optional<Activo> porId(int id) throws Exception {
        String sql = "{CALL sp_activo_por_id(?)}";
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
    public int actualizar(Activo a) throws Exception {
        String sql = "{CALL sp_activo_actualizar(?,?,?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, Integer.parseInt(a.getIdActivo()));
            cs.setString(2, a.getNombreActivo());
            cs.setString(3, a.getCodigoBarras());
            cs.setBigDecimal(4, java.math.BigDecimal.valueOf(a.getValorActivo()));
            cs.setString(5, a.getEstado());
            cs.setDate(6, DateUtil.toSqlDate(a.getFechaMantenimiento()));

            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? rs.getInt("filas_afectadas") : 0;
            }
        }
    }

    @Override
    public List<Activo> listar() throws Exception {
        String sql = "{CALL sp_activo_listar()}";
        List<Activo> out = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    private Activo map(ResultSet rs) throws SQLException {
        Activo a = new Activo();
        a.setIdActivo(String.valueOf(rs.getInt("IdActivo")));
        a.setNombreActivo(rs.getString("NombreActivo"));
        a.setCodigoBarras(rs.getString("CodigoBarras"));
        a.setValorActivo(rs.getBigDecimal("ValorActivo").doubleValue());
        a.setEstado(rs.getString("Estado"));
        Date fm = rs.getDate("FechaMantenimiento");
        a.setFechaMantenimiento(fm == null ? null : fm.toString());
        return a;
    }
}

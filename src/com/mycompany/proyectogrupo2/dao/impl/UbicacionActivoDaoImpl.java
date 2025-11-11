package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.UbicacionActivo;
import com.mycompany.proyectogrupo2.dao.UbicacionActivoDao;
import com.mycompany.proyectogrupo2.util.DBConnection;

import java.sql.*;
import java.util.Optional;

public class UbicacionActivoDaoImpl implements UbicacionActivoDao {

    @Override
    public int insertar(int idActivo, String tipo, Integer idDestino, Date fechaInicio, Date fechaFin) throws Exception {
        String sql = "{CALL sp_ubicacion_insertar(?,?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, idActivo);
            cs.setString(2, tipo);
            if (idDestino == null) cs.setNull(3, Types.INTEGER); else cs.setInt(3, idDestino);
            cs.setDate(4, fechaInicio);
            if (fechaFin == null) cs.setNull(5, Types.DATE); else cs.setDate(5, fechaFin);

            boolean hasResult = cs.execute();
            if (hasResult) try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return rs.getInt("IdNuevo");
            }
            return -1;
        }
    }

    @Override
    public Optional<UbicacionActivo> ultimaPorActivo(int idActivo) throws Exception {
        String sql = "{CALL sp_ubicacion_ultima_por_activo(?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, idActivo);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    private UbicacionActivo map(ResultSet rs) throws SQLException {
        UbicacionActivo u = new UbicacionActivo();
        u.setIdUbicacionActivo(String.valueOf(rs.getInt("IdUbicacionActivo")));
        u.setIdActivo(String.valueOf(rs.getInt("IdActivo")));
        u.setTipoUbicacion(rs.getString("TipoUbicacion"));
        u.setIdDestino(rs.getObject("IdDestino") == null ? null : String.valueOf(rs.getInt("IdDestino")));
        Date fi = rs.getDate("FechaInicio");
        Date ff = rs.getDate("FechaFin");
        u.setFechaInicio(fi == null ? null : fi.toString());
        u.setFechaFin(ff == null ? null : ff.toString());
        return u;
    }
}

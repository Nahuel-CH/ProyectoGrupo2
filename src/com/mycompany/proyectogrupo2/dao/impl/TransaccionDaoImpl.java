package com.mycompany.proyectogrupo2.dao.impl;

import com.mycompany.proyectogrupo2.model.Transaccion;
import com.mycompany.proyectogrupo2.dao.TransaccionDao;
import com.mycompany.proyectogrupo2.util.DBConnection;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class TransaccionDaoImpl implements TransaccionDao {

    @Override
    public int insertar(String tipo, Date fecha, int idEmpleado, int idActivo, int idSucursal) throws Exception {
        String sql = "{CALL sp_transaccion_insertar(?,?,?,?,?)}";
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setString(1, tipo);
            cs.setDate(2, fecha);
            cs.setInt(3, idEmpleado);
            cs.setInt(4, idActivo);
            cs.setInt(5, idSucursal);

            boolean hasResult = cs.execute();
            if (hasResult) try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return rs.getInt("IdNuevo");
            }
            return -1;
        }
    }

    @Override
    public List<Transaccion> porActivo(int idActivo) throws Exception {
        String sql = "{CALL sp_transacciones_por_activo(?)}";
        List<Transaccion> out = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             CallableStatement cs = cn.prepareCall(sql)) {
            cs.setInt(1, idActivo);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    private Transaccion map(ResultSet rs) throws SQLException {
        Transaccion t = new Transaccion();
        t.setIdTransaccion(String.valueOf(rs.getInt("IdTransaccion")));
        t.setTipo(rs.getString("Tipo"));
        Date f = rs.getDate("Fecha");
        t.setFecha(f == null ? null : f.toString());
        t.setIdEmpleado(String.valueOf(rs.getInt("IdEmpleado")));
        t.setIdActivo(String.valueOf(rs.getInt("IdActivo")));
        t.setIdSucursal(String.valueOf(rs.getInt("IdSucursal")));
        return t;
    }
}

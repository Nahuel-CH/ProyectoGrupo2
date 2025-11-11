package com.mycompany.proyectogrupo2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL  = "jdbc:mysql://localhost:3306/inventario_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "0414"; //

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

package com.mycompany.proyectogrupo2.util;

import java.sql.Date;

public class DateUtil {
    public static Date toSqlDate(String yyyyMMdd) {
        if (yyyyMMdd == null || yyyyMMdd.isBlank()) return null;
        return Date.valueOf(yyyyMMdd); // "YYYY-MM-DD"
    }
    public static String fromSqlDate(Date d) {
        return (d == null) ? null : d.toString();
    }
}

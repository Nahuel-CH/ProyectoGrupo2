package com.mycompany.proyectogrupo2;

import com.mycompany.proyectogrupo2.dao.*;
import com.mycompany.proyectogrupo2.dao.impl.*;
import com.mycompany.proyectogrupo2.model.*;
import java.sql.Date;

public class ProyectoGrupo2 {
    public static void main(String[] args) throws Exception {
        // Instancias DAO
        ClienteDao cdao = new ClienteDaoImpl();
        EmpleadoDao edao = new EmpleadoDaoImpl();
        SucursalDao sdao = new SucursalDaoImpl();
        ActivoDao adao = new ActivoDaoImpl();
        UbicacionActivoDao udao = new UbicacionActivoDaoImpl();
        TransaccionDao tdao = new TransaccionDaoImpl();

        // ==== PRUEBAS RÁPIDAS ====

        // 1) INSERT Cliente
        Cliente c = new Cliente(null,"Ana","Pérez","123456789","San José","8888-8888","ana@correo.com");
        int idCliente = cdao.insertar(c);
        System.out.println("Cliente insertado: " + idCliente);

        // 2) INSERT Empleado y Sucursal
        Empleado e = new Empleado(null,"Luis","Rodríguez","222222222");
        int idEmp = edao.insertar(e);
        Sucursal s = new Sucursal(null,"Sucursal Centro","SJ Centro","2222-2222");
        int idSuc = sdao.insertar(s);
        System.out.println("Empleado=" + idEmp + "  Sucursal=" + idSuc);

        // 3) INSERT Activo
        Activo a = new Activo(null,"Bomba Infusión","CB-001", 1200.00,"Activo", null);
        int idAct = adao.insertar(a);
        System.out.println("Activo=" + idAct);

        // 4) INSERT Transacción: Entrega a sucursal
        int idTx = tdao.insertar("Entrega", Date.valueOf("2025-01-15"), idEmp, idAct, idSuc);
        System.out.println("Transacción=" + idTx);

        // 5) INSERT Ubicación vigente en sucursal (FechaFin NULL)
        int idUb = udao.insertar(idAct, "Sucursal", idSuc, Date.valueOf("2025-01-15"), null);
        System.out.println("Ubicación=" + idUb);

        // 6) LISTAR transacciones por activo
        tdao.porActivo(idAct).forEach(System.out::println);

        // 7) CRUD Cliente (buscar/actualizar/eliminar)
        System.out.println("Buscar: " + cdao.buscarPorCedula("123456789").orElse(null));
        Cliente upd = new Cliente(String.valueOf(idCliente), "Ana María","Pérez","123456789","SJ Centro","8888-0000","ana@correo.com");
        System.out.println("Filas actualizadas: " + cdao.actualizar(upd));
        System.out.println("Filas eliminadas: " + cdao.eliminarPorCedula("123456789"));
    }
}

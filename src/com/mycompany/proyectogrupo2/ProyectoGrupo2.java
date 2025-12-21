package com.mycompany.proyectogrupo2;

import com.mycompany.proyectogrupo2.dao.ActivoDao;
import com.mycompany.proyectogrupo2.dao.ClienteDao;
import com.mycompany.proyectogrupo2.dao.EmpleadoDao;
import com.mycompany.proyectogrupo2.dao.SucursalDao;
import com.mycompany.proyectogrupo2.dao.TransaccionDao;
import com.mycompany.proyectogrupo2.dao.UbicacionActivoDao;

import com.mycompany.proyectogrupo2.dao.impl.ActivoDaoImpl;
import com.mycompany.proyectogrupo2.dao.impl.ClienteDaoImpl;
import com.mycompany.proyectogrupo2.dao.impl.EmpleadoDaoImpl;
import com.mycompany.proyectogrupo2.dao.impl.SucursalDaoImpl;
import com.mycompany.proyectogrupo2.dao.impl.TransaccionDaoImpl;
import com.mycompany.proyectogrupo2.dao.impl.UbicacionActivoDaoImpl;

import com.mycompany.proyectogrupo2.model.Activo;
import com.mycompany.proyectogrupo2.model.Cliente;
import com.mycompany.proyectogrupo2.model.Empleado;
import com.mycompany.proyectogrupo2.model.Sucursal;
import com.mycompany.proyectogrupo2.model.Transaccion;
import com.mycompany.proyectogrupo2.model.UbicacionActivo;

import com.mycompany.proyectogrupo2.util.DBConnection;
import com.mycompany.proyectogrupo2.util.pantallas.VentanaPrincipal;

import java.sql.Date;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ProyectoGrupo2 {

    public static void main(String[] args) {
        boolean runTests = false;

        if (args != null) {
            for (String a : args) {
                if ("--tests".equalsIgnoreCase(a) || "-t".equalsIgnoreCase(a)) {
                    runTests = true;
                    break;
                }
            }
        }

        // Si ejecutás con --tests, corre pruebas rápidas y sale
        if (runTests) {
            runQuickTests();
            return;
        }

        // Caso normal: levantar GUI (VentanaPrincipal)
        if (!checkDbConnection()) {
            return; // mensaje ya mostrado
        }

        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    private static boolean checkDbConnection() {
        try {
            DBConnection.getConnection().close();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar a la base de datos.\n\n"
                            + "Verificá que:\n"
                            + "1) MySQL esté encendido\n"
                            + "2) Exista la BD 'inventario_db'\n"
                            + "3) Usuario/contraseña/puerto estén correctos en DBConnection\n"
                            + "4) Tengás agregado el MySQL Connector/J al proyecto\n\n"
                            + "Detalle: " + ex.getMessage(),
                    "Error de conexión a BD",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    private static void runQuickTests() {
        try {
            // Pre-flight DB check también en tests
            if (!checkDbConnection()) {
                return;
            }

            // Instancias DAO
            ClienteDao cdao = new ClienteDaoImpl();
            EmpleadoDao edao = new EmpleadoDaoImpl();
            SucursalDao sdao = new SucursalDaoImpl();
            ActivoDao adao = new ActivoDaoImpl();
            UbicacionActivoDao udao = new UbicacionActivoDaoImpl();
            TransaccionDao tdao = new TransaccionDaoImpl();

            System.out.println("==== PRUEBAS RÁPIDAS ====");

            // 1) INSERT Cliente
            Cliente c = new Cliente(
                    null,
                    "Ana",
                    "Pérez",
                    "123456789",
                    "San José",
                    "8888-8888",
                    "ana@correo.com"
            );
            int idCliente = cdao.insertar(c);
            System.out.println("Cliente insertado: " + idCliente);

            // 2) INSERT Sucursal y Empleado
            Sucursal s = new Sucursal(null, "Sucursal Central", "San José");
            int idSuc = sdao.insertar(s);
            System.out.println("Sucursal insertada: " + idSuc);

            Empleado e = new Empleado(
                    null,
                    "Carlos",
                    "Ramírez",
                    "999999999",
                    "carlos@correo.com",
                    idSuc
            );
            int idEmp = edao.insertar(e);
            System.out.println("Empleado insertado: " + idEmp);

            // 3) INSERT Activo
            Activo a = new Activo(
                    null,
                    "Laptop Dell",
                    "Equipo",
                    new Date(System.currentTimeMillis()),
                    "Bueno",
                    idCliente
            );
            int idActivo = adao.insertar(a);
            System.out.println("Activo insertado: " + idActivo);

            // 4) INSERT Ubicación
            UbicacionActivo ua = new UbicacionActivo(
                    null,
                    idActivo,
                    "Bodega 1",
                    new Date(System.currentTimeMillis())
            );
            int idUbic = udao.insertar(ua);
            System.out.println("Ubicación insertada: " + idUbic);

            // 5) INSERT Transacción
            Transaccion t = new Transaccion(
                    null,
                    idActivo,
                    "Mantenimiento",
                    new Date(System.currentTimeMillis()),
                    idEmp
            );
            int idTrans = tdao.insertar(t);
            System.out.println("Transacción insertada: " + idTrans);

            // 6) LISTAR Clientes
            System.out.println("\nClientes:");
            cdao.listar().forEach(System.out::println);

            // 7) ELIMINAR Cliente por cédula
            int eliminados = cdao.eliminarPorCedula("123456789");
            System.out.println("\nClientes eliminados: " + eliminados);

            System.out.println("==== FIN PRUEBAS RÁPIDAS ====");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Falló la ejecución de las pruebas rápidas.\n\nDetalle: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

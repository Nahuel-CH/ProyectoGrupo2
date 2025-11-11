CREATE DATABASE IF NOT EXISTS inventario_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE inventario_db;

/* BASE DE DATOS */
DROP VIEW IF EXISTS v_mantenimientos_recientes;
DROP VIEW IF EXISTS v_traslados;
DROP VIEW IF EXISTS v_ultima_ubicacion_activo;
DROP VIEW IF EXISTS v_activos_sin_movimientos;
DROP VIEW IF EXISTS v_transacciones_mensuales;
DROP VIEW IF EXISTS v_valor_activos_por_sucursal;
DROP VIEW IF EXISTS v_activos_por_sucursal;
DROP VIEW IF EXISTS v_transacciones_detalle;
DROP VIEW IF EXISTS v_activos_detalle;
DROP VIEW IF EXISTS v_empleados_detalle;
DROP TRIGGER IF EXISTS trg_valida_valor_activo;
DROP TRIGGER IF EXISTS trg_valida_valor_activo_upd;
DROP TRIGGER IF EXISTS trg_valida_fecha_ubicacion_ins;
DROP TRIGGER IF EXISTS trg_valida_fecha_ubicacion_upd;
DROP TRIGGER IF EXISTS trg_prevenir_eliminar_sucursal;
DROP PROCEDURE IF EXISTS sp_cliente_insertar;
DROP PROCEDURE IF EXISTS sp_cliente_por_cedula;
DROP PROCEDURE IF EXISTS sp_cliente_actualizar;
DROP PROCEDURE IF EXISTS sp_cliente_eliminar_por_cedula;
DROP PROCEDURE IF EXISTS sp_cliente_listar;
DROP PROCEDURE IF EXISTS sp_sucursal_insertar;
DROP PROCEDURE IF EXISTS sp_sucursal_por_id;
DROP PROCEDURE IF EXISTS sp_sucursal_actualizar;
DROP PROCEDURE IF EXISTS sp_sucursal_eliminar;
DROP PROCEDURE IF EXISTS sp_sucursal_listar;
DROP PROCEDURE IF EXISTS sp_empleado_insertar;
DROP PROCEDURE IF EXISTS sp_empleado_por_id;
DROP PROCEDURE IF EXISTS sp_empleado_actualizar;
DROP PROCEDURE IF EXISTS sp_empleado_eliminar;
DROP PROCEDURE IF EXISTS sp_empleado_listar;
DROP PROCEDURE IF EXISTS sp_puesto_insertar;
DROP PROCEDURE IF EXISTS sp_puesto_por_empleado;
DROP PROCEDURE IF EXISTS sp_puesto_actualizar;
DROP PROCEDURE IF EXISTS sp_puesto_eliminar;
DROP PROCEDURE IF EXISTS sp_activo_insertar;
DROP PROCEDURE IF EXISTS sp_activo_por_id;
DROP PROCEDURE IF EXISTS sp_activo_actualizar;
DROP PROCEDURE IF EXISTS sp_activo_listar;
DROP PROCEDURE IF EXISTS sp_ubicacion_insertar;
DROP PROCEDURE IF EXISTS sp_ubicacion_ultima_por_activo;
DROP PROCEDURE IF EXISTS sp_transaccion_insertar;
DROP PROCEDURE IF EXISTS sp_transacciones_por_activo;

/* TABLAS */
CREATE TABLE IF NOT EXISTS cliente (
  IdCliente INT AUTO_INCREMENT PRIMARY KEY,
  NombreCliente VARCHAR(100) NOT NULL,
  Apellido VARCHAR(100) NOT NULL,
  Cedula VARCHAR(20) NOT NULL UNIQUE,
  Direccion VARCHAR(255) NOT NULL,
  Telefono VARCHAR(15) NOT NULL,
  Correo VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sucursal (
  IdSucursal INT AUTO_INCREMENT PRIMARY KEY,
  NombreSucursal VARCHAR(100) NOT NULL,
  Direccion VARCHAR(255) NOT NULL,
  Telefono VARCHAR(15) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS empleado (
  IdEmpleado INT AUTO_INCREMENT PRIMARY KEY,
  NombreEmpleado VARCHAR(100) NOT NULL,
  Apellido VARCHAR(100) NOT NULL,
  Cedula VARCHAR(20) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS PuestoEmpleado (
  IdEmpleado INT NOT NULL,
  Puesto VARCHAR(50) NOT NULL,
  IdSucursalTrabajo INT NOT NULL,
  FOREIGN KEY (IdEmpleado) REFERENCES empleado(IdEmpleado) ON DELETE CASCADE,
  FOREIGN KEY (IdSucursalTrabajo) REFERENCES sucursal(IdSucursal)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Activo (
  IdActivo INT AUTO_INCREMENT PRIMARY KEY,
  NombreActivo VARCHAR(100) NOT NULL,
  CodigoBarras VARCHAR(50) NOT NULL UNIQUE,
  ValorActivo DECIMAL(12,2) NOT NULL,
  Estado VARCHAR(20) DEFAULT 'Activo' CHECK (Estado IN ('Activo','Cuarentena','Mantenimiento','Inactivo')),
  FechaMantenimiento DATE NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS UbicacionActivo (
  IdUbicacionActivo INT AUTO_INCREMENT PRIMARY KEY,
  IdActivo INT NOT NULL,
  TipoUbicacion VARCHAR(50) NOT NULL CHECK (TipoUbicacion IN ('Sucursal','Cliente','Mantenimiento')),
  IdDestino INT NULL,
  FechaInicio DATE NOT NULL,
  FechaFin DATE NULL,
  FOREIGN KEY (IdActivo) REFERENCES Activo(IdActivo) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Transaccion (
  IdTransaccion INT AUTO_INCREMENT PRIMARY KEY,
  Tipo VARCHAR(50) NOT NULL CHECK (Tipo IN ('Entrega','Devolucion','Mantenimiento','Traslado Sede')),
  Fecha DATE NOT NULL,
  IdEmpleado INT NOT NULL,
  IdActivo INT NOT NULL,
  IdSucursal INT NOT NULL,
  FOREIGN KEY (IdEmpleado) REFERENCES empleado(IdEmpleado),
  FOREIGN KEY (IdActivo) REFERENCES activo(IdActivo),
  FOREIGN KEY (IdSucursal) REFERENCES sucursal(IdSucursal)
) ENGINE=InnoDB;

/* VISTAS */
CREATE OR REPLACE VIEW v_empleados_detalle AS
SELECT e.IdEmpleado, e.NombreEmpleado, e.Apellido, p.Puesto, s.NombreSucursal
FROM empleado e
LEFT JOIN PuestoEmpleado p ON p.IdEmpleado = e.IdEmpleado
LEFT JOIN sucursal s ON p.IdSucursalTrabajo = s.IdSucursal;

CREATE OR REPLACE VIEW v_activos_detalle AS
SELECT a.IdActivo, a.NombreActivo, a.CodigoBarras, a.ValorActivo, a.Estado, a.FechaMantenimiento
FROM Activo a;

CREATE OR REPLACE VIEW v_transacciones_detalle AS
SELECT t.IdTransaccion, t.Fecha, t.Tipo, t.IdActivo, a.NombreActivo,
       t.IdEmpleado, CONCAT(e.NombreEmpleado,' ',e.Apellido) AS Empleado,
       t.IdSucursal, s.NombreSucursal
FROM Transaccion t
LEFT JOIN Activo a ON a.IdActivo = t.IdActivo
LEFT JOIN Empleado e ON e.IdEmpleado = t.IdEmpleado
LEFT JOIN Sucursal s ON s.IdSucursal = t.IdSucursal;

CREATE OR REPLACE VIEW v_activos_por_sucursal AS
SELECT s.IdSucursal, s.NombreSucursal, COUNT(t.IdActivo) AS TotalActivos
FROM Sucursal s
LEFT JOIN Transaccion t ON t.IdSucursal = s.IdSucursal
GROUP BY s.IdSucursal, s.NombreSucursal;

CREATE OR REPLACE VIEW v_valor_activos_por_sucursal AS
SELECT s.IdSucursal, s.NombreSucursal, COALESCE(SUM(a.ValorActivo),0) AS ValorTotal
FROM Sucursal s
LEFT JOIN Transaccion t ON t.IdSucursal = s.IdSucursal
LEFT JOIN Activo a ON a.IdActivo = t.IdActivo
GROUP BY s.IdSucursal, s.NombreSucursal;

CREATE OR REPLACE VIEW v_transacciones_mensuales AS
SELECT DATE_FORMAT(t.Fecha,'%Y-%m') AS Mes, t.Tipo, COUNT(*) AS Total
FROM Transaccion t
GROUP BY DATE_FORMAT(t.Fecha,'%Y-%m'), t.Tipo
ORDER BY Mes DESC;

CREATE OR REPLACE VIEW v_activos_sin_movimientos AS
SELECT a.*
FROM Activo a
LEFT JOIN Transaccion t ON t.IdActivo = a.IdActivo
WHERE t.IdTransaccion IS NULL;

CREATE OR REPLACE VIEW v_ultima_ubicacion_activo AS
SELECT ua.IdActivo, ua.TipoUbicacion, ua.IdDestino, ua.FechaInicio, ua.FechaFin
FROM UbicacionActivo ua
JOIN (
  SELECT IdActivo, MAX(COALESCE(FechaFin,'9999-12-31')) AS max_fin
  FROM UbicacionActivo
  GROUP BY IdActivo
) m ON m.IdActivo = ua.IdActivo AND m.max_fin = COALESCE(ua.FechaFin,'9999-12-31');

CREATE OR REPLACE VIEW v_traslados AS
SELECT t.IdTransaccion, t.Fecha, t.Tipo, t.IdActivo, a.NombreActivo, t.IdSucursal
FROM Transaccion t
JOIN Activo a ON a.IdActivo = t.IdActivo
WHERE t.Tipo = 'Traslado Sede';

CREATE OR REPLACE VIEW v_mantenimientos_recientes AS
SELECT t.*, a.NombreActivo
FROM Transaccion t
JOIN Activo a ON a.IdActivo = t.IdActivo
WHERE t.Tipo = 'Mantenimiento'
  AND t.Fecha >= DATE_SUB(CURDATE(), INTERVAL 90 DAY);

/* PROCEDIMIENTOS */
DELIMITER $$

CREATE PROCEDURE sp_cliente_insertar(
  IN p_nombreCliente VARCHAR(100),
  IN p_apellido VARCHAR(100),
  IN p_cedula VARCHAR(20),
  IN p_direccion VARCHAR(255),
  IN p_telefono VARCHAR(15),
  IN p_correo VARCHAR(100)
)
BEGIN
  INSERT INTO cliente (NombreCliente, Apellido, Cedula, Direccion, Telefono, Correo)
  VALUES (p_nombreCliente, p_apellido, p_cedula, p_direccion, p_telefono, p_correo);
  SELECT LAST_INSERT_ID() AS IdNuevo;
END$$

CREATE PROCEDURE sp_cliente_por_cedula(IN p_cedula VARCHAR(20))
BEGIN
  SELECT * FROM cliente WHERE Cedula = p_cedula;
END$$

CREATE PROCEDURE sp_cliente_actualizar(
  IN p_IdCliente INT,
  IN p_nombreCliente VARCHAR(100),
  IN p_apellido VARCHAR(100),
  IN p_cedula VARCHAR(20),
  IN p_direccion VARCHAR(255),
  IN p_telefono VARCHAR(15),
  IN p_correo VARCHAR(100)
)
BEGIN
  UPDATE cliente
     SET NombreCliente = p_nombreCliente,
         Apellido = p_apellido,
         Cedula = p_cedula,
         Direccion = p_direccion,
         Telefono = p_telefono,
         Correo = p_correo
   WHERE IdCliente = p_IdCliente;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_cliente_eliminar_por_cedula(IN p_cedula VARCHAR(20))
BEGIN
  DELETE FROM cliente WHERE Cedula = p_cedula;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_cliente_listar()
BEGIN
  SELECT * FROM cliente ORDER BY NombreCliente;
END$$

CREATE PROCEDURE sp_sucursal_insertar(
  IN p_nombreSucursal VARCHAR(100),
  IN p_direccion VARCHAR(255),
  IN p_telefono VARCHAR(15)
)
BEGIN
  INSERT INTO sucursal (NombreSucursal, Direccion, Telefono)
  VALUES (p_nombreSucursal, p_direccion, p_telefono);
  SELECT LAST_INSERT_ID() AS IdNuevo;
END$$

CREATE PROCEDURE sp_sucursal_por_id(IN p_IdSucursal INT)
BEGIN
  SELECT * FROM sucursal WHERE IdSucursal = p_IdSucursal;
END$$

CREATE PROCEDURE sp_sucursal_actualizar(
  IN p_idSucursal INT,
  IN p_nombreSucursal VARCHAR(100),
  IN p_direccion VARCHAR(255),
  IN p_telefono VARCHAR(15)
)
BEGIN
  UPDATE sucursal
     SET NombreSucursal = p_nombreSucursal,
         Direccion = p_direccion,
         Telefono = p_telefono
   WHERE IdSucursal = p_idSucursal;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_sucursal_eliminar(IN p_idSucursal INT)
BEGIN
  DELETE FROM sucursal WHERE IdSucursal = p_idSucursal;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_sucursal_listar()
BEGIN
  SELECT * FROM sucursal ORDER BY NombreSucursal;
END$$

CREATE PROCEDURE sp_empleado_insertar(
  IN p_nombreEmpleado VARCHAR(100),
  IN p_apellido VARCHAR(100),
  IN p_cedula VARCHAR(20)
)
BEGIN
  INSERT INTO empleado (NombreEmpleado, Apellido, Cedula)
  VALUES (p_nombreEmpleado, p_apellido, p_cedula);
  SELECT LAST_INSERT_ID() AS IdNuevo;
END$$

CREATE PROCEDURE sp_empleado_por_id(IN p_idEmpleado INT)
BEGIN
  SELECT * FROM empleado WHERE IdEmpleado = p_idEmpleado;
END$$

CREATE PROCEDURE sp_empleado_actualizar(
  IN p_idEmpleado INT,
  IN p_nombreEmpleado VARCHAR(100),
  IN p_apellido VARCHAR(100),
  IN p_cedula VARCHAR(20)
)
BEGIN
  UPDATE empleado
     SET NombreEmpleado = p_nombreEmpleado,
         Apellido = p_apellido,
         Cedula = p_cedula
   WHERE IdEmpleado = p_idEmpleado;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_empleado_eliminar(IN p_idEmpleado INT)
BEGIN
  DELETE FROM empleado WHERE IdEmpleado = p_idEmpleado;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_empleado_listar()
BEGIN
  SELECT * FROM empleado ORDER BY IdEmpleado;
END$$

CREATE PROCEDURE sp_puesto_insertar(
  IN p_idEmpleado INT,
  IN p_puesto VARCHAR(50),
  IN p_idSucursalTrabajo INT
)
BEGIN
  INSERT INTO PuestoEmpleado (IdEmpleado, Puesto, IdSucursalTrabajo)
  VALUES (p_idEmpleado, p_puesto, p_idSucursalTrabajo);
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_puesto_por_empleado(IN p_idEmpleado INT)
BEGIN
  SELECT * FROM PuestoEmpleado WHERE IdEmpleado = p_idEmpleado;
END$$

CREATE PROCEDURE sp_puesto_actualizar(
  IN p_idEmpleado INT,
  IN p_puesto VARCHAR(50),
  IN p_idSucursalTrabajo INT
)
BEGIN
  UPDATE PuestoEmpleado
     SET Puesto = p_puesto,
         IdSucursalTrabajo = p_idSucursalTrabajo
   WHERE IdEmpleado = p_idEmpleado;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_puesto_eliminar(IN p_idEmpleado INT)
BEGIN
  DELETE FROM PuestoEmpleado WHERE IdEmpleado = p_idEmpleado;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_activo_insertar(
  IN p_nombreActivo VARCHAR(100),
  IN p_codigoBarras VARCHAR(50),
  IN p_valorActivo DECIMAL(12,2),
  IN p_estado VARCHAR(20),
  IN p_fechaMantenimiento DATE
)
BEGIN
  INSERT INTO Activo (NombreActivo, CodigoBarras, ValorActivo, Estado, FechaMantenimiento)
  VALUES (p_nombreActivo, p_codigoBarras, p_valorActivo, p_estado, p_fechaMantenimiento);
  SELECT LAST_INSERT_ID() AS IdNuevo;
END$$

CREATE PROCEDURE sp_activo_por_id(IN p_idActivo INT)
BEGIN
  SELECT * FROM Activo WHERE IdActivo = p_idActivo;
END$$

CREATE PROCEDURE sp_activo_actualizar(
  IN p_idActivo INT,
  IN p_nombreActivo VARCHAR(100),
  IN p_codigoBarras VARCHAR(50),
  IN p_valorActivo DECIMAL(12,2),
  IN p_estado VARCHAR(20),
  IN p_fechaMantenimiento DATE
)
BEGIN
  UPDATE Activo
     SET NombreActivo = p_nombreActivo,
         CodigoBarras = p_codigoBarras,
         ValorActivo = p_valorActivo,
         Estado = p_estado,
         FechaMantenimiento = p_fechaMantenimiento
   WHERE IdActivo = p_idActivo;
  SELECT ROW_COUNT() AS filas_afectadas;
END$$

CREATE PROCEDURE sp_activo_listar()
BEGIN
  SELECT * FROM Activo ORDER BY IdActivo;
END$$

CREATE PROCEDURE sp_ubicacion_insertar(
  IN p_idActivo INT,
  IN p_tipoUbicacion VARCHAR(50),
  IN p_idDestino INT,
  IN p_fechaInicio DATE,
  IN p_fechaFin DATE
)
BEGIN
  INSERT INTO UbicacionActivo (IdActivo, TipoUbicacion, IdDestino, FechaInicio, FechaFin)
  VALUES (p_idActivo, p_tipoUbicacion, p_idDestino, p_fechaInicio, p_fechaFin);
  SELECT LAST_INSERT_ID() AS IdNuevo;
END$$

CREATE PROCEDURE sp_ubicacion_ultima_por_activo(IN p_idActivo INT)
BEGIN
  SELECT * FROM v_ultima_ubicacion_activo WHERE IdActivo = p_idActivo;
END$$

CREATE PROCEDURE sp_transaccion_insertar(
  IN p_tipo VARCHAR(50),
  IN p_fecha DATE,
  IN p_idEmpleado INT,
  IN p_idActivo INT,
  IN p_idSucursal INT
)
BEGIN
  INSERT INTO Transaccion (Tipo, Fecha, IdEmpleado, IdActivo, IdSucursal)
  VALUES (p_tipo, p_fecha, p_idEmpleado, p_idActivo, p_idSucursal);
  SELECT LAST_INSERT_ID() AS IdNuevo;
END$$

CREATE PROCEDURE sp_transacciones_por_activo(IN p_idActivo INT)
BEGIN
  SELECT * FROM Transaccion WHERE IdActivo = p_idActivo ORDER BY Fecha ASC;
END$$

DELIMITER ;

/* TRIGGERS */
DELIMITER $$

CREATE TRIGGER trg_valida_valor_activo
BEFORE INSERT ON Activo
FOR EACH ROW
BEGIN
  IF NEW.ValorActivo < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El valor del activo no puede ser negativo';
  END IF;
END$$

CREATE TRIGGER trg_valida_valor_activo_upd
BEFORE UPDATE ON Activo
FOR EACH ROW
BEGIN
  IF NEW.ValorActivo < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El valor del activo no puede ser negativo';
  END IF;
END$$

CREATE TRIGGER trg_valida_fecha_ubicacion_ins
BEFORE INSERT ON UbicacionActivo
FOR EACH ROW
BEGIN
  IF NEW.FechaFin IS NOT NULL AND NEW.FechaFin < NEW.FechaInicio THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'FechaFin no puede ser anterior a FechaInicio';
  END IF;
END$$

CREATE TRIGGER trg_valida_fecha_ubicacion_upd
BEFORE UPDATE ON UbicacionActivo
FOR EACH ROW
BEGIN
  IF NEW.FechaFin IS NOT NULL AND NEW.FechaFin < NEW.FechaInicio THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'FechaFin no puede ser anterior a FechaInicio';
  END IF;
END$$

CREATE TRIGGER trg_prevenir_eliminar_sucursal
BEFORE DELETE ON Sucursal
FOR EACH ROW
BEGIN
  IF EXISTS (
    SELECT 1 FROM UbicacionActivo
     WHERE IdDestino = OLD.IdSucursal
       AND TipoUbicacion = 'Sucursal'
       AND (FechaFin IS NULL OR FechaFin >= CURDATE())
  ) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar la sucursal porque tiene activos asignados';
  END IF;
END$$

DELIMITER ;

/* DATOS DE PRUEBA */
INSERT INTO sucursal (NombreSucursal, Direccion, Telefono)
VALUES ('Sucursal Centro','SJ Centro','2222-2222');

INSERT INTO empleado (NombreEmpleado, Apellido, Cedula)
VALUES ('Luis','Rodríguez','222222222');

INSERT INTO activo (NombreActivo, CodigoBarras, ValorActivo, Estado)
VALUES ('Bomba Infusión','CB-001',1200.00,'Activo');

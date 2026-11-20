# Sistema de préstamos y cobranza

Aplicación de escritorio en Java para gestionar préstamos: clientes, solicitudes y aprobación, cuotas, pagos, moras, comprobantes y reportes. Proyecto académico del curso de Desarrollo de Aplicaciones Empresariales (semestre anterior, USAT).

## Qué hace

- Registro de clientes y solicitud de préstamos, con aprobación.
- Generación automática de cuotas y control de pagos (parciales o totales) por método de pago.
- Cálculo de moras y regularización.
- Comprobantes de pago y notificaciones de cuotas por vencer.
- Historial crediticio de cada cliente.
- Inicio de sesión de trabajadores con menú según las opciones permitidas.
- Reportes de cartera, morosidad, métodos de pago, rendimiento por trabajador e historial del cliente.

## Tecnologías

Java (NetBeans, Swing), PostgreSQL y JDBC. La lógica de negocio importante está en la base de datos: 13 tablas y 13 procedimientos y funciones almacenados en PL/pgSQL, por ejemplo `generar_cuotas`, `calcular_moras_prestamo`, `insertar_pago` y `aprobar_prestamo`.

## Estructura

- `src/from`: pantallas (formularios Swing).
- `src/ad`: acceso a datos, que llama a las consultas y procedimientos.
- `src/ln`: clases de lógica de negocio.
- `src/db`: conexión a la base de datos.
- `database/`: respaldo de la base de datos.

## Cómo ejecutarlo

1. Instala PostgreSQL y crea la base `proyecto_DAE`.
2. Restaura el respaldo: `pg_restore -U postgres -d proyecto_DAE database/bd_proyectodae.dump` (el respaldo se generó con PostgreSQL 18).
3. Define la contraseña de la base como variable de entorno: `DB_PASS`.
4. Abre el proyecto en NetBeans, agrega el driver JDBC de PostgreSQL a las librerías y ejecuta `main.main`.

## Autor

Carlos Daniel Primo Vilchez. Ingeniería de Sistemas y Computación, USAT.

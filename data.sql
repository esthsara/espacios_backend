--Nuevo

--administrador
-- -------------------------------
-- Administrador 1
-- -------------------------------
INSERT INTO persona (fecha_nacimiento, nombre, a_paterno, a_materno, telefono, email, url_imagen, estado)
VALUES ('1985-04-12', 'Carlos', 'Lopez', 'Gomez', '71234567', 'carlos.lopez@gmail.com', 'https://example.com/carlos.jpg', TRUE)
RETURNING id_persona;

-- Supongamos que id_persona = 1
INSERT INTO administrador (id_persona, cargo, direccion)
VALUES (1, 'Gerente', 'Av. Siempre Viva 123');

-- -------------------------------
-- Administrador 2
-- -------------------------------
INSERT INTO persona (fecha_nacimiento, nombre, a_paterno, a_materno, telefono, email, url_imagen, estado)
VALUES ('1990-06-25', 'Ana', 'Martinez', 'Ruiz', '71234568', 'ana.martinez@gmail.com', 'https://example.com/ana.jpg', TRUE)
RETURNING id_persona;

-- id_persona = 2
INSERT INTO administrador (id_persona, cargo, direccion)
VALUES (2, 'Coordinadora', 'Calle Falsa 456');

-- -------------------------------
-- Administrador 3
-- -------------------------------
INSERT INTO persona (fecha_nacimiento, nombre, a_paterno, a_materno, telefono, email, url_imagen, estado)
VALUES ('1982-11-05', 'Luis', 'Fernandez', 'Diaz', '71234569', 'luis.fernandez@gmail.com', 'https://example.com/luis.jpg', TRUE)
RETURNING id_persona;

-- id_persona = 3
INSERT INTO administrador (id_persona, cargo, direccion)
VALUES (3, 'Subgerente', 'Av. Principal 789');

-- -------------------------------
-- Administrador 4
-- -------------------------------
INSERT INTO persona (fecha_nacimiento, nombre, a_paterno, a_materno, telefono, email, url_imagen, estado)
VALUES ('1978-03-18', 'Maria', 'Sanchez', 'Lopez', '71234570', 'maria.sanchez@gmail.com', 'https://example.com/maria.jpg', TRUE)
RETURNING id_persona;

-- id_persona = 4
INSERT INTO administrador (id_persona, cargo, direccion)
VALUES (4, 'Directora', 'Calle Secundaria 321');

-- -------------------------------
-- Administrador 5
-- -------------------------------
INSERT INTO persona (fecha_nacimiento, nombre, a_paterno, a_materno, telefono, email, url_imagen, estado)
VALUES ('1995-09-30', 'Jorge', 'Gonzalez', 'Perez', '71234571', 'jorge.gonzalez@gmail.com', 'https://example.com/jorge.jpg', TRUE)
RETURNING id_persona;

-- id_persona = 5
INSERT INTO administrador (id_persona, cargo, direccion)
VALUES (5, 'Jefe de Área', 'Av. Central 654');


--usuario_control
--invitado
--cliente
--persona
--macrodistrito
INSERT INTO macrodistrito (nombre_macrodistrito, descripcion, estado) VALUES
('Centro', 'Zona central de la ciudad', true),
('Sur', 'Área residencial y comercial', true),
('Norte', 'Zona industrial y de transporte', true),
('Este', 'Área en expansión urbana', true),
('Oeste', 'Zona con parques y áreas verdes', true);

--zona
INSERT INTO zona (nombre_zona, descripcion, estado, id_macrodistrito) VALUES
('Zona A', 'Área deportiva principal', true, 1),
('Zona B', 'Cancha de fútbol', true, 1),
('Zona C', 'Piscina olímpica', true, 2),
('Zona D', 'Gimnasio municipal', true, 2),
('Zona E', 'Pista de atletismo', true, 3),
('Zona F', 'Área de yoga y pilates', true, 3),
('Zona G', 'Zona de entrenamiento funcional', true, 1),
('Zona H', 'Área de recreación familiar', true, 2);

--areadeportiva
INSERT INTO AreaDeportiva (
  nombre_area, descripcion_area, email_area, telefono_area,
  hora_inicio_area, hora_fin_area, url_imagen,
  latitud, longitud, id_zona, id_persona, estado
) VALUES
('Cancha Sintética Norte', 'Cancha de fútbol 8 con césped sintético.', 'contacto1@deportes.com', '70123456',
 '08:00:00', '22:00:00', 'cancha_norte.jpg', -16.500, -68.150, 1, 1, TRUE),

('Coliseo Municipal', 'Coliseo techado para básquet y voleibol.', 'coliseo@deportes.com', '70234567',
 '07:00:00', '21:00:00', 'coliseo_muni.jpg', -16.495, -68.132, 2, 2, TRUE),

('Piscina Olímpica', 'Piscina semiolímpica climatizada.', 'piscina@deportes.com', '70345678',
 '06:00:00', '20:00:00', 'piscina.jpg', -16.510, -68.140, 3, 1, TRUE),

('Cancha de Tenis Sur', 'Cancha de tenis de arcilla.', 'tenis@deportes.com', '70456789',
 '09:00:00', '19:00:00', 'tenis_sur.jpg', -16.520, -68.155, 1, 2, TRUE),

('Gimnasio Central', 'Área equipada con pesas y máquinas.', 'gym@deportes.com', '70567890',
 '05:30:00', '23:00:00', 'gimnasio.jpg', -16.480, -68.125, 2, 3, TRUE);


--cancha
INSERT INTO cancha (
    nombre_cancha,
    costo_hora,
    capacidad,
    mantenimiento,
    hora_inicio,
    hora_fin,
    tipo_superficie,
    tamano,
    iluminacion,
    cubierta,
    url_imagen,
    estado,
    id_areadeportiva
) VALUES
('Cancha Fútbol Norte', 80.0, 22, 'ninguno', '08:00', '18:00', 'césped sintético', '30x50', 'LED', 'techada', 'img/cancha1.jpg', true, 1),
('Cancha Básquet Sur', 60.0, 12, 'preventivo', '09:00', '17:00', 'parquet', '20x30', 'fluorescente', 'abierta', 'img/cancha2.jpg', true, 1),
('Cancha Vóley Arena', 50.0, 10, 'correctivo', '07:00', '15:00', 'arena', '18x25', 'natural', 'abierta', 'img/cancha3.jpg', true, 2),
('Cancha Multideporte', 70.0, 16, 'ninguno', '06:00', '14:00', 'cemento', '25x40', 'LED', 'techada', 'img/cancha4.jpg', true, 2),
('Cancha Tenis', 90.0, 4, 'preventivo', '10:00', '18:00', 'arcilla', '23x10', 'LED', 'techada', 'img/cancha5.jpg', true, 3),
('Cancha Fútbol 7', 85.0, 14, 'correctivo', '08:00', '16:00', 'césped natural', '40x60', 'fluorescente', 'abierta', 'img/cancha6.jpg', true, 4),
('Cancha Indoor', 75.0, 10, 'ninguno', '07:30', '15:30', 'parquet', '20x20', 'LED', 'techada', 'img/cancha7.jpg', true, 5),
('Cancha Rugby', 100.0, 30, 'preventivo', '06:00', '14:00', 'césped natural', '60x100', 'natural', 'abierta', 'img/cancha8.jpg', true, 5),
('Cancha Ping Pong', 40.0, 4, 'ninguno', '09:00', '17:00', 'madera', '5x10', 'LED', 'techada', 'img/cancha9.jpg', true, 5),
('Cancha Patinaje', 65.0, 20, 'correctivo', '08:00', '16:00', 'cemento', '30x60', 'fluorescente', 'abierta', 'img/cancha10.jpg', true, 5);


--equipamiento
INSERT INTO equipamiento (nombre_equipamiento, tipo_equipamiento, descripcion, estado, url_imagen)
VALUES 
('Balón de fútbol', 'Deporte', 'Balón oficial de fútbol tamaño 5', TRUE, 'https://example.com/ball.jpg'),
('Red de voleibol', 'Deporte', 'Red reglamentaria para voleibol', TRUE, 'https://example.com/net.jpg'),
('Portería de fútbol', 'Deporte', 'Portería plegable de 3x2 metros', TRUE, 'https://example.com/goal.jpg'),
('Conos de entrenamiento', 'Accesorio', 'Juego de 10 conos para entrenamiento', TRUE, 'https://example.com/cones.jpg'),
('Colchonetas', 'Gimnasio', 'Colchonetas para ejercicios y estiramientos', TRUE, 'https://example.com/mats.jpg');

--equipamiento

--disciplina
--reserva
--pago
--qr
--cancelacion
--comentario

/*AGREGEN EN LIMPIO LA PARTE DE ARRIBA*/














-- 25 registros para PERSONA

INSERT INTO persona (
    fecha_nacimiento,
    nombre,
    a_paterno,
    a_materno,
    telefono,
    email,
    ci
) VALUES 
('1990-05-12', 'Carlos', 'Gonzales', 'Lopez', '7894561', 'carlos.gonzales@gmail.com', '1234567'),
('1985-11-23', 'María', 'Fernandez', 'Rojas', '7654321', 'maria.fernandez@gmail.com', '9876543'),
('2000-02-08', 'Luis', 'Ramirez', 'Torrez', '7123456', 'luis.ramirez@gmail.com', '4567890'),
('1995-07-30', 'Ana', 'Sanchez', 'Perez', '7981234', 'ana.sanchez@gmail.com', '3216549'),
('1988-03-15', 'Jorge', 'Mendoza', 'Quispe', '7012345', 'jorge.mendoza@gmail.com', '6543210'),
('1992-06-18', 'Sofia', 'Vargas', 'Luna', '7345678', 'sofia.vargas@gmail.com', '1122334'),
('1999-09-09', 'Diego', 'Castro', 'Morales', '7456123', 'diego.castro@gmail.com', '7788990'),
('1987-12-01', 'Valeria', 'Ortiz', 'Flores', '7567890', 'valeria.ortiz@gmail.com', '9988776'),
('1993-04-25', 'Andrés', 'Rojas', 'Gutiérrez', '7678901', 'andres.rojas@gmail.com', '3344556'),
('1996-08-14', 'Camila', 'Torres', 'Salazar', '7789012', 'camila.torres@gmail.com', '6677889'),
('1991-01-10', 'Fernando', 'López', 'Cruz', '7234567', 'fernando.lopez@gmail.com', '4455667'),
('1989-10-05', 'Isabel', 'Martínez', 'Delgado', '7345123', 'isabel.martinez@gmail.com', '5566778'),
('1997-03-22', 'Ricardo', 'Paredes', 'Aguilar', '7456789', 'ricardo.paredes@gmail.com', '6677880'),
('1994-06-30', 'Lucía', 'Gómez', 'Rivera', '7561234', 'lucia.gomez@gmail.com', '7788991'),
('1998-12-12', 'Esteban', 'Navarro', 'Silva', '7672345', 'esteban.navarro@gmail.com', '8899002'),
('1990-04-17', 'Paola', 'Cortez', 'Mamani', '7123987', 'paola.cortez@gmail.com', '1122445'),
('1986-07-08', 'Martín', 'Zeballos', 'Choque', '7234890', 'martin.zeballos@gmail.com', '2233445'),
('1992-11-19', 'Natalia', 'Camacho', 'Vargas', '7345671', 'natalia.camacho@gmail.com', '3344557'),
('1999-05-03', 'Sebastián', 'Peña', 'Rojas', '7456129', 'sebastian.pena@gmail.com', '4455668'),
('1993-09-27', 'Gabriela', 'Flores', 'Guzmán', '7567891', 'gabriela.flores@gmail.com', '5566779'),
('1996-02-14', 'Rodrigo', 'Salinas', 'Delgado', '7678902', 'rodrigo.salinas@gmail.com', '6677881'),
('1988-08-08', 'Carla', 'Mamani', 'Quispe', '7789013', 'carla.mamani@gmail.com', '7788992'),
('1995-01-01', 'Daniel', 'Gutiérrez', 'Rivera', '7890123', 'daniel.gutierrez@gmail.com', '8899003'),
('1991-06-06', 'Verónica', 'Luna', 'Aguilar', '7012346', 'veronica.luna@gmail.com', '9900112'),
('1997-10-10', 'Pablo', 'Cárdenas', 'Torrez', '7123457', 'pablo.cardenas@gmail.com', '1011121');


-- cliente
INSERT INTO cliente (
    id_cliente,
    estado_cliente
) VALUES 
(1, 'activo'),
(2, 'activo'),
(3, 'activo'),
(4, 'activo'),
(5, 'activo'),
(6, 'activo');



-- usuario_control

INSERT INTO usuario_control (
    id_us_control,
    estado_operativo,
    hora_inicio_turno,
    hora_fin_turno,
    direccion
) VALUES 
(12, 'activo', '08:00:00', '16:00:00', 'Av. Sucre 101, La Paz'),
(13, 'activo', '14:00:00', '22:00:00', 'Calle Comercio 55, La Paz'),
(14, 'activo', '07:00:00', '15:00:00', 'Zona Central, Calle 3, La Paz'),
(15, 'activo', '10:00:00', '18:00:00', 'Av. Camacho 200, La Paz'),
(16, 'activo', '06:00:00', '14:00:00', 'Calle Murillo 88, La Paz');


-- administrador
INSERT INTO administrador (
    id_administrador,
    cargo,
    direccion
) VALUES 
(7, 'Administrador', 'Av. Libertador 123, La Paz'),
(8, 'Administrador', 'Calle 21 de Calacoto, La Paz'),
(9, 'Administrador', 'Zona Sur, Calle 10, La Paz'),
(10, 'Administrador', 'Av. Arce 456, La Paz'),
(11, 'Administrador', 'Calle Jaimes Freyre 89, La Paz');


-- invitado
INSERT INTO invitado (
    id_invitado,
    verificado
) VALUES 
(17, true),
(18, true),
(19, true),
(20, true),
(21, true),
(22, true),
(23, true),
(24, true),
(25, true);




------------------------SQL JOSUE ENTIDADES PRINCIPALES-------------------
select rx.estado_reserva, rx.monto_total, px.monto, px.tipo_pago
from reserva rx, pago px
where px.id_reserva = rx.id_reserva


-- INSERTS de ejemplo para disciplina
SELECT * FROM disciplina
-- INSERTS de ejemplo para Disciplina
INSERT INTO disciplina (id_disciplina, nombre, descripcion)
VALUES 
    (1, 'Fútbol', 'Deporte de equipo, 11 jugadores por lado'),
    (2, 'Baloncesto', 'Deporte de equipo, 5 jugadores por lado'),
    (3, 'Natación', 'Deporte individual o por relevos en piscina');


-- INSERTS de ejemplo para Reserva
INSERT INTO reserva (fecha_creacion, fecha_reserva, hora_inicio, hora_fin, estado_reserva, monto_total, observaciones, id_cliente)
VALUES ('2025-09-20', '2025-09-25', '10:00:00', '12:00:00', 'Confirmada', 150.00, 'Reserva para torneo amistoso.', 4);

INSERT INTO reserva (fecha_creacion, fecha_reserva, hora_inicio, hora_fin, estado_reserva, monto_total, observaciones, id_cliente)
VALUES ('2025-09-20', '2025-09-26', '14:00:00', '16:00:00', 'Confirmada', 180.00, 'Reserva para entrenamiento de equipo.', 5);

INSERT INTO reserva (fecha_creacion, fecha_reserva, hora_inicio, hora_fin, estado_reserva, monto_total, observaciones, id_cliente)
VALUES ('2025-09-20', '2025-09-27', '18:00:00', '20:00:00', 'Confirmada', 200.00, 'Reserva para partido amistoso.', 6);


-- INSERTS de ejemplo para Pagos
-- Pagos para la reserva con id 1 (cliente 5)
INSERT INTO pago (monto, fecha, tipo_pago, metodo_pago, estado, id_reserva)
VALUES (90.00, '2025-09-20', 'parcial', 'tarjeta', 'confirmado', 1);

INSERT INTO pago (monto, fecha, tipo_pago, metodo_pago, estado, id_reserva)
VALUES (90.00, '2025-09-21', 'parcial', 'efectivo', 'confirmado', 1);


-----canchas inventada con valor null
INSERT INTO cancha (nombre_cancha, costo_hora, capacidad, estado_cancha, mantenimiento, hora_inicio, hora_fin, tipo_superficie, tamano, iluminacion, cubierta, url_imagen, estado, id_areadeportiva)
VALUES ('Cancha Sintética A', 50.00, 10, 'Disponible', 'No requiere', '08:00:00', '22:00:00', 'Sintética', '25x15', 'Sí', 'Cubierta', 'https://example.com/cancha.jpg', TRUE, NULL);

INSERT INTO cancha (nombre_cancha, costo_hora, capacidad, estado_cancha, mantenimiento, hora_inicio, hora_fin, tipo_superficie, tamano, iluminacion, cubierta, url_imagen, estado, id_areadeportiva)
VALUES ('Cancha Sintética B', 50.00, 10, 'Disponible', 'No requiere', '08:00:00', '22:00:00', 'Sintética', '25x15', 'Sí', 'Cubierta', 'https://example.com/cancha.jpg', TRUE, NULL);



--- QR KAREN------------------------------------------------ ---
-- QR 1 para reserva 1
INSERT INTO qr (
    codigo_qr,
    fecha_generacion,
    fecha_expiracion,
    estado,
    descripcion,
    id_reserva,
    id_invitado,
    id_us_control
) VALUES (
    'QR-R1-001',
    '2025-09-21 08:00:00',
    '2025-09-22 08:00:00',
    TRUE,
    'Acceso cancha norte',
    1,
    2,
    3
);

-- QR 2 para reserva 1
INSERT INTO qr (
    codigo_qr,
    fecha_generacion,
    fecha_expiracion,
    estado,
    descripcion,
    id_reserva,
    id_invitado,
    id_us_control
) VALUES (
    'QR-R1-002B',
    '2025-09-21 08:15:00',
    '2025-09-22 08:15:00',
    TRUE,
    'Acceso cancha norte - segundo invitado',
    1,
    3,
    4
);

-- QR 3 para reserva 1
INSERT INTO qr (
    codigo_qr,
    fecha_generacion,
    fecha_expiracion,
    estado,
    descripcion,
    id_reserva,
    id_invitado,
    id_us_control
) VALUES (
    'QR-R1-003',
    '2025-09-21 08:30:00',
    '2025-09-22 08:30:00',
    TRUE,
    'Acceso cancha norte - tercer invitado',
    1,
    4,
    5
);

-- QR 1 para reserva 2
INSERT INTO qr (
    codigo_qr,
    fecha_generacion,
    fecha_expiracion,
    estado,
    descripcion,
    id_reserva,
    id_invitado,
    id_us_control
) VALUES (
    'QR-R2-001',
    '2025-09-21 09:00:00',
    '2025-09-22 09:00:00',
    TRUE,
    'Acceso cancha sur',
    2,
    5,
    6
);

-- CANCELACION ----
INSERT INTO cancelacion (
    fecha_cancelacion,
    hora_cancelacion,
    motivo,
    estado,
    id_reserva,
    id_cliente
) VALUES (
    '2025-09-21',
    '09:45:00',
    'Cancelación por reprogramación de evento deportivo',
    TRUE,
    3,  -- ID de reserva existente
    6   -- ID de cliente existente, esto validar...... solo el cliente q
    -- la resreva debria poder cancelar
);

-- COMENTARIO --

-- Comentario 1
INSERT INTO comentario (
    contenido,
    calificacion,
    fecha,
    estado,
    id_persona,
    id_cancha
) VALUES (
    'Muy buena experiencia. El césped estaba en excelentes condiciones.',
    5,
    '2025-09-20',
    TRUE,
    2,  -- ID de persona existente
    1   -- ID de cancha existente
);

-- Comentario 2
INSERT INTO comentario (
    contenido,
    calificacion,
    fecha,
    estado,
    id_persona,
    id_cancha
) VALUES (
    'La cancha estaba mojada y resbalosa. Deberían mejorar el drenaje.',
    3,
    '2025-09-21',
    TRUE,
    3,  -- otra persona
    2   -- otra cancha
);


BEGIN;

-- ==========================================
-- 1️⃣ MACRODISTRITO
-- ==========================================
INSERT INTO public.macrodistrito (nombre_macrodistrito, descripcion, estado)
VALUES
('Centro', 'Zona central de la ciudad', true),
('Sur', 'Área residencial y comercial', true),
('Norte', 'Zona industrial y de transporte', true),
('Este', 'Área en expansión urbana', true),
('Oeste', 'Zona con parques y áreas verdes', true),
('Centro Historico', 'Zona central y administrativa de La Paz', true),
('Cotahuma', 'Área residencial y cultural con zonas tradicionales', true),
('Max Paredes', 'Macrodistrito comercial y popular con gran movimiento urbano', true),
('San Antonio', 'Zona residencial en expansión con áreas deportivas', true),
('Sur Moderno', 'Macrodistrito con urbanizaciones modernas y centros deportivos', true);

-- ==========================================
-- 2️⃣ ZONA
-- ==========================================
INSERT INTO public.zona (nombre_zona, descripcion, estado, id_macrodistrito)
VALUES
('Sopocachi', 'Zona tradicional con parques y cafés, muy activa deportivamente', true, 1),
('Miraflores', 'Área central con múltiples complejos deportivos y canchas', true, 1),
('Villa Fátima', 'Zona comercial con espacios deportivos comunales', true, 3),
('Kupini', 'Barrio del macrodistrito San Antonio con áreas recreativas', true, 4),
('Achumani', 'Zona residencial del sur con clubes deportivos', true, 5),
('Calacoto', 'Área moderna con canchas de fútbol rápido y gimnasios', true, 5),
('Zona A', 'Área deportiva principal', true, 1),
('Zona B', 'Cancha de fútbol', true, 1),
('Zona C', 'Piscina olímpica', true, 2),
('Zona D', 'Gimnasio municipal', true, 2),
('Zona E', 'Pista de atletismo', true, 3),
('Zona F', 'Área de yoga y pilates', true, 3),
('Zona G', 'Zona de entrenamiento funcional', true, 1),
('Zona H', 'Área de recreación familiar', true, 2);


-- ==========================================
-- 3️⃣ PERSONA
-- ==========================================
INSERT INTO public.persona (nombre, a_paterno, a_materno, email, telefono, fecha_nacimiento, estado, url_imagen)
VALUES
('Carlos', 'Lopez', 'Gomez', 'carlos.lopez@gmail.com', '71234567','1985-04-12',  true, 'https://example.com/carlos.jpg'),
('Ana', 'Martinez', 'Ruiz', 'ana.martinez@gmail.com', '71234568', '1990-06-25', true, 'https://example.com/ana.jpg'),
('Luis', 'Fernandez', 'Diaz', 'luis.fernandez@gmail.com', '71234569', '1982-11-05', true, 'https://example.com/luis.jpg'),
('Maria', 'Sanchez', 'Lopez', 'maria.sanchez@gmail.com', '71234570', '1978-03-18', true, 'https://example.com/maria.jpg'),
('Jorge', 'Gonzalez', 'Perez', 'jorge.gonzalez@gmail.com', '71234571', '1995-09-30', true, 'https://example.com/jorge.jpg'),
('Carlos', 'Mendoza', 'Quispe', 'cmendoza@lapaz.bo', '72015489', '1985-07-15', true, 'https://i.imgur.com/XA1Q3bE.png'),
('María', 'Fernández', 'Loza', 'mfernandez@lapaz.bo', '76743219', '1990-04-10', true, 'https://i.imgur.com/E6F5nIT.png'),
('Jorge', 'Choque', 'Apaza', 'jchoque@lapaz.bo', '69874123', '1993-02-28', true, 'https://i.imgur.com/nEczp7Z.png'),
('Lucía', 'Rojas', 'Mamani', 'lrojas@lapaz.bo', '76584912', '1998-09-22', true, 'https://i.imgur.com/YfL1l5C.png'),
('Andrés', 'Callisaya', 'Copa', 'acallisaya@lapaz.bo', '78941236', '1992-06-17', true, 'https://i.imgur.com/nzAYbHD.png'),
('Elena', 'Flores', 'Aliaga', 'eflores@lapaz.bo', '71345892', '1989-11-08', true, 'https://i.imgur.com/a6eWDjR.png'),
('Víctor', 'Quenta', 'Villca', 'vquenta@lapaz.bo', '75841236', '1983-12-20', true, 'https://i.imgur.com/FynUcjB.png'),
('Gabriela', 'Limachi', 'Cruz', 'glimachi@lapaz.bo', '74236987', '1996-03-11', true, 'https://i.imgur.com/Pa3Vt7N.png'),
('Rodrigo', 'Huanca', 'Salvatierra', 'rhuanca@lapaz.bo', '73361982', '1999-08-04', true, 'https://i.imgur.com/vsVEdwz.png'),
('Natalia', 'Paredes', 'Choque', 'nparedes@lapaz.bo', '74125896', '1995-01-30', true, 'https://i.imgur.com/hpA3KFi.png');


-- ==========================================
-- 4️⃣ ADMINISTRADOR
-- ==========================================
-- Asociamos administradores a algunas personas insertadas en la parte 1.
INSERT INTO public.administrador (cargo, direccion, id_persona)
VALUES 
('Gerente', 'Av. Siempre Viva 123', 1),
('Administrador General', 'Av. Mariscal Santa Cruz Nº 1450', 2),
('Subgerente', 'Av. Principal 789', 3),
('Directora', 'Calle Secundaria 321', 4),
('Jefe de Área', 'Av. Central 654', 5),
('Coordinadora', 'Calle Falsa 456', 6),
('Encargada de Complejo Deportivo', 'Calle Ecuador Nº 2215, Sopocachi', 7),
('Administrador de Zona Sur', 'Av. Ballivián Nº 510, Calacoto', 8);

-- ==========================================
-- 5️⃣ ÁREA DEPORTIVA
-- ==========================================
INSERT INTO public.areadeportiva (
    descripcion_area, email_area, estado, hora_fin_area, hora_inicio_area,
    latitud, longitud, nombre_area, telefono_area, url_imagen, id_persona, id_zona
)
VALUES
('Complejo Deportivo Municipal Miraflores, con canchas sintéticas y gimnasio cerrado.',
 'miraflores@lapaz.bo', true, '22:00:00', '06:00:00',
 -16.5012, -68.1193, 'Complejo Miraflores', '22458971',
 'https://i.imgur.com/5CvjY9D.png', 1, 2),

('Polideportivo Sopocachi, espacio cubierto para futsal, voleibol y básquet.',
 'sopocachi@lapaz.bo', true, '21:00:00', '07:00:00',
 -16.5108, -68.1279, 'Polideportivo Sopocachi', '22245678',
 'https://i.imgur.com/f0Jb0eY.png', 2, 1),

('Club Deportivo Achumani, moderno y con canchas de fútbol 7 y frontón.',
 'achumani@lapaz.bo', true, '23:00:00', '06:00:00',
 -16.5554, -68.0745, 'Club Achumani', '27894562',
 'https://i.imgur.com/dGLb5dr.png', 5, 5),

('Cancha techada para fútbol 5, ubicada en la zona de Miraflores, ideal para torneos nocturnos.',
 'contacto.miraflores@lapazdeportes.bo', TRUE, '22:00:00', '08:00:00',
 -16.499, -68.119, 'Cancha Miraflores', '70123456',
 'cancha_miraflores.jpg', 1, 1),
('Complejo polideportivo con varias disciplinas disponibles, ubicado en la zona Sur de La Paz.',
 'complejosur@lapazdeportes.bo', TRUE, '21:00:00', '07:00:00',
 -16.538, -68.077, 'Complejo Deportivo Sur', '70234567',
 'complejo_sur.jpg', 2, 2),
('Piscina semiolímpica cubierta y climatizada, en la zona Sopocachi.',
 'piscina.sopocachi@lapazdeportes.bo', TRUE, '20:00:00', '06:00:00',
 -16.507, -68.126, 'Piscina Sopocachi', '70345678',
 'piscina_sopocachi.jpg', 3, 3),
('Coliseo moderno para básquet y voleibol, ubicado en la zona Villa Fátima.',
 'coliseo.vf@lapazdeportes.bo', TRUE, '22:30:00', '07:30:00',
 -16.478, -68.097, 'Coliseo Villa Fátima', '70456789',
 'coliseo_villafatima.jpg', 4, 4),
('Centro deportivo con gimnasio y canchas múltiples, en el centro de La Paz.',
 'centrodeportivo@lapazdeportes.bo', TRUE, '23:00:00', '05:30:00',
 -16.495, -68.133, 'Centro Deportivo Central', '70567890',
 'centro_deportivo_central.jpg', 5, 5);




-- ==========================================
-- 6️⃣ CANCHA
-- ==========================================
INSERT INTO public.cancha (
    capacidad, costo_hora, cubierta, estado, hora_fin, hora_inicio,
    iluminacion, mantenimiento, nombre_cancha, tamano, tipo_superficie, url_imagen, id_areadeportiva
)
VALUES
(12, 80.00, 'Techada', true, '22:00', '06:00', 'LED', 'Mensual', 'Cancha Fútbol 7 Miraflores', '40x20', 'Sintética', 'https://i.imgur.com/8s9UbUl.png', 1),
(10, 60.00, 'Descubierta', true, '21:00', '07:00', 'Halógena', 'Quincenal', 'Cancha Multiuso Sopocachi', '30x15', 'Cemento pulido', 'https://i.imgur.com/b7WUt9p.png', 2),
(14, 100.00, 'Semi-techada', true, '23:00', '06:00', 'LED', 'Semanal', 'Cancha Fútbol 7 Achumani', '50x30', 'Césped sintético', 'https://i.imgur.com/ESN1pU9.png', 3),
(8, 50.00, 'Descubierta', true, '22:00', '07:00', 'Halógena', 'Mensual', 'Cancha Frontón Achumani', '20x10', 'Concreto', 'https://i.imgur.com/FnfwK8b.png', 3);

-- ==========================================
-- 7️⃣ EQUIPAMIENTO
-- ==========================================
INSERT INTO public.equipamiento (descripcion, estado, nombre_equipamiento, tipo_equipamiento, url_imagen)
VALUES
('Balones oficiales de fútbol 7', true, 'Balón de Fútbol', 'Deportivo', 'https://i.imgur.com/FTkI7tJ.png'),
('Conos plásticos de entrenamiento', true, 'Conos de Entrenamiento', 'Accesorio', 'https://i.imgur.com/mVqYrhE.png'),
('Petos deportivos de diferentes colores', true, 'Petos', 'Indumentaria', 'https://i.imgur.com/wVuQ5Bg.png'),
('Red de arco estándar para fútbol 7', true, 'Red de Arco', 'Deportivo', 'https://i.imgur.com/qbPqu6p.png'),
('Marcadores para líneas de cancha', true, 'Pintura Deportiva', 'Accesorio', 'https://i.imgur.com/gfEpIY5.png');



-- Tabla CLIENTE (relación 1:1 con PERSONA)
INSERT INTO public.cliente (id_persona, estado_cliente) VALUES
(6, 'Activo'),
(7, 'Activo'),
(8, 'Activo'),
(9, 'Activo'),
(10, 'Inactivo');



COMMIT;
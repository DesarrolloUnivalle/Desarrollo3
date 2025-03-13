CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena TEXT NOT NULL,
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
);

CREATE TABLE ordenes (
    id SERIAL PRIMARY KEY,
    usuario_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) NOT NULL CHECK (estado IN ('Pendiente', 'Pagada', 'Enviada', 'Entregada', 'Cancelada')),
    total DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE detalle_orden (
    id SERIAL PRIMARY KEY,
    orden_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (orden_id) REFERENCES ordenes(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

CREATE TABLE entregas (
    id SERIAL PRIMARY KEY,
    orden_id INT NOT NULL,
    repartidor_id INT NOT NULL,
    estado VARCHAR(50) NOT NULL CHECK (estado IN ('Asignado', 'En camino', 'Entregado', 'Cancelado')),
    fecha_entrega TIMESTAMP,
    ubicacion_actual TEXT,
    FOREIGN KEY (orden_id) REFERENCES ordenes(id) ON DELETE CASCADE,
    FOREIGN KEY (repartidor_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    orden_id INT NOT NULL,
    estado VARCHAR(50) NOT NULL CHECK (estado IN ('Pendiente', 'Completado', 'Fallido')),
    fecha_pago TIMESTAMP,
    transaccion_id VARCHAR(100) UNIQUE,
    FOREIGN KEY (orden_id) REFERENCES ordenes(id) ON DELETE CASCADE
);

CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    servicio VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(50) CHECK (tipo IN ('INFO', 'WARNING', 'ERROR'))
);

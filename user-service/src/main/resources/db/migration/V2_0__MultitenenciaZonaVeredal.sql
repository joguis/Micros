-- Crear tabla zona_veredal
CREATE TABLE IF NOT EXISTS user_service.zona_veredal
(
    id          SERIAL PRIMARY KEY,
    nombre      VARCHAR(200) NOT NULL,
    codigo      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Agregar columna zona_veredal_id a la tabla user (nullable inicialmente)
ALTER TABLE user_service.user 
ADD COLUMN IF NOT EXISTS zona_veredal_id BIGINT;

-- Crear foreign key
ALTER TABLE user_service.user
ADD CONSTRAINT fk_user_zona_veredal 
FOREIGN KEY (zona_veredal_id) 
REFERENCES user_service.zona_veredal(id);

-- Crear índice para mejorar performance
CREATE INDEX IF NOT EXISTS idx_user_zona_veredal_id 
ON user_service.user(zona_veredal_id);

-- Habilitar Row Level Security (opcional, pero recomendado para mayor seguridad)
ALTER TABLE user_service.user ENABLE ROW LEVEL SECURITY;

-- Crear política RLS (esto requiere que pases el zona_veredal_id como variable de sesión)
-- CREATE POLICY zona_veredal_policy ON user_service.user
-- FOR ALL
-- USING (zona_veredal_id = current_setting('app.current_zona_veredal_id', true)::bigint);

-- Insertar algunas zonas veredales de ejemplo
INSERT INTO user_service.zona_veredal (nombre, codigo, descripcion)
VALUES 
    ('Zona Veredal Norte', 'ZV-NORTE', 'Zona veredal ubicada en la región norte'),
    ('Zona Veredal Sur', 'ZV-SUR', 'Zona veredal ubicada en la región sur'),
    ('Zona Veredal Centro', 'ZV-CENTRO', 'Zona veredal ubicada en la región centro')
ON CONFLICT DO NOTHING;
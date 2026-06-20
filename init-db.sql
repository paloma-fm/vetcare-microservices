-- Script de inicialización de bases de datos VetCare
-- Se ejecuta automáticamente al primer arranque del contenedor MySQL

CREATE DATABASE IF NOT EXISTS `auth`          CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `vetcare_clientes`    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `vetcare_mascotas`    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `vetcare_veterinarios` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `vetcare_atenciones`  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `vetcare_vacunas`     CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Conceder permisos al usuario root desde cualquier host
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;


# 🛰️ Proyecto Final - API IoT Backend (Java + Spring Boot)

## 📌 Descripción General

Este proyecto consiste en el desarrollo de una **API REST** en Java para una plataforma IoT orientada al sector minero. Su objetivo principal es permitir el **registro, almacenamiento y consulta de datos** generados por sensores distribuidos en distintas ubicaciones, asegurando trazabilidad, seguridad y eficiencia en la integración de dispositivos de distintas tecnologías (ESP32 y Zigbee).

---

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Security (roles & JWT)**
- **PostgreSQL** (base de datos relacional)
- **JPA / Hibernate**
- **Maven**
- **Swagger / OpenAPI**
- **Docker (opcional)**
- **Postman (para pruebas)**

---

## 🧱 Estructura del Modelo de Datos

```txt
Admin (rol)
 └── Company
       └── Location
             └── Sensor
                   └── Sensor Data (variable -> valor)
```

- Cada compañía se identifica con una `company_api_key`.
- Cada sensor se identifica con una `sensor_api_key`.

---

## 📡 Endpoints Principales

> Todos los endpoints requieren `company-api-key` en los headers, excepto la inserción de `sensor_data`, que usa `sensor_api_key`.

### 🏢 Company (`/api/v1/companies`)
- `POST`: Crear compañía (`ADMIN`)
- `GET`: Listar compañías (`ADMIN`)
- `PUT /{id}`: Editar compañía
- `DELETE /{id}`: Eliminar compañía

### 📍 Location (`/api/v1/locations`)
- `POST`: Crear locación
- `GET`: Listar todas las locaciones
- `GET /{id}`: Obtener locación por ID
- `PUT /{id}`: Actualizar locación (`ADMIN`)
- `DELETE /{id}`: Eliminar locación (`ADMIN`)

### 📦 Sensor (`/api/v1/sensors`)
- `POST`: Crear sensor
- `GET`: Listar sensores
- `GET /{id}`: Consultar sensor
- `PUT /{id}`: Actualizar sensor
- `DELETE /{id}`: Eliminar sensor

### 📈 Sensor Data (`/api/v1/sensor_data`)
- `POST`: Insertar datos (con `sensor_api_key`)
- `GET`: Consultar datos históricos  
  Parámetros:
  - `from`: timestamp (EPOCH)
  - `to`: timestamp (EPOCH)
  - `sensor_id`: lista de IDs

---

## 🛡️ Seguridad

- Roles: `ADMIN` y usuarios autenticados.
- Seguridad basada en JWT.
- Protección por `@PreAuthorize` en endpoints sensibles.
- Acceso a datos por API keys.

---

## ⚙️ Instalación y Ejecución

### 1. Clonar repositorio
```bash
git clone https://github.com/<tu_usuario>/iot-api-backend.git
cd iot-api-backend
```

### 2. Configurar base de datos
Editar `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/iotdb
spring.datasource.username=postgres
spring.datasource.password=tu_clave
```

### 3. Crear base de datos
Usar script SQL:
```bash
psql -U postgres -d iotdb -f schema.sql
```

### 4. Ejecutar el proyecto
```bash
./mvnw spring-boot:run
```

Acceso local: [http://localhost:8080](http://localhost:8080)

---

## 🧪 Pruebas con Postman

1. Importa la colección `IoT_API.postman_collection.json`.
2. Asegúrate de tener una `company_api_key` válida.
3. Realiza pruebas con:
   - Creación de locaciones y sensores.
   - Inserción de datos.
   - Consultas por tiempo.

---

## 📁 Archivos clave

- `LocationController.java`: CRUD de ubicaciones
- `CompanyController.java`: Gestión de compañías
- `SensorController.java`: Registro de sensores
- `SensorDataController.java`: Ingesta y consulta de datos
- `CompanyServiceImpl.java`: Lógica del negocio

---

## 📍 Notas adicionales

- Este proyecto es parte del **programa Backend Java Developer (2024–2025)**.
- Desarrollado como parte del módulo final de integración.
- Compatible con despliegue en AWS EC2 o servicios como Railway, Render o Heroku.

---

## 🧑‍💻 Autor / Equipo

> Proyecto desarrollado por el grupo **Equipo 1**, cohorte Backend Java Developer 2024–2025  
> Contacto: [carlos.garcia@talentofuturo.com](mailto:carlos.garcia@talentofuturo.com)

---

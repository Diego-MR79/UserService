# Microservicio Usuarios

Servicio de usuarios del proyecto CapySoft.

## Docker

Construir la imágen:

```shell
docker build -t users-service .
```

Ejecutar contenedor:

```shell
docker run --name users-service --network capysoft_network -d -p 8003:8003 users-service
```

## Uso

Esta aplicación requiere que se este levantado el servicio de eureka-server y la base de datos mysql-server.

http://localhost:8003/users
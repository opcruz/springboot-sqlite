FROM ubuntu:24.04

WORKDIR /app

# Copiar ejecutable y archivos específicos al contenedor
COPY --chmod=755 target/SQLiteDemo /app/SQLiteDemo
COPY target/*.so /app/

# Exponer el puerto 8080
EXPOSE 8080

# Definir el entrypoint
ENTRYPOINT ["/app/SQLiteDemo"]

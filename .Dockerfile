# Imagen base con Java 17
FROM eclipse-temurin:21-jdk-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el JAR construido al contenedor
COPY target/*.jar app.jar

# Exponemos el puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java","-jar","/app/app.jar"]

# ETAPA 1: Construir la aplicación
# Usa una imagen de Maven para compilar tu código.
FROM maven:3.9-eclipse-temurin-21 AS builder

# Establece el directorio de trabajo dentro de la imagen.
WORKDIR /app

# Copia los archivos del proyecto (pom.xml y el código fuente).
COPY pom.xml .
COPY src ./src

# Compila el proyecto y crea el JAR ejecutable.
RUN mvn clean install -DskipTests

# ETAPA 2: Crear la imagen final
FROM eclipse-temurin:21-jre-jammy

# Crea el directorio de la aplicación y define la aplicación a correr.
WORKDIR /app

# Copia el JAR compilado desde la etapa 'builder' a la imagen final.
COPY --from=builder /app/target/*.jar app.jar

# Define el comando para ejecutar la aplicación cuando el contenedor inicie.
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expone el puerto por defecto de tu aplicación.
EXPOSE 8081
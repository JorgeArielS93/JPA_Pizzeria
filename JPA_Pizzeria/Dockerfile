# ===== ETAPA 1: CONSTRUCCIÓN DEL JAR =====
# Utilizamos Maven con OpenJDK 17 como imagen base para compilar la aplicación
# Esta primera etapa se usa solo para construir el archivo JAR
FROM maven:3.8.5-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo el archivo pom.xml primero para aprovechar la caché de Docker
# Esto permite reutilizar las dependencias descargadas si el pom.xml no cambia
COPY pom.xml ./

# Descargamos todas las dependencias definidas en el pom.xml
# El parámetro go-offline permite descargar las dependencias sin compilar el proyecto
RUN mvn dependency:go-offline

# Ahora copiamos el código fuente de la aplicación
# Esto se hace después de descargar las dependencias para no invalidar la caché si solo cambia el código
COPY src ./src

# Ejecutamos el comando de Maven para compilar y empaquetar la aplicación en un archivo JAR
# Omitimos las pruebas para acelerar el proceso de construcción (en producción podrías querer eliminar -DskipTests)
RUN mvn clean package -DskipTests

# Listamos el contenido del directorio target para verificar que el JAR se creó correctamente
RUN ls -la /app/target

# ===== ETAPA 2: CREACIÓN DE LA IMAGEN FINAL =====
# Utilizamos una imagen base ligera de OpenJDK 17 Alpine para la imagen final
# Esto reduce significativamente el tamaño de la imagen resultante
FROM openjdk:17-jdk-alpine

# Establecemos el directorio de trabajo para la aplicación
WORKDIR /app

# Copiamos solo el archivo JAR compilado desde la etapa de construcción
# Esto hace que la imagen final sea mucho más pequeña ya que no incluye el código fuente ni las herramientas de compilación
COPY --from=build /app/target/JPA_Pizzeria-0.0.1-SNAPSHOT.jar /app/JPA_Pizzeria.jar

# Exponemos el puerto 8080 que utilizará Spring Boot
# Esto documenta qué puertos necesita la aplicación pero no publica automáticamente el puerto
# Para publicar el puerto al ejecutar el contenedor, se debe usar el parámetro -p (ejemplo: docker run -p 8080:8080)
EXPOSE 8080

# Definimos el comando que se ejecutará cuando el contenedor se inicie
# El parámetro -jar indica que se ejecutará un archivo JAR
ENTRYPOINT ["java", "-jar", "/app/JPA_Pizzeria.jar"]

# Configuramos la zona horaria para Argentina/Jujuy
# Esto es importante para que las fechas y horas se muestren correctamente según la región
ENV TZ=America/Argentina/Jujuy
# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de Maven
COPY .mvn/ .mvn
COPY ../mvnw pom.xml ./
RUN chmod +x mvnw

# Descargar dependencias (esto ayuda a que las builds sean más rápidas después)
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente y construir el jar
COPY ../src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar solo el archivo .jar generado desde el stage anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto (Render suele usar el 8080 por defecto para Spring)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]